# Telepítő súgó

A FairEmail telepítése nagyon egyszerű. Hozzá kell adnia legalább egy fiókot, hogy fogadhasson, és egy identitást, hogy küldhessen emaileket. A gyors beállítás egyszerre hoz létre egy fiókot és egy identitást a legtöbb fő szolgáltató esetében.

## Követelmények

Internetcsatlakozás szükséges a fiókok és az identitások létrehozásához.

## Gyors beállítás

Csak írd be a neved, email címed és jelszavad és tapints a *Tovább* gombra.

Ez működni fog a legtöbb email szolgáltató esetében.

Ha a gyors telepítés nem működik, egy másik módon kell majd fiókot és identitást beállítanod, lásd lejjebb instrukciókért.

## Set up account - to receive email

To add an account, tap *Manage accounts* and tap the orange *add* button at the bottom. Select a provider from the list, enter the username, which is mostly your email address and enter your password. Tap *Check* to let FairEmail connect to the email server and fetch a list of system folders. After reviewing the system folder selection you can add the account by tapping *Save*.

If your provider is not in the list of providers, select *Custom*. Enter the domain name, for example *gmail.com* and tap *Get settings*. If your provider supports [auto-discovery](https://tools.ietf.org/html/rfc6186), FairEmail will fill in the hostname and port number, else check the setup instructions of your provider for the right IMAP hostname, port number and protocol (SSL/TLS or STARTTLS). For more about this, please see [here](https://github.com/M66B/FairEmail/blob/master/FAQ.md#authorizing-accounts).

## Set up identity - to send email

Similarly, to add an identity, tap *Manage identity* and tap the orange *add* button at the bottom. Enter the name you want to appear in the from address of the emails you send and select a linked account. Tap *Save* to add the identity.

If the account was configured manually, you likely need to configure the identity manually too. Enter the domain name, for example *gmail.com* and tap *Get settings*. If your provider supports [auto-discovery](https://tools.ietf.org/html/rfc6186), FairEmail will fill in the hostname and port number, else check the setup instructions of your provider for the right SMTP hostname, port number and protocol (SSL/TLS or STARTTLS).

Az aliasok használatáról lásd [ezt a GYIK-et](https://github.com/M66B/FairEmail/blob/master/FAQ.md#FAQ9).

## Grant permissions - to access contact information

If you want to lookup email addresses, have contact photos shown, etc, you'll need to grant read contacts permission to FairEmail. Just tap *Grant permissions* and select *Allow*.

## Setup battery optimizations - to continuously receive emails

On recent Android versions, Android will put apps to sleep when the screen is off for some time to reduce battery usage. If you want to receive new emails without delays, you should disable battery optimizations for FairEmail. Tap *Disable battery optimizations* and follow the instructions.

## Kérdések és problémák

Ha kérdésed vagy problémád van, lásd [ezt az oldalt](https://github.com/M66B/FairEmail/blob/master/FAQ.md) segítségért.