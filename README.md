<img src="https://github.com/M66B/FairEmail/raw/master/images/banner_crying6.png" />

<p align="center">
	<a href="#downloads">Downloads</a> &bull;
	<a href="#privacy">Privacy</a> &bull;
	<a href="#support">Support</a> &bull;
	<a href="#license">License</a>
</p>

<img align="right" src="https://raw.githubusercontent.com/M66B/FairEmail/master/app/src/main/res/mipmap-hdpi/ic_launcher.png">

# FairEmail (Fork)

![GitHub](https://img.shields.io/github/license/M66B/FairEmail.svg)
![GitHub release](https://img.shields.io/github/release/M66B/FairEmail.svg)
![GitHub commits since tagged version](https://img.shields.io/github/commits-since/M66B/FairEmail/0.1.svg)

*Fully featured, open source, privacy oriented email app for Android*

FairEmail is easy to setup and works with virtually all email providers, including Gmail, Outlook and Yahoo!

FairEmail might be for you if you value your privacy.

*FairEmail does not support non-standard protocols, like Microsoft Exchange Web Services and Microsoft ActiveSync.*

***

## What I changed in this fork

* Signal color for dark theme
* For zoom = 0 font size of sender and subject is the same, for 1 and 2 font size of subject ist smaller then font size of sender
* In detail view
    - date and size is on one line
    - color of date, size and subject in header detail info is colorUnread
    - font size of subject in header block is one step larger
    - When opening an email the subject in the email list element is hidden
    - date, size and subject will be always shown in header detail info, even when choosing the option to hide the header detail infos on opening the email 

***

## Main features

* Fully featured
* 100 % [open source](https://github.com/M66B/FairEmail/blob/master/LICENSE)
* [Privacy oriented](https://github.com/M66B/FairEmail/blob/master/PRIVACY.md)
* Unlimited accounts
* Unlimited email addresses
* Unified inbox
* [Conversation threading](https://en.wikipedia.org/wiki/Conversation_threading)
* Two way synchronization
* Offline storage and operations
* Battery friendly
* Low data usage
* Small (~ 12 MB)
* Material design (including dark/black theme)
* Maintained and supported

This app is deliberately minimalistic by design, so you can concentrate on reading and writing messages.

This app starts a foreground service with a low priority status bar notification to make sure you'll never miss new emails.

## Privacy features

* Encryption/decryption supported ([OpenPGP](https://www.openpgp.org/) and [S/MIME](https://en.wikipedia.org/wiki/S/MIME))
* Reformat messages to prevent [phishing](https://en.wikipedia.org/wiki/Phishing)
* Confirm showing images to prevent tracking
* Confirm opening links to prevent tracking and phishing
* Automatically recognize and disable tracking images
* Warning if messages could not be [authenticated](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq92)

## Simple

* Quick setup
* Easy navigation
* No bells and whistles
* No distracting "eye candy"

## Secure

* No data storage on third party servers
* Using open standards (IMAP, SMTP, OpenPGP, S/MIME, etc)
* Safe message view (styling, scripting and unsafe HTML removed)
* Confirm opening links, images and attachments
* No special permissions required
* No advertisements
* No analytics and no tracking ([error reporting](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq104) is opt-in)
* No [Google backup](https://developer.android.com/guide/topics/data/backup)
* FairEmail is an original work, not a fork or a clone

## Efficient

* Fast and lightweight
* [IMAP IDLE](https://en.wikipedia.org/wiki/IMAP_IDLE) (push messages) supported
* Built with latest development tools and libraries

## Pro features

All pro features are convenience or advanced features.

* Account/identity colors
* Colored stars ([instructions](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq107))
* Notification settings (sounds) per account/folder/sender (requires Android 8 Oreo)
* Configurable notification actions
* Snooze messages ([instructions](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq67))
* Send messages after selected time
* Synchronization scheduling ([instructions](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq78))
* Reply templates
* Accept/decline calendar invitations
* Filter rules ([instructions](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq71))
* Search on server ([instructions](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq13))
* Keyword management
* S/MIME sign/encrypt
* Biometric authentication ([instructions](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq113))
* Unified inbox widget
* Export settings

## Screenshots

Please see [here](https://email.faircode.eu/#screenshots) for screenshots.

## Downloads

* [GitHub](https://github.com/M66B/FairEmail/releases)
* [Play store](https://play.google.com/store/apps/details?id=eu.faircode.email)
* [Play store](https://play.google.com/apps/testing/eu.faircode.email) (test)

To download a GitHub release you might need to expand the assets section to download the [APK file](https://en.wikipedia.org/wiki/Android_application_package).

The GitHub version is being updated more often than the Play store version.
The GitHub release will automatically check for updates on GitHub.
You can turn this off in the miscellaneous settings.

Certificate fingerprints:

```
MD5: 64:90:8E:2C:0D:25:29:B0:D0:26:2D:24:D8:BB:66:56
SHA1: 17:BA:15:C1:AF:55:D9:25:F9:8B:99:CE:A4:37:5D:4C:DF:4C:17:4B
SHA256: E0:20:67:24:9F:5A:35:0E:0E:C7:03:FE:9D:F4:DD:68:2E:02:91:A0:9F:0C:2E:04:10:50:BB:E7:C0:64:F5:C9
```

One line command to display certificate fingerprints:

```unzip -p fairemail.apk META-INF/CERT.RSA | keytool -printcert```

I do not hand over the signing keys of my apps to Google.

* [F-Droid](https://f-droid.org/en/packages/eu.faircode.email/) ([last build status](https://f-droid.org/wiki/page/eu.faircode.email/lastbuild))

Note that F-Droid builds new versions irregularly and you'll need the F-Droid client to get update notifications.
To get updates in a timely fashion you are advised to use the GitHub release.

Because F-Droid builds and GitHub releases are signed differently, an F-Droid build needs to be uninstalled first to be able to update to a GitHub release.

## Privacy

Please see [here](https://github.com/M66B/FairEmail/blob/master/PRIVACY.md#fairemail) for the privacy policy.

## Compatibility

FairEmail requires at least Android 5 Lollipop.
Individual message notifications are available on Android 7 Nougat and later only
because earlier Android versions do not support notification grouping.

FairEmail will work properly on devices without any Google service installed.

See [here](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-known-problems) for known problems.

## Support

See [here](https://github.com/M66B/FairEmail/blob/master/FAQ.md) for a list of often asked questions and about how to get support.

## Contributing

*Documentation*

Contributions to this document and the frequently asked questions
are preferred in the form of [pull requests](https://help.github.com/articles/creating-a-pull-request/).

*Translations*

* You can translate the in-app texts of FairEmail [on Crowdin](https://crowdin.com/project/open-source-email)
* If your language is not listed, please send a message through [this contact form](https://contact.faircode.eu/?product=other)

*Source code*

Building FairEmail from source code is straightforward with [Android Studio](http://developer.android.com/sdk/).
It is expected that you can solve build problems yourself, so there is no support on building.

Source code contributions are preferred in the form of [pull requests](https://help.github.com/articles/creating-a-pull-request/).
Please [contact me](https://contact.faircode.eu/?product=other) first to tell me what your plans are.

Please note that by contributing you agree to the license below, including the copyright, without any additional terms or conditions.

## Attribution

See [here](https://github.com/M66B/FairEmail/blob/master/ATTRIBUTION.md) for a list of used libraries and associated license information.

Error reporting is sponsored by:

![Bugsnag Logo](/images/bugsnag_logo_navy.png)

[Bugsnag](https://www.bugsnag.com/) monitors application stability
and is used to [help improve FairEmail](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-faq104).

## License

Copyright &copy; 2018-2019 Marcel Bokhorst. All rights reserved.

[GNU General Public License version 3](https://www.gnu.org/licenses/gpl.txt)

> FairEmail is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

> FairEmail is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
