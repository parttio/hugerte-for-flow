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

### Releasing under Windows 11

Windows 11 may have issues deploying a new release due to missing tools or unusual setup, that might work on
other OSes out of the box. 

(As a side note: this is more or less a log of my personal tragedy trying to set up my
machine to make it work. Some of this stuff may help, other not. If you find things, that work differently, feel
free to updated or extend the following steps. Stefan)

#### GPG
GPG is used to sign the package for maven central. By default, Windows has no utility installed for that.
1. Install gpg: https://gpg4win.org/
2. Open Kleopatra and create a new public-private-key-pair. 
3. Export that created key to the public key servers (context menu on the key).
4. Extend your maven settings.xml with a gpg profile. To do so copy the 16 digit hexadecimal "id" from the Kleopatra list entry
Then create such an entry in your settings:
       <profile>
            <id>gpg</id>
            <properties>
                <gpg.keyname>YOUR_16_DIGIT_KEY</gpg.keyname>
            </properties>
        </profile>

Alternatively it should also work to pass the key as a parameter to the mvn call later by using -Dgpg.keyname=YOUR_16_DIGIT_KEY

**Important**
At the moment, there are two release profiles. Ensure to always use the "release-gpg" one plus your gpg profile or -D parameter.

#### SSH
If not setup, you may need to setup the ssh agent and tell git to use Windows' open ssh. You need an ssh key pair in your
.ssh folder, for instance `id_rsa` or `id_ed25519`. 

1. Check if the ssh agent is running using the PowerShell as an admin `Get-Service ssh-agent`
2. If it shows "STOPPED", start it with `Start-Service ssh-agent` and set it to automatic startup with `Set-Service ssh-agent -StartupType Automatic`
3. Add your key via `ssh-add $env:USERPROFILE\.ssh\id_ed25519` (or whatever your key file name is)
4. Verify the successfull add via `ssh-add -l`
5. Open a new console and test `ssh -T git@github.com`. You should see a success message.
   
#### IDE
1. Add a new maven configuration to your IDE.
2. `mvn release:prepare -Prelease-gpg,gpg` or `mvn release:prepare -Prelease-gpg -Dgpg.keyname=YOUR_KEY`
3. Add an environment variable with the name `MAVEN_GPG_PASSPHRASE` and set your key password here - I trust you, that no one else can use this ;)
4. Add an environment variable to your system (or alternatively to your maven configuration - see next step): `GIT_SSH`
   that points to `C:\Windows\System32\OpenSSH\ssh.exe`
5. Run it (and pray, since it still is Windows).

If something fails, ensure to call `mvn release:rollback` before doing anything else.

