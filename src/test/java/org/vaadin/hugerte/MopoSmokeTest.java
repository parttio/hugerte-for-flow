package org.vaadin.hugerte;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import in.virit.mopo.Mopo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MopoSmokeTest {

    @LocalServerPort
    private int port;

    static Playwright playwright = Playwright.create();

    private Browser browser;
    private Page page;
    private Mopo mopo;

    @BeforeEach
    public void setup() {
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
//                        .setHeadless(false)
//                        .setDevtools(true)
                );

        page = browser.newPage();
        page.setDefaultTimeout(5000); // die faster if needed
        mopo = new Mopo(page);
    }

    @AfterEach
    public  void closePlaywright() {
        page.close();
        browser.close();
    }

    @Test
    public void smokeTest() {
        List<String> flakyIgnoredTests = List.of("githubissue2");
        int maxAttempts = 3;
        mopo.trackClientSideErrors();
        String rootUrl = "http://localhost:" + port + "/";
        mopo.getViewsReportedByDevMode(browser, rootUrl).forEach(viewName -> {
            if (flakyIgnoredTests.contains(viewName)) {
                System.out.println("Ignored test '%s' as flaky".formatted(viewName));
                return;
            }
            System.out.println("Checking %s".formatted(viewName));
            String url = rootUrl + viewName;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                // Reset accumulated errors so each attempt starts from a clean slate.
                mopo.getClientSideErrors().clear();
                page.navigate(url);
                mopo.waitForConnectionToSettle();
                List<String> errors = mopo.getClientSideErrors();
                if (errors.isEmpty()) {
                    System.out.println("Checked %s and it contained no JS errors.".formatted(viewName));
                    return;
                }
                if (attempt < maxAttempts) {
                    System.out.println("WARNING: JS errors on '%s' (attempt %d/%d), retrying: %s"
                            .formatted(viewName, attempt, maxAttempts, errors));
                    page.waitForTimeout(500);
                } else {
                    mopo.failOnClientSideErrors();
                }
            }
        });

    }

    @Test
    public void menuConfig() throws InterruptedException {
        mopo.trackClientSideErrors();
        String url = "http://localhost:" + port + "/menuconfig";
        page.navigate(url);
        PlaywrightAssertions.assertThat(page.locator("button").getByText("File")).isVisible();
        mopo.failOnClientSideErrors();
    }

    @Test
    public void setValueBackToPreviousValueAfterEditUpdatesUi() {
        page.navigate("http://localhost:" + port + "/setvalueafteredit");
        page.waitForFunction("() => document.querySelector('vaadin-huge-rte')?.editor?.initialized");

        // Editing away from A makes the server model differ from what "Reset to A" will restore, which is what leaves
        // the property mirror stale. "Reset to A" must then still push A back to the editor.
        simulateEdit("<h2>Hello Friends and Guys</h2>");

        page.locator("#reset").click();
        mopo.waitForConnectionToSettle();

        Object content = page.evaluate("() => document.querySelector('vaadin-huge-rte').editor.getContent()");
        assertEquals("<h2>Hello Friends</h2>", content);

        // Same case for the empty value: after another edit, clear() from the server must empty the editor too.
        simulateEdit("<h2>edited once more</h2>");

        page.locator("#clear").click();
        mopo.waitForConnectionToSettle();

        Object cleared = page.evaluate("() => document.querySelector('vaadin-huge-rte').editor.getContent()");
        assertEquals("", cleared);
    }

    /**
     * Simulates a user edit by setting the editor content and pushing the delta to the server. The throttle timestamp
     * is reset first, otherwise the delta can be swallowed by the throttle right after the editor's init flush.
     */
    private void simulateEdit(String html) {
        page.evaluate("(html) => { const el = document.querySelector('vaadin-huge-rte');"
                + " el._lastSyncedValueTimestamp = 0;"
                + " el.editor.setContent(html); el.onValueChange(); }", html);
        mopo.waitForConnectionToSettle();
    }

}
