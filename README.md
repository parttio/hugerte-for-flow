# HugeRTE for Flow

Vaadin Flow Java API for HugeRTE text editor. HugeRTE is an MIT-licensed fork of TinyMCE, the world's #1 JavaScript library for rich text editing, which switched from MIT to a GPL-or-pay license model in its latest version.

This add-on is a direct port of the [TinyMCE for Flow](https://vaadin.com/directory/component/tinymce-for-flow) add-on to use HugeRTE instead. Comments in the code may though refer to previous issues in the TinyMCE add-on, as the code is mostly a copy of it.

Works with binder as the component implements HasValue interfaces. The value is plain HTML. If you can't trust your clients, consider adding a converter that filters the input with e.g. JSOUP library.

Builds will be available from https://vaadin.com/directory 

## Development instructions

Check out the project to your IDE and run the Application class from src/test/java folder.

This opens up a Spring Boot based testing setup to http://localhost:9998

Some very basic tests are executed on `mvn install` using Playwright.

## Cutting a release

Before cutting a release, make sure the build passes properly locally and in GitHub Actions based verification build.

To tag a release and increment versions, issue:

    mvn release:prepare release:clean

Answer questions, defaults most often fine.
Note that `release:perform` is not needed as there is a GitHub Action is set up build and to push release to Maven Central automatically.

Directory will automatically pick up new releases within about half an hour, but if browser or Vaadin version support change, be sure to adjust the metadata in Vaadin Directory UI.
