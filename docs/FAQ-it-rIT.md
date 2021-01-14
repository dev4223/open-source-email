# Supporto FairEmail

Se hai una domanda, controlla prima le domande frequenti qui sotto. In fondo puoi trovare come porre altre domande, richiedere funzionalità e segnalare errori.

## Indice

* [Autorizzare account](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-authorizing-accounts)
* [Come fare per ...?](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-howto)
* [Problemi noti](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-known-problems)
* [Funzionalità pianificate](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-planned-features)
* [Funzionalità richieste di frequente](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-frequently-requested-features)
* [Domande frequenti](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-frequently-asked-questions)
* [Ricevi supporto](https://github.com/M66B/FairEmail/blob/master/FAQ.md#user-content-get-support)

## Autorizzare account

Nella maggior parte dei casi, la configurazione rapida sarà in grado di identificare automaticamente la configurazione giusta.

Se la configurazione rapida fallisce, dovrai configurare manualmente un account (per ricevere email) e un'identità (per inviare email). Per farlo avrai bisogno degli indirizzi dei server IMAP e SMTP e dei numeri delle porte, di sapere se SSL/TLS o STARTTLS devono essere usati e conoscere il tuo nome utente (solitamente, ma non sempre, il tuo indirizzo email) e la password.

Cercare *IMAP* e il nome del fornitore di solito è sufficiente per trovare la documentazione giusta.

In alcuni casi, dovrai abilitare l'accesso esterno al tuo account e/o utilizzare una password speciale (app), per esempio quando l'autenticazione a due fattori è attiva.

Per autorizzare:

* Gmail / G suite, vedi la [domanda 6](#user-content-faq6)
* Outlook / Live / Hotmail, vedi la [domanda 14](#user-content-faq14)
* Office 365, vedi [la domanda 14](#user-content-faq156)
* Microsoft Exchange, vedi [la domanda 8](#user-content-faq8)
* Yahoo, AOL e Sky, vedi la [domanda 88](#user-content-faq88)
* Apple iCloud, vedi la [domanda 148](#user-content-faq148)
* Free.fr, vedi la [domanda 157](#user-content-faq157)

Si prega di vedere [qui](#user-content-faq22) per messaggi di errore e soluzioni comuni.

Domande correlate:

* [OAuth è supportato?](#user-content-faq111)
* [Perché ActiveSync non è supportato?](#user-content-faq133)

<a name="howto">

## Come fare per ...?

* Cambiare il nome dell'account: Impostazioni, passo 1, Gestisci, tocca l'account
* Cambiare l'azione di scorrimento a sinistra/destra: Impostazioni, Comportamento, Imposta azioni di scorrimento
* Cambiare password: Impostazioni, passo 1, Gestisci, tocca l'account, cambia la password
* Impostare una firma: Impostazioni, passo 2, Gestisci, tocca l'identità, Modifica firma.
* Aggiungere indirizzi CC e CCN: tocca l'icona persone alla fine dell'oggetto dell'email
* Andare al messaggio successivo/precedente all'archiviazione/eliminazione: nelle impostazioni di comportamento disattiva *Chiudi automaticamente le conversazioni* e seleziona *Vai alla conversazione precedente/successiva* sotto *Alla chiusura di una conversazione*
* Aggiungere una cartella nella casella di posta unificata: premi a lungo la cartella nell'elenco delle cartelle e spunta *Mostra nella casella di posta unificata*
* Aggiungere una cartella al menu di navigazione: premi a lungo la cartella nell'elenco delle cartelle e spunta *Mostra nel menu di navigazione*
* Caricare più messaggi: premi a lungo una cartella nell'elenco delle cartelle, seleziona *Sincronizza più messaggi*
* Eliminare un messaggio, evitando il cestino: nel menu a 3 punti appena sopra il testo del messaggio *Elimina* o, in alternativa, deseleziona la cartella cestino nelle impostazioni dell'account
* Eliminare un account/identità: Impostazioni, passi 1/2, Gestisci, tocca account/identità, menu a tre punti, Elimina
* Eliminare una cartella: premi a lungo la cartella nell'elenco delle cartelle, Modifica le proprietà, menu a tre punti, Elimina
* Annullare l'invio: posta In uscita, tocca il messaggio, tocca l'icona Annulla
* Conservare i messaggi inviati nella posta in arrivo: [vedi queste domande frequenti](#user-content-faq142)
* Cambiare cartelle di sistema: Impostazioni, passo 1, Gestisci, tocca l'account, in fondo
* Impostazioni di esportazione/importazione: Impostazioni, menu di navigazione/menu hamburger

## Problemi noti

* ~~Un [errore in Android 5.1 e 6](https://issuetracker.google.com/issues/37054851) causa la visualizzazione di un formato sbagliato delle ore. Attivando/disattivando l'impostazione di Android *Usa il formato 24 ore* potrebbe risolvere temporaneamente il problema. È stato aggiunto un espediente.~~
* ~~Un [errore in Google Drive](https://issuetracker.google.com/issues/126362828) fa sì che i file esportati su Google Drive siano vuoti. Google lo ha corretto.~~
* ~~Un [errore in AndroidX](https://issuetracker.google.com/issues/78495471) provoca un crash occasionale di FairEmail durante la pressione a lungo o lo scorrimento. Google lo ha corretto.~~
* ~~Un [errore in AndroidX ROOM](https://issuetracker.google.com/issues/138441698) causa a volte un crash con "*... Eccezione durante il calcolo dei dati live del database ... Impossibile leggere la riga ...*". È stato aggiunto un espediente.~~
* Un [errore in Android](https://issuetracker.google.com/issues/119872129) causa un crash di FairEmail con "*... Notifica errata postata ...*" su alcuni dispositivi dopo l'aggiornamento di FairEmail e toccando su una notifica.
* Un [errore in Android](https://issuetracker.google.com/issues/62427912) a volte provoca un crash con "*... ActivityRecord non trovato per ...*" dopo l'aggiornamento FairEmail. La reinstallazione ([source](https://stackoverflow.com/questions/46309428/android-activitythread-reportsizeconfigurations-causes-app-to-freeze-with-black)) potrebbe risolvere il problema.
* Un [errore in Android](https://issuetracker.google.com/issues/37018931) a volte provoca un crash con *... InputChannel non è inizializzato ...* su alcuni dispositivi.
* ~~Un [errore in LineageOS](https://review.lineageos.org/c/LineageOS/android_frameworks_base/+/265273) a volte provoca un crash con *... java.lang.ArrayIndexOutOfBoundsException: length=...; index=... ...*.~~
* Un errore in Nova Launcher su Android 5.x causa il crash di FairEmail con un *java.lang.StackOverflowError* quando Nova Launcher ha accesso al servizio di accessibilità.
* ~~Il selettore di cartelle a volte non mostra cartelle per motivi ancora sconosciuti. Sembra essere risolto.~~
* ~~Un [errore in AndroidX](https://issuetracker.google.com/issues/64729576) rende difficile toccare lo scroller rapido. È stato aggiunto un espediente.~~
* ~~La crittografia con YubiKey risulta in un ciclo infinito. Ciò sembra essere causato da un [errore in OpenKeychain](https://github.com/open-keychain/open-keychain/issues/2507).~~
* Lo scorrimento verso una posizione collegata internamente nei messaggi originali non funziona. Ciò non può essere risolto perché la vista originale dei messaggi è contenuta in una vista a scorrimento.
* Un'anteprima di un testo di un messaggio non (sempre) appare negli orologi Samsung perché [setLocalOnly](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder.html#setLocalOnly(boolean)) sembra venga ignorato. I testi di anteprima del messaggio sono noti per essere visualizzati correttamente sugli indossabili Pebble 2, Fitbit Charge 3, Mi band 3 e Xiaomi Amazfit BIP. Vedi anche [queste domande frequenti](#user-content-faq126).
* Un [errore in Android](https://issuetracker.google.com/issues/37068143) a volte provoca un crash con *... Spostamento non valido: ... L'intervallo valido è ... quando il testo è selezionato e tocca all'esterno del testo selezionato. Questo bug è stato risolto in Android 6.0.1.</li>
* I collegamenti interni (ancoraggi) non funzioneranno perché i messaggi originali sono mostrati in una WebView incorporata in una vista a scorrimento (l'elenco delle conversazioni). Questa è una limitazione di Android che non può essere fissata o lavorata.
* Il rilevamento della lingua [non funziona più](https://issuetracker.google.com/issues/173337263) su dispositivi Pixel con (aggiornato a?) Android 11</ul>

## Funzionalità pianificate

* ~~Sicronizzazione su richiesta (manuale)~~
* ~~Cifratura semi-automatica~~
* ~~Copia messaggio~~
* ~~Stelle colorate~~
* ~~Impostazioni di notifica per cartella~~
* ~~Selezione immagini locali per le firme~~ (non verrà aggiunto perchè richiede la gestione dei file e quasi tutti i client email non mostrano le immagini per impostazione predefinita)
* ~~Mostra messaggi che soddisfano un criterio~~
* ~~[ManageSieve](https://tools.ietf.org/html/rfc5804)~~ (non ci sono librerie Java mantenute con licenza adeguata e senza dipendenze, inoltre FairEmail ha le proprie regole di filtraggio)
* ~~Cerca messaggi con/senza allegati~~ (non può essere implementata perchè IMAP non supporta la ricerca degli allegati)
* ~~Cerca una cartella~~ (il filtro su cartelle gerarchiche è problematico)
* ~~Suggerimenti di ricerca~~
* ~~[Messaggio di configurazione Autocrypt](https://autocrypt.org/autocrypt-spec-1.0.0.pdf) (section 4.4)~~ (Secondo me non è una buona idea permette ad un client di posta elettronica di gestire le chiavi di crittografia solo per casi d'uso rari se OpenKeychain può esportare anche le chiavi)
* ~~Cartelle unificate generiche~~
* ~~Programmazione notifiche nuovo messaggio per account~~ (implementato aggiungendo regole di orario in modo che i messaggi possano essere silenziati durante periodi scelti)
* ~~Copiare account e identità~~
* ~~Zoom con selezione~~ (non è possibile in modo affidabile in una lista scorrevole; si può invece ingrandire l'intero messaggio)
* ~~Visualizzazione delle cartelle più compatta~~
* ~~Creare liste e tabelle~~ (richiede un rich text editor, vedi [questa FAQ](#user-content-faq99))
* ~~Pizzica ingrandimento della dimensione del testo~~
* ~~Mostra GIF~~
* ~~Temi~~ (un tema grigio chiaro e scuro è stato aggiunto perché questo è ciò che gran parte delle persone sembrano di volere)
* ~~Ogni condizione dell'ora del giorno~~ (ogni giorno non si adatta davvero nella condizione da/alla data/orario)
* ~~Invia come allegato~~
* ~~Widget per il profilo selezionato~~
* ~~Ricorda di allegare i file~~
* ~~Seleziona i domini per cui mostrare le immagini~~ (questo sarà troppo complicato da usare)
* ~~Visualizzazione messaggi preferiti unificati~~ (c'è già una ricerca speciale per questo)
* ~~Sposta azione di notifica~~
* ~~Supporto S/MIME~~
* ~~Cerca impostazioni~~

Qualsiasi cosa su questa lista è in ordine casuale e *potrebbe* essere aggiunto nel vicino futuro.

## Funzionalità richieste di frequente

Il design è basato su molte discussioni e se vuoi puoi discuterne anche tu [in questo forum](https://forum.xda-developers.com/android/apps-games/source-email-t3824168). L'obiettivo del design è quello di essere minimalista (senza menu inutili, pulsanti, ecc.) e di non distrarre (senza colori fantasiosi, animazioni, ecc). Tutte le cose visualizzate devono essere utili in un modo o nell'altro e devono essere posizionate con attenzione per un facile utilizzo. Caratteri, dimensioni, colori, ecc devono essere in material design quando possibile.

## Domande frequenti

* [(1) Quali autorizzazioni sono necessarie e perché?](#user-content-faq1)
* [(2) Perché viene mostrata una notifica permanente?](#user-content-faq2)
* [(3) Cosa sono le operazioni e perché sono in sospeso?](#user-content-faq3)
* [(4) Come posso usare un certificato di sicurezza non valido / password vuota / connessione di testo semplice?](#user-content-faq4)
* [(5) Come posso personalizzare la visualizzazione dei messaggi?](#user-content-faq5)
* [(6) Come posso accedere a Gmail / G suite?](#user-content-faq6)
* [(7) Perché i messaggi inviati non appaiono (direttamente) nella cartella inviata?](#user-content-faq7)
* [(8) Posso usare un account Microsoft Exchange?](#user-content-faq8)
* [(9) Cosa sono le identità / come faccio ad aggiungere un alias?](#user-content-faq9)
* [~~(11) Perché POP non è supportato?~~](#user-content-faq11)
* [~~(10) Cosa significa 'UIDPLUS non supportato'?~~](#user-content-faq10)
* [(12) Come funziona la crittografia/decifrazione?](#user-content-faq12)
* [(13) Come funziona la ricerca su dispositivo/server?](#user-content-faq13)
* [(14) Come posso impostare un account Outlook / Live / Hotmail?](#user-content-faq14)
* [(15) Perché il testo del messaggio continua a caricare?](#user-content-faq15)
* [(16) Perché i messaggi non vengono sincronizzati?](#user-content-faq16)
* [~~(17) Perché la sincronizzazione manuale non funziona?~~](#user-content-faq17)
* [(18) Perché l'anteprima del messaggio non viene sempre mostrata?](#user-content-faq18)
* [(19) Perché le funzionalità pro sono così costose?](#user-content-faq19)
* [(20) Posso ottenere un rimborso?](#user-content-faq20)
* [(21) Come abilitare la luce delle notifiche?](#user-content-faq21)
* [(22) Cosa significa errore dell' account/cartella ... ?](#user-content-faq22)
* [(23) Perché mi arriva una segnalazione.. ?](#user-content-faq23)
* [(24) Cos'è la navigazione dei messaggi sul server?](#user-content-faq24)
* [(25) Perché non posso selezionare/aprire/salvare un'immagine, allegato o file?](#user-content-faq25)
* [(26) Posso aiutarvi a tradurre FairEmail nella mia lingua?](#user-content-faq26)
* [(27) Come posso distinguere le immagini integrate da quelle esterne?](#user-content-faq27)
* [(28) Come posso gestire le notifiche della barra di stato?](#user-content-faq28)
* [(29) Come posso ricevere notifiche di messaggi per altre cartelle?](#user-content-faq29)
* [(30) Come posso usare le impostazioni rapide previste?](#user-content-faq30)
* [(31) Come posso usare i collegamenti rapidi previsti?](#user-content-faq31)
* [(32) Come posso controllare se leggere un'email è davvero sicuro?](#user-content-faq32)
* [(33) Perché gli indirizzi modificati dei mittenti non funzionano?](#user-content-faq33)
* [(34) Come vengono abbinate le identità?](#user-content-faq34)
* [(35) Perché dovrei fare attenzione alla visualizzazione di immagini, allegati, il messaggio originale e ad aprire collegamenti?](#user-content-faq35)
* [(36) Come sono crittografati i file delle impostazioni?](#user-content-faq36)
* [(37) Come vengono memorizzate le password?](#user-content-faq37)
* [(39) Come posso ridurre l'uso della batteria di FairEmail?](#user-content-faq39)
* [(40) Come posso ridurre l'uso dei dati di FairEmail?](#user-content-faq40)
* [(41) Come posso correggere l'errore 'Handshake non riuscito'?](#user-content-faq41)
* [(42) È possibile aggiungere un nuovo provider all'elenco dei provider?](#user-content-faq42)
* [(43) Riesci a mostrare l'originale ... ?](#user-content-faq43)
* [(44) È possibile mostrare le foto dei contatti/ identicon nella cartella 'messaggi inviati'?](#user-content-faq44)
* [(45) Come posso risolvere 'Questa chiave non è disponibile. Per usarla, devi importarla come una delle tue!' ?](#user-content-faq45)
* [(46) Perché la lista dei messaggi continua ad aggiornarsi?](#user-content-faq46)
* [(47) Come risolvo l'errore 'Nessun account primario o nessuna cartella bozze'?](#user-content-faq47)
* [~~(48) Come posso risolvere l'errore 'Nessun account primario o nessuna cartella di archivio' ?~~](#user-content-faq48)
* [(49) Come faccio a risolvere 'Un'app obsoleta ha inviato un percorso di file invece di un flusso di file'?](#user-content-faq49)
* [(50) È possibile aggiungere un'opzione per sincronizzare tutti i messaggi?](#user-content-faq50)
* [(51) Come vengono ordinate le cartelle?](#user-content-faq51)
* [(52) Perché ci vuole tempo per riconnettersi a un account?](#user-content-faq52)
* [(53) Si può attaccare la barra d'azione del messaggio in alto/in basso?](#user-content-faq53)
* [~~(54) Come uso un prefisso dello spazio del nome?~~](#user-content-faq54)
* [(55) Come posso contrassegnare tutti i messaggi come letti / spostati o eliminare tutti i messaggi?](#user-content-faq55)
* [(56) Puoi aggiungere il supporto per JMAP?](#user-content-faq56)
* [(57) Posso usare HTML nelle firme?](#user-content-faq57)
* [(58) Cosa significa un'icona dell'email aperta/chiusa?](#user-content-faq58)
* [(59) I messaggi originali possono essere aperti nel browser?](#user-content-faq59)
* [(60) Lo sapevi ...?](#user-content-faq60)
* [(61) Perché alcuni messaggi sono mostrati oscurati?](#user-content-faq61)
* [(62) Quali metodi di autenticazione sono supportati?](#user-content-faq62)
* [(63) Come sono ridimensionate le immagini per la visualizzazione su schermi?](#user-content-faq63)
* [~~(64) Puoi aggiungere azioni personalizzate per lo scorrimento a sinistra/destra?~~](#user-content-faq64)
* [(65) Perché alcuni allegati sono mostrati oscurati?](#user-content-faq65)
* [(66) FairEmail è disponibile nella Libreria di Google Play Family?](#user-content-faq66)
* [(67) Come posso posticipare le conversazioni?](#user-content-faq67)
* [~~(68) Perché Adobe Acrobat reader non apre gli allegati PDF / le app di Microsoft non aprono i documenti allegati?~~](#user-content-faq68)
* [(69) Puoi aggiungere lo scorrimento in su su un messaggio nuovo?](#user-content-faq69)
* [(70) Quando saranno auto-espansi i messaggi?](#user-content-faq70)
* [(71) Come uso le regole del filtro?](#user-content-faq71)
* [(72) Cosa sono le identità/i profili principali?](#user-content-faq72)
* [(73) È sicuro/efficiente spostare i messaggi attraverso i profili?](#user-content-faq73)
* [(74) Perché vedo messaggi duplicati?](#user-content-faq74)
* [(75) Puoi creare una versione iOS, Windows, Linux, etc?](#user-content-faq75)
* [(76) Cosa fa 'Cancella messaggi locali'?](#user-content-faq76)
* [(77) Perché a volte i messaggi sono mostrati con un piccolo ritardo?](#user-content-faq77)
* [(78) Come uso i programmi?](#user-content-faq78)
* [(79) Come uso la sincronizzazione su richiesta (manuale)?](#user-content-faq79)
* [~~(80) Come risolvo l'errore 'Impossibile caricare BODYSTRUCTURE'?~~](#user-content-faq80)
* [~~(81) Puoi rendere scuro lo sfondo del messaggio originale nel tema scuro?~~](#user-content-faq81)
* [(82) Cos'è un'immagine di tracciamento?](#user-content-faq82)
* [(84) A che servono i contatti locali?](#user-content-faq84)
* [(85) Perché un'identità non è disponibile?](#user-content-faq85)
* [~~(86) Cosa sono le 'caratteristiche di privacy extra'?~~](#user-content-faq86)
* [(87) Cosa significa 'credenziali non valide'?](#user-content-faq87)
* [(88) Come posso usare un account Yahoo, AOL o Sky?](#user-content-faq88)
* [(89) Come invio messaggi di solo testo semplice?](#user-content-faq89)
* [(90) Perché alcuni testi sono collegati senza essere un collegamento?](#user-content-faq90)
* [~~(91) Puoi aggiungere la sincronizzazione periodica per risparmiare energia della batteria?~~](#user-content-faq91)
* [(92) Puoi aggiungere il filtro dello spam, la verifica della firma DKIM e l'autorizzazione SPF?](#user-content-faq92)
* [(93) Puoi consentire l'installazione/archiviazione dei dati su supporti di archiviazione esterna (sdcard)?](#user-content-faq93)
* [(94) Cosa significa la striscia rossa/arancione alla fine dell'intestazione?](#user-content-faq94)
* [(95) Perché non tutte le app sono mostrate durante la selezione di un allegato o immagine?](#user-content-faq95)
* [(96) Dove posso trovare le impostazioni IMAP e SMTP?](#user-content-faq96)
* [(97) Cos'è 'pulizia'?](#user-content-faq97)
* [(98) Perché posso ancora scegliere i contatti dopo aver revocato i permessi dei contatti?](#user-content-faq98)
* [(99) Puoi aggiungere un editor ricco di testo o markdown?](#user-content-faq99)
* [(100) Come posso sincronizzare le categorie di Gmail?](#user-content-faq100)
* [(101) Cosa significa il punto blu/arancione in fondo alle conversazioni?](#user-content-faq101)
* [(102) Come posso abilitare la rotazione automatica delle immagini?](#user-content-faq102)
* [(103) Come registro l'audio?](#user-content-faq158)
* [(104) Cosa devo sapere sulla segnalazione degli errori?](#user-content-faq104)
* [(105) Come funziona l'opzione roam-like-at-home?](#user-content-faq105)
* [(106) Quali launcher possono mostrare un distintivo di conteggio con il numero di messaggi non letti?](#user-content-faq106)
* [(107) Come uso le stelle colorate?](#user-content-faq107)
* [~~(108) Puoi aggiungere permanentemente l'eliminazione dei messaggi da ogni cartella?~~](#user-content-faq108)
* [~~(109) Perché 'seleziona profilo' è disponibile solo nelle versioni ufficiali?~~](#user-content-faq109)
* [(110) Perché (alcuni) messaggi vuoti e/o allegati sono corrotti?](#user-content-faq110)
* [(111) OAuth è supportato?](#user-content-faq111)
* [(112) Quale provider di posta elettronica consigliate?](#user-content-faq112)
* [(113) Come funziona l'autenticazione biometrica?](#user-content-faq113)
* [(114) Puoi aggiungere un'importazione per le impostazioni di altre app d'email?](#user-content-faq114)
* [(115) Puoi aggiungere i chip dell'indirizzo e-mail?](#user-content-faq115)
* [~~(116) Come posso mostrare le immagini nei messaggi da mittenti fidati di default?~~](#user-content-faq116)
* [(117) Puoi aiutarmi a ripristinare il mio acquisto?](#user-content-faq117)
* [(118) Cosa fa 'Rimuovi parametri di tracciamento' esattamente?](#user-content-faq118)
* [~~(119) Puoi aggiungere i colori al widget di posta in arrivo unificato?~~](#user-content-faq119)
* [(120) Perché le notifiche dei nuovi messaggi non sono rimosse all'apertura dell'app?](#user-content-faq120)
* [(121) Come sono raggruppati i messaggi in una conversazione?](#user-content-faq121)
* [~~(122) Perché il nome/indirizzo email del destinatario viene mostrato come un colore d'avviso?~~](#user-content-faq122)
* [(123) Cosa succederà quando FairEmail non potrà connettersi ad un server d'email?](#user-content-faq123)
* [(124) Perché ricevo 'Messaggio troppo grande o troppo complesso da mostrare'?](#user-content-faq124)
* [(125) Quali sono le caratteristiche sperimentali correnti?](#user-content-faq125)
* [(126) Possono le anteprime del messaggio essere inviate al mio indossabile?](#user-content-faq126)
* [(127) Come posso risolvere 'Argomento/i di HELO Sintatticamente non validi'? ](#user-content-faq127)
* [(128) Come posso reimpostare le domande chieste, ad esempio per mostrare le immagini?](#user-content-faq128)
* [(129) Sono ProtonMail e Tutanota supportati?](#user-content-faq129)
* [(130) Cosa significa messaggio d'errore ?](#user-content-faq130)
* [(131) Puoi modificare la direzione per lo scorrimento al messaggio precedente/successivo?](#user-content-faq131)
* [(132) Perché le notifiche dei nuovi messaggi sono silenziate?](#user-content-faq132)
* [(133) Perché ActiveSync non è supportato?](#user-content-faq133)
* [(134) Puoi aggiungere l'eliminazione dei messaggi locali?](#user-content-faq134)
* [(135) Perché i messaggi cestinati e le bozze sono mostrati nelle conversazioni?](#user-content-faq135)
* [(135) Come posso eliminare un profilo/un'identità/una cartella?](#user-content-faq136)
* [(137) Come posso reimpostare 'Non chiedere più'?](#user-content-faq137)
* [(138) Puoi aggiungere la gestione/sincronizzazione del calendario/contatto?](#user-content-faq138)
* [(139) Come risolvo 'L'utente è autenticato ma non connesso'?](#user-content-faq139)
* [(140) Perché il testo del massaggio contiene caratteri strani?](#user-content-faq140)
* [(141) Come posso correggere 'Una cartella delle bozze è necessaria per inviare i messaggi'?](#user-content-faq141)
* [(142) Come posso memorizzare i messaggi inviati nella posta in arrivo?](#user-content-faq142)
* [~~(143) Puoi aggiungere una cartella del cestino per i profili POP3?~~](#user-content-faq143)
* [(144) Come posso registrare le note vocali?](#user-content-faq144)
* [(145) Come posso impostare un suono di notifica per un profilo, una cartella o un mittente?](#user-content-faq145)
* [(146) Come posso risolvere gli orari non corretti dei messaggi?](#user-content-faq146)
* [(147) Cosa dovrei sapere delle versioni di terze parti?](#user-content-faq147)
* [(148) Come posso usare un profilo di Apple iCloud?](#user-content-faq148)
* [(149) Come funziona il widget di conteggio dei messaggi non letti?](#user-content-faq149)
* [(150) Puoi aggiungere l'annullamento degli inviti al calendario?](#user-content-faq150)
* [(151) Puoi aggiungere il backup/ripristino dei messaggi?](#user-content-faq151)
* [(152) Come posso inserire un gruppo di contatto?](#user-content-faq152)
* [(153) Perché non funziona l'eliminazione permanente dei messaggi di Gmail?](#user-content-faq153)
* [~~(154) Puoi aggiungere i favicon come foto di contatto?~~](#user-content-faq154)
* [(155) Cos'è un file winmail.dat?](#user-content-faq155)
* [(156) Come posso configurare un profilo di Office 365?](#user-content-faq156)
* [(157) Come posso configurare un profilo di Free.fr?](#user-content-faq157)
* [(158) Quale registratore audio / videocamera consigliate?](#user-content-faq158)
* [(159) Cosa sono gli elenchi di protezione del monitoraggio di Disconnect?](#user-content-faq159)
* [(160) Puoi aggiungere l'eliminazione permanente dei messaggi senza conferma?](#user-content-faq160)
* [(161) Puoi aggiungere un'impostazione per modificare il colore primario e di cadenza?](#user-content-faq161)
* [(162) IMAP NOTIFY è supportato?](#user-content-faq162)
* [(163) Cos'è la classificazione dei messaggi?](#user-content-faq163)

[Ho un'altra domanda.](#user-content-support)

<a name="faq1"></a>
**(1) Quali permessi sono necessari e perché?**

I permessi di Android seguenti sono necessari:

* *avere accesso completo alla rete* (INTERNET): per inviare e ricevere le email
* *visualizza le connessioni di rete* (ACCESS_NETWORK_STATE): per monitorare le modifiche alla connettività di internet
* *esegui all'avvio* (RECEIVE_BOOT_COMPLETED): per avviare il monitoraggio all'avvio del dispositivo
* *servizio in primo piano* (FOREGROUND_SERVICE): per eseguire un servizio in primo piano su Android 9 Pie e successive, vedi anche la prossima domanda
* *prevenire che il dispositivo vada in riposo* (WAKE_LOCK): per mantenere attivo il dispositivo durante la sincronizzazione dei messaggi
* *fatturazione in-app* (BILLING): per consentire gli acquisti in-app
* Opzionale: *leggi i tuoi contatti* (READ_CONTACTS): per completare automaticamente gli indirizzi e mostrare le foto
* Opzionale: *leggere i contenuti della tua scheda SD* (READ_EXTERNAL_STORAGE): per accettare i file da altre app obsolete, vedi anche [questa FAQ](#user-content-faq49)
* Opzionale: *usa l'hardware delle impronte digitali* (USE_FINGERPRINT) e usa *l'hardware biometrico* (USE_BIOMETRIC): per usare l'autenticazione biometrica
* Opzionale: *trova profili sul dispositivo* (GET_ACCOUNTS): per selezionare un profilo quando si utilizza la configurazione rapida di Gmail
* Android 5.1 e precedenti: *usa profili sul dispositivo* (USE_CREDENTIALS): per selezionare un profilo quando usi la configurazione rapida di Gmail (non necessario sulle versioni di Android successive)
* Android 5.1 Lollipop e precedenti: *Leggi profilo* (READ_PROFILE): per leggere il tuo nome quando usi la configurazione rapida di Gmail (non necessario sulle versioni di Android successive)

[I permessi opzionali](https://developer.android.com/training/permissions/requesting) sono supportati solo su Android 6 Marshmallow e successive. Sulle versioni precedenti di Android ti sarà chiesto di garantire i permessi opzionali per l'installazione di FairEmail.

I permessi seguenti sono necessari per mostrare il conteggio dei messaggi non letti come un distintivo (vedi anche [questa FAQ](#user-content-faq106)):

* *com.sec.android.provider.badge.permission.READ*
* *com.sec.android.provider.badge.permission.WRITE*
* *com.htc.launcher.permission.READ_SETTINGS*
* *com.htc.launcher.permission.UPDATE_SHORTCUT*
* *com.sonyericsson.home.permission.BROADCAST_BADGE*
* *com.sonymobile.home.permission.PROVIDER_INSERT_BADGE*
* *com.anddoes.launcher.permission.UPDATE_COUNT*
* *com.majeur.launcher.permission.UPDATE_BADGE*
* *com.huawei.android.launcher.permission.CHANGE_BADGE*
* *com.huawei.android.launcher.permission.READ_SETTINGS*
* *com.huawei.android.launcher.permission.WRITE_SETTINGS*
* *android.permission.READ_APP_BADGE*
* *com.oppo.launcher.permission.READ_SETTINGS*
* *com.oppo.launcher.permission.WRITE_SETTINGS*
* *me.everything.badger.permission.BADGE_COUNT_READ*
* *me.everything.badger.permission.BADGE_COUNT_WRITE*

FairEmail manterrà un elenco di indirizzi da cui ricevi e invii messaggi e userà questa lista per i suggerimenti di contatto quando non viene concesso alcun permesso di contatti a FairEmail. Ciò significa che puoi usare FairEmail senza il fornitore di contatti di Android (rubrica indirizzi). Nota che puoi ancora selezionare i contatti senza concedere i permessi dei contatti a FairEmail, solo suggerendo contatti non funzionerà senza i permessi dei contatti.

<br />

<a name="faq2"></a>
**(2) Perché è mostrata una notifica permanente?**

Una notifica della barra dello stato permanente a bassa priorità con il numero di profili monitorati e il numero di operazioni in attesa (vedi prossima domanda) è mostrata per prevenire che Android termini il servizio che si occupa della ricezione continua delle email. Questo era [già necessario](https://developer.android.com/reference/android/app/Service.html#startForeground(int,%20android.app.Notification)), ma con l'introduzione della [doze mode](https://developer.android.com/training/monitoring-device-state/doze-standby) in Android 6 Marshmallow questo è necessario più che mai. La modalità Doze interromperà tutte le app quando lo schermo è spento per un po' di tempo, a meno che l'app non abbia avviato un servizio in primo piano, che richiede la visualizzazione di una notifica della barra di stato.

La maggior parte, se non tutte, le altre app d'email non mostrano una notifica, con "effetto collaterale" che i nuovi messaggi sono spesso non segnalati o segnalati tardi e che i messaggi non sono inviati o inviati tardi.

Android mostra prima le icone delle notifiche della barra di stato di alta priorità e nasconderà l'icona della notifica di FairEmail se non c'è più spazio per mostrare le icone. In pratica ciò significa che la notifica della barra di stato non prende spazio nella barra di stato, a meno che non ci sia spazio disponibile.

La notifica della barra di stato può essere disabilitata tramite le impostazioni di notifica di FairEmail:

* Android 8 Oreo e successive: tocca il pulsante *Ricevi canale* e disabilita il canale tramite le impostazioni di Android (questo non disabiliterà le notifiche dei messaggi)
* Android 7 Nougat e precedenti: abilitato l'*Uso del servizio in background per sincronizzare i messaggi*, ma assicurati di leggere l'osservazione sotto l'impostazione

Puoi passare alla sincronizzazione dei messaggi periodicamente nelle impostazioni di ricezione per rimuovere la notifica, ma sii consapevole che ciò potrebbe usare maggiore energia della batteria. Vedi [qui](#user-content-faq39) per ulteriori dettagli sull'uso della batteria.

Android 8 Oreo potrebbe anche mostrare una notifica della barra di stato con il testo *Le app sono in esecuzione in background*. Sei pregato di vedere [qui](https://www.reddit.com/r/Android/comments/7vw7l4/psa_turn_off_background_apps_notification/) su come puoi disabilitare questa notifica.

Alcune persone hanno suggerito di usare [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) (FCM) invece di un servizio Android con una notifica della barra dello stato, ma questo richiederebbe ai fornitori dell'email di inviare messaggi FCM ad o ad un server centrale dove sono raccolti tutti i messaggi di inviare messaggi FCM. La prima non si verificherà è l'ultima avrà significative implicazioni sulla privacy.

Se sei venuto/a qui cliccando sulla notifica, dovresti sapere che il prossimo clic aprirà la casella di posta unificata.

<br />

<a name="faq3"></a>
**(3) Cosa sono le operazioni e perché sono in sospeso?**

La notifica della barra dello stato a bassa priorità mostra il numero di operazioni in sospeso, che possono essere:

* *add*: aggiungi messaggio alla cartella remota
* *move*: sposta messaggio ad un'altra cartella remota
* *copy*: copia messaggio in un'altra cartella remota
* *fetch*: preleva il messaggio modificato (spinto)
* *delete*: elimina il messaggio dalla cartella remota
* *seen*: contrassegna il messaggio come letto/non letto nella cartella remota
* *answered*: contrassegna il messaggio come risposto nella cartella remota
* *flag*: aggiunge/rimuove la stella nella cartella remota
* *keyword*: aggiunge/rimuove il flag IMAP nella cartella remota
* *label*: imposta/ripristina l'etichetta di Gmail nella cartella remota
* *headers*: scarica le intestazioni dei messaggi
* *raw*: scarica il messaggio grezzo
* *body*: scarica il testo del messaggio
* *attachment*: scarica l'allegato
* *sync*: sincronizza i messaggi locali e remoti
* *subscribe*: si iscrive alla cartella remota
* *purge*: elimina tutti i messaggi dalla cartella remota
* *send*: invia messaggio
* *exists*: controlla se il messaggio esiste
* *rule*: esegui regola sul corpo del testo

Le operazioni sono elaborate solo quando esiste una connessione al server email o quando sincronizza manualmente. Vedi anche [questa FAQ](#user-content-faq16).

<br />

<a name="faq4"></a>
**(4) Come posso usare un certificato di sicurezza non / password vuota / connessione di testo semplice?**

*... Non affidabile ... non nel certificato ...*
<br />
*... Certificato di sicurezza non valido (Impossibile verificare l'identità del server) ...*

Dovresti provare a risolvere ciò contattando il tuo fornitore o ottenendo un certificato di sicurezza valido perché i certificati di sicurezza non validi non sono sicuri e consente gli [attacchi man-in-the-middle](https://en.wikipedia.org/wiki/Man-in-the-middle_attack). Se il denaro è un ostacolo, puoi ottenere dei certificati di sicurezza gratuiti da [Let's Encrypt](https://letsencrypt.org).

In alternativa, puoi accettare la fingerprint dei certificati server non validi in questo modo:

1. Assicurati di star usando una connessione internet affidabile (nessuna rete Wi-Fi pubblica, etc)
1. Vai alla schermata di configurazione tramite il menu di navigazione (scorri verso l'interno dal lato sinistro)
1. Nella fase 1 e 2 tocca *Gestisci* e tocca il profilo e l'identità difettosi
1. Seleziona/salva il profilo e l'identità
1. Seleziona la casella sotto al messaggio di errore e salva di nuovo

Questo "fisserà" il certificato del server per prevenire attacchi dell'uomo di mezzo.

Nota che le versioni più vecchie di Android potrebbero non riconoscere le autorità di certificazione più nuove come Let's Encrypt causando alle connessioni di essere considerate non sicure, vedi anche [qui](https://developer.android.com/training/articles/security-ssl).

*Ancoraggio di fiducia per il percorso di certificazione non trovato*

*... java.security.cert.CertPathValidatorException: Ancoraggio di fiducia per il percorso di certificazione non trovato ...* significa che il gestore di fiducia di Android predefinito non è stato capace di verificare la catena di certificati del server.

Dovresti correggere la configurazione del server o accettare l'impronta digitale mostrata sotto al messaggio di errore.

Nota che questo problema può essere anche causato dal mancato invio dal server di tutti i certificati intermedi.

*Password vuota*

Il tuo nome utente è probabilmente facile da indovinare, quindi non è sicuro.

*Connessione del testo semplice*

Il tuo nome utente e la password e tutti i messaggi saranno inviati e ricevuti non crittografati, il che è **molto insicuro** perché un[attacco man-in-the-middle](https://en.wikipedia.org/wiki/Man-in-the-middle_attack) è molto semplice su una connessione non crittografata.

Se vuoi ancora usare un certificato di sicurezza non valido, una password vuota o una connessione di testo semplice, dovrai abilitare le connessioni non sicure nelle impostazioni del profilo e/o identità. STARTTLS dovrebbe essere selezionato per le connessioni in testo semplice. Se abiliti le connessioni non sicure, dovresti connetterti tramite reti affidabili e private soltanto e mai tramite reti pubbliche, come quelle offerte negli hotel, aeroporti, etc.

<br />

<a name="faq5"></a>
**(5) Come posso personalizzare la visualizzazione dei messaggi?**

Nel menu di trabocco a tre puntini puoi abilitare o disabilitare o selezionare:

* *dimensione del testo*: per tre differenti dimensioni del font
* *vista compatta*: per altri elementi di messaggio compressi e un font di testo del messaggio più piccolo

Nella sezione di visualizzazione delle impostazioni puoi attivare o disattivare ad esempio:

* *Casella unificata*: per disabilitare la casella della posta in arrivo unificata ed elencare le cartelle selezionate invece per la casella di posta in arrivo unificata
* *Stile tabulare*: per mostrare un elenco lineare invece delle schede
* *Raggruppa per data*: mostra l'intestazione della data sui messaggi con la stessa data
* *Filettatura di conversazione*: per disabilitare la filettatura della conversazione e mostrare messaggi singoli piuttosto
* *Barra d'azione della conversazione*: per disabilitare la barra di navigazione in basso
* *Evidenzia colore*: per selezionare un colore per il mittente dei messaggi non letti
* *Mostra foto di contatto*: per nascondere le foto di contatto
* *Mostra nomi e indirizzi email*: per mostrare nomi o nomi e indirizzi email
* *Mostra oggetto corsivo*: per mostrare l'oggetto del messaggio come testo normale
* *Mostra stelle*: per nascondere le stelle (preferiti)
* *Mostra anteprima del messaggio*: per mostrare 1-4 linee del testo del messaggio
* *Mostra dettagli dell'indirizzo di default*: per espandere la sezione degli indirizzi di default
* *Mostra automaticamente il messaggio originale per i contatti noti*: per mostrare automaticamente i messaggi originali per i contatti sul tuo dispositivo, sei pregato di leggere [questa FAQ](#user-content-faq35)
* *Mostra automaticamente le immagini per i contatti noti*: per mostrare automaticamente le immagini per i contatti sul tuo dispositivo, sei pregato di leggere [questa FAQ](#user-content-faq35)

Nota che i messaggi sono visibili in anteprima solo quando il testo del messaggio è stato scaricato. I testi del messaggio più grandi non sono scaricati di default su reti misurate (generalmente mobili). Puoi modificare questa cosa nelle impostazioni di connessione.

Alcune persone chiedono:

* di mostrare il soggetto in grassetto, ma il grassetto è già usato per evidenziare i messaggi non letti
* di spostare la stella a sinistra, ma è molto più facile azionarla sul lato destro

<br />

<a name="faq6"></a>
**(6) Come posso accedere a Gmail / G suite?**

Se usi la versione del Play Store o di GitHub di FairEmail, puoi usare la procedura guidata di configurazione rapida per configurare facilmente un profilo e un'identità di Gmail. La procedura guidata di configurazione rapida di Gmail non è disponibile per le build di terze parti, come le build di F-Droid, perché Google ha approvato l'uso di OAuth solo per le build ufficiali.

Se non vuoi usare un profilo sul dispositivo di Gmail, puoi abilitare l'accesso per le "app meno sicure" e usare la password del tuo profilo (sconsigliato) o abilitare l'autenticazione a due fattori e usare una password specifica dell'app. Per usare una password dovrai configurare un profilo e identità tramite i passaggi 1 e 2 della configurazione invece che tramite la procedura guidata rapida.

Sei pregato di vedere [questa FAQ](#user-content-faq111) sul perché sono utilizzabili i profili su dispositivo.

Nota che una password specifica dell'app è necessario quando l'autenticazione a due fattori è abilitata.

<br />

*Password specifica dell'app*

Vedi [qui](https://support.google.com/accounts/answer/185833) come generare una password specifica dell'app.

<br />

*Abilita "App meno sicure"*

**Importante**: usare questo metodo è sconsigliato perché meno affidabile.

**Importante**: I profili Gsuite autorizzati con un nome utente/password smetteranno di funzionare [nel futuro prossimo](https://gsuiteupdates.googleblog.com/2019/12/less-secure-apps-oauth-google-username-password-incorrect.html).

Vedi [qui](https://support.google.com/accounts/answer/6010255) su come abilitare le "app meno sicure" o vai [direttamente all'impostazione](https://www.google.com/settings/security/lesssecureapps).

Se usi profili Gmail multipli, assicurati di modificare l'impostazione delle "app meno sicure" dei profili corretti.

Sappi che devi lasciare la schermata delle impostazioni delle "app meno sicure" usando la freccia indietro per applicarle.

Se usi questo metodo, dovresti usare una [password forte](https://en.wikipedia.org/wiki/Password_strength) per il tuo profilo Gmail, il che è comunque una buona idea. Nota che usare il protocollo IMAP [standard](https://tools.ietf.org/html/rfc3501) non è di per sé meno sicuro.

Quando "app meno sicure" non è abilitato, otterrai l'errore *Autenticazione fallita - credenziali non valide* per i profili (IMAP) e *Nome utente e Password non accettate* per le identità (SMTP).

<br />

*Generale*

Potresti ottenere l'avviso "*Sei pregato di accedere tramite il tuo browser web*". Questo si verifica quando Google considera la rete che ti connette a internet (potrebbe essere un VPN) come insicura. Questo si può prevenire usando la procedura guidata rapida di Gmail su una password specifica dell'app.

Vedi [qui](https://support.google.com/mail/answer/7126229) per le istruzioni di Google e [qui](https://support.google.com/mail/accounts/answer/78754) per la risoluzione dei problemi.

<br />

<a name="faq7"></a>
**(7) Perché i messaggi inviati non compaiono (direttamente) nella cartella inviati?**

I messaggi inviati sono normalmente spostati dalla posta in uscita alla cartella inviata appena il tuo provider aggiunge i messaggi inviati alla cartella degli inviati. Questo richiede la selezione di una cartella inviati nelle impostazioni del profilo e la cartella inviati da impostare per la sincronizzazione.

Alcuni provider non tengono traccia dei messaggi inviati o il server SMTP usato potrebbe non essere correlato al provider. In questi casi FairEmail, aggiungerà automaticamente i messaggi inviati alla cartella inviati sulla sincronizzazione della cartella inviati, che succederà dopo l'invio di un messaggio. Nota che questo risulterà in traffico internet extra.

~~Se non succede, il tuo provider potrebbe non tenere traccia dei messaggi inviati o potresti star usando un server SMTP non correlato al provider.~~ ~~In questi casi puoi abilitare le identità avanzate impostando *Archivia messaggi inviati* per far aggiungere i messaggi inviati a FairEmail alla cartella inviati dopo l'invio di un messaggio.~~ ~~Nota che abilitare quest'impostazione potrebbe risultare in messaggi duplicati se il tuo provider aggiunge anche i messaggi inviati alla cartella inviati.~~ ~~Stai anche attento al fatto che abilitare quest'impostazione risulterà in uso dei dati extra, specialmente inviando i messaggi con grandi allegati.~~

~~Se i messaggi inviati nella posta in uscita non si trovano nella cartella inviati alla sincronizzazione completa, saranno anche spostati dalla posta in uscita alla cartella inviati.~~ ~~Una sincronizzazione completa si verifica riconnettendosi al server o sincronizzando periodicamente o manualmente.~~ ~~Potresti voler abilitare l'impostazione avanzata *Archivia messaggi inviati* invece di spostare i messaggi prima alla cartella degli inviati.~~

<br />

<a name="faq8"></a>
**(8) Posso usare un profilo di Microsoft Exchange?**

Puoi usare un profilo di Microsoft Exchange se è accessibile via IMAP; che è principalmente del caso. Vedi [qui](https://support.office.com/en-us/article/what-is-a-microsoft-exchange-account-47f000aa-c2bf-48ac-9bc2-83e5c6036793) per ulteriori informazioni.

Sei pregato di vedere [qui](https://support.office.com/en-us/article/pop-imap-and-smtp-settings-for-outlook-com-d088b986-291d-42b8-9564-9c414e2aa040) per la documentazione di Microsoft sulla configurazione di un client email. Esiste anche una sezione sugli errori di connessione comune e le soluzioni.

Alcune versioni del server di Exchange più vecchie hanno un bug che causa un messaggio vuoto e allegati corrotti. Sei pregato di vedere [questa FAQ](#user-content-faq110) per una soluzione.

Sei pregato di vedere [questa FAQ](#user-content-faq133) sul supporto di ActiveSync.

Sei pregato di vedere [questa FAQ](#user-content-faq111) sul supporto di OAuth.

<br />

<a name="faq9"></a>
**(9) Cosa sono le identità / come aggiungo un alias</p>

Le identità rappresentano gli indirizzi email *da* cui invii tramite un server (SMTP) email.

Alcuni provider ti consentono di avere alias multipli. Puoi configurarli impostando il campo dell'indirizzo email di un'identità aggiuntiva all'indirizzo dell'alias e impostando il campo del nome utente al tuo indirizzo email principale.

Nota che puoi copiare un'identità tenendola premuta a lungo.

In alternativa, puoi abilitare *Consenti la modifica dell'indirizzo del mittente* nelle impostazioni avanzate di un'identità esistente per modificare il nome utente componendo un nuovo messaggio, se il tuo provider lo consente.

FairEmaiò aggiornerà automaticamente le password delle identità correlate quando aggiorni la password del profilo associato o un'identità correlata.

Vedi [questa FAQ](#user-content-faq33) sulla modifica del nome utente di indirizzi email.

<br />

<a name="faq10"></a>
**~~(10) Cosa significa 'UIDPLUS non supportato'?~~**

~~Il messaggio di errore *UIDPLUS non supportato* significa che il tuo provider email non fornisce l'[estensione UIDPLUS](https://tools.ietf.org/html/rfc4315) di IMAP. Quest'estensione IMAP è richiesta per implementare la sincronizzazione a due passaggi, che non è una funzionalità opzionale. Quindi, a meno che il tuo provider non possa abilitarla, non puoi usare FairEmail per questo provider.~~

<br />

<a name="faq11"></a>
**~~(11) Perché POP non è supportato?~~**

~~Oltre ciò ogni provider email decente supporta [IMAP](https://en.wikipedia.org/wiki/Internet_Message_Access_Protocol) di questi giorni,~~ ~~usare [POP](https://en.wikipedia.org/wiki/Post_Office_Protocol) risulterà in uso della batteria extra non necessari e notifiche dei nuovi messaggi ritardati.~~ ~~Inoltre, POP è inadatto alla sincronizzazione a due passaggi e più spesso che mai le persone leggono e scrivono messaggi su diversi dispositivi di questi giorni.~~

~~Fondamentalmente, POP supporta solo lo scaricamento e l'eliminazione dei messaggi dalla posta in arrivo.~~ ~~Quindi, le operazioni comuni come impostare gli attributi del messaggio (letto, preferito, risposto, etc.), aggiungere (backup) e spostare messaggi non è possibile.~~

~~Vedi anche [cosa scrive a riguardo Google](https://support.google.com/mail/answer/7104828).~~

~~Per esempio [Gmail può importare i messaggi](https://support.google.com/mail/answer/21289) da un altro profilo POP,~~ ~~che si può usare come soluzione per quando il tuo provider non supporta IMAP.~~

~~tl;dr; considera di passare a IMAP.~~

<br />

<a name="faq12"></a>
**(12) Come funziona la crittografia/decrittografia?**

La comunicazione con i server email è sempre crittografata, a meno che tu non lo disattivi esplicitamente. Questa domanda riguarda la crittografia opzionale end-to-end con PGP o S/MIME. Il mittente e il destinatario dovrebbero prima acconsentire a riguardo e scambiarsi messaggi firmati per trasferire la loro chiave pubblica per poter inviare i messaggi crittografati.

*Generale*

Sei pregato di [vedere qui](https://en.wikipedia.org/wiki/Public-key_cryptography) come funziona la crittografia della chiave pubblica/privata.

Crittografia in breve:

* I messaggi **in uscita** sono crittografati con la **chiave pubblica** del destinatario
* I messaggi **in arrivo** sono decrittografati con la **chiave privata** del destinatario

Iscrizione breve:

* I messaggi **In Uscita** sono firmati con la **chiave privata** del mittente
* I messaggi **In Entrata** sono verificati con la **chiave pubblica** del mittente

Per firmare/crittografare un messaggio, basta selezionare il metodo appropriato nella finestra di invio. Puoi sempre aprire la finestra di invio usando il menu di trabocco a tre puntini nel caso tu abbia selezionato *Non mostrare più* prima.

Per verificare una firma o decrittografare un messaggio ricevuto, apri il messaggio e tocca solo il gesto o l'icona del lucchetto proprio sotto la barra d'azione del messaggio.

La prima volta che invii un messaggio crittografato/decrittografato ti potrebbe esser chiesta una chiave di firma. FairEmail archivierà automaticamente la chiave di firma selezionata nell'identità usata per la prossima volta. Se devi ripristinare la chiave di firma, salva solo l'identità o premila a lungo nell'elenco delle identità e seleziona *Ripristina la chiave di firma*. La chiave di firma selezionata è visibile nell'elenco delle identità. Se devi selezionare una chiave su base del caso, puoi creare le identità multiple per lo stesso profilo con lo stesso indirizzo email.

Nelle impostazioni della privacy puoi selezionare il metodo di crittografia predefinito (PGP o S/MIME), abilita *Firma di default*, *Crittografa di default* e *Decrittografa automaticamente i messaggi*, ma sappi che la decrittografia automatica è impossibile se l'interazione dell'utente è necessaria, come selezionare una chiave o leggere un token di sicurezza.

Il testo/gli allegati del messaggio da crittografare e quelli del messaggio decrittografato sono archiviati solo localmente e non saranno mai aggiunti al server remoto. Se vuoi annullare la decrittografia, puoi usare l'elemento del menu di *resincronizza* nel menu a tre puntini della barra d'azione del messaggio.

*PGP*

Dovrai installare e configurare [OpenKeychain](https://f-droid.org/en/packages/org.sufficientlysecure.keychain/) prima. FairEmail è stato testato con la versione 5.4 di OpenKeychain. Le ultime versioni saranno più probabilmente compatibili, quelle precedenti no.

**Importante**: l'app di OpenKeychain è nota per crashare (improvvisamente) quando l'app chiamante (FairEmail) non è ancora autorizzata e riceve una chiave pubblica esistente. Puoi risolverlo provando a inviare un messaggio firmato/crittografato a un mittente con una chiave pubblica sconosciuta.

**Importante**: se l'app di OpenKeychain non può trovare (più) una chiave, potresti dover ripristinare una chiave selezionata precedentemente. Questo si può fare premendo a lungo un'identità nell'elenco delle identità (Configurazione, passaggio 2, Gestione).

**Importante**: per connettere affidabilmente app come FairEmail al servizio di OpenKeychain per crittografare/decrittografare i messaggi, potrebbe essere necessario disabilitare le ottimizzazioni della batteria per l'app di OpenKeychain.

**Importante**: l'app di OpenKeychain necessita del permesso di contatti per funzionare correttamente.

**Importante**: su alcuni dispositivi / versioni di Android è necessario abilitare *Mostra popup con l'esecuzione in background* nei permessi aggiuntivi delle impostazioni dell'app di Android dell'app di OpenKeychain. Senza questo permesso la bozza sarà salvata, ma il popup di OpenKeychain per confermare/selezionare potrebbe non comparire.

FairEmail invierà l'intestazione di [Autocrypt](https://autocrypt.org/) per l'uso da altri client email, ma solo per i messaggi firmati e crittografati perché troppi server email hanno problemi con le intestazioni spesso lunghe di Autocrypt. Nota che il modo più sicuro per avviare uno scambio di email crittografate è inviando messaggi firmati prima. Le intestazioni ricevute di Autocrypt saranno inviate all'app di OpenKeychain per l'archiviazione alla verifica di una firma o decrittografando un messaggio.

Tutta la gestione delle chiavi è delegata all'app OpenKeychain per motivi di sicurezza. Questo significa anche che FairEmail non memorizza le chiavi PGP.

Il PGP crittografato in linea nei messaggi ricevuti è supportato, ma le firme e i messaggi in uscita PGP in linea non sono supportati, vedi [qui](https://josefsson.org/inline-openpgp-considered-harmful.html) sul perché no.

I messaggi di sola firma o sola crittografia non sono una buona idea, sei pregato di vedere qui perché no:

* [Considerazioni di OpenPGP Parte I](https://k9mail.github.io/2016/11/24/OpenPGP-Considerations-Part-I.html)
* [Considerazioni di OpenPGP Parte II](https://k9mail.github.io/2017/01/30/OpenPGP-Considerations-Part-II.html)
* [Considerazioni di OpenPGP Parte III Autocrypt](https://k9mail.github.io/2018/02/26/OpenPGP-Considerations-Part-III-Autocrypt.html)

I messaggi di sola firma sono supportati, i messaggi di sola crittografia no.

Errori comuni:

* *Nessuna chiave*: non c'è nessuna chiave PGP disponibile per uno degli indirizzi e-mail elencati
* *Chiave mancante per la crittografia*: c'è probabilmente una chiave selezionata in FairEmail che non esiste più nell'applicazione OpenKeychain. Resettare la chiave (vedi sopra) probabilmente risolverà questo problema.

*S/MIME*

Crittografare un messaggio richiede le chiavi pubbliche dei destinatari. Firmare un messaggio richiede la tua chiave privata.

Le chiavi private sono memorizzate da Android e sono importabili tramite le impostazioni di sicurezza avanzate di Android. Esiste una scorciatoia (pulsante) per questo nelle impostazioni sulla privacy. Android ti chiederà di impostare un PIN, schema o password se non lo hai fatto prima. Se hai un dispositivo Nokia con Android 9, sei pregato di [leggere questo prima](https://nokiamob.net/2019/08/10/a-bug-prevents-nokia-1-owners-from-unlocking-their-screen-even-with-right-pin-pattern/).

Nota che i certificati possono contenere chiavi multiple per scopi multipli, ad esempio l'autenticazione, la crittografia e la firma. Android importa solo la prima chiave, quindi per importarle tutte, il certificato va prima diviso. Questo non è molto banale e ti si consiglia di chiedere supporto al fornitore del certificato.

Nota che la firma S/MIME con altri algoritmi oltre RSA è supportata, ma sappi che altri client email potrebbero non supportarlo. La crittografia S/MIME è possibile solo con algoritmi simmetrici, il che significa in pratica usando RSA.

Il metodo predefinito di crittografia è PGP, ma l'ultimo metodo di crittografia usato sarà ricordato per l'identità selezionata per la prossima volta. Potresti dover abilitare di nuovo le opzioni di invio nel menu a tre puntini per poter selezionare il metodo di crittografia.

Per consentire chiavi private differenti per lo stesso indirizzo email, FairEmail ti fa sempre selezionare una chiave quando ci sono identità multiple con lo stesso indirizzo email per lo stesso profilo.

Le chiavi pubbliche sono memorizzate da FairEmail e sono importabili verificando una firma per la prima volta o tramite le impostazioni sulla privacy (formato PEM o DER).

FairEmail verifica sia la firma che l'intera catena di certificati.

Errori comuni:

* *Nessun certificato corrispondente a targetContraints trovato*: questo potrebbe significare che stai usando una versione vecchia di FairEmail
* *Impossibile trovare il percorso di certificazione valido al target richiesto*: fondamentalmente ciò significa che uno o più certificati intermedi o di root non sono stati trovati
* *La chiave privata non corrisponde a quelle di crittografia*: la chiave selezionata non è utilizzabile per decrittografare il messaggio, probabilmente perché errata
* *Nessuna chiave privata*: nessun certificato selezionato o disponibile nel keystore di Android

Nel caso in cui la catena del certificato sia errata, puoi toccare sul piccolo pulsante di informazioni per mostrare tutti i certificati. Dopo i dettagli del certificato è mostrato l'emittente o "selfSign". Un certificato è autofirmato quando l'oggetto e l'emittente corrispondono. I certificati da un'autorità di certificazione (CA) sono contrassegnati con "[keyCertSign](https://tools.ietf.org/html/rfc5280#section-4.2.1.3)". I certificati trovati nel negozio di chiavi di Android sono contrassegnate con "Android".

Una catena valida somiglia a questa:

```
Il tuo certificato > zero o più certificati intermedi > certificato CA (root) contrassegnato con "Android"
```

Nota che una catena di certificati sarà sempre invalida quando nessun certificato di ancoraggio è trovato nel negozio di chiavi di Android, il che è fondamentale per la convalida del certificato S/MIME.

Sei pregato di vedere [qui](https://support.google.com/pixelphone/answer/2844832?hl=en) come puoi importare i certificati nel negozio di chiavi di Android.

L'uso di chiavi scadute, messaggi crittografati/firmati in linea e token di sicurezza hardware non è supportato.

Se stai cercando un certificato S/MIME gratuito (di prova), vedi [qui](http://kb.mozillazine.org/Getting_an_SMIME_certificate) per le opzioni. Sei pregato di assicurarti di [leggere questo prima](https://davidroessli.com/logs/2019/09/free-smime-certificates-in-2019/#update20191219) se vuoi richiedere un certificato S/MIME Actalis. Se cerchi un certificato S/MIME economico, ho avuto una buona esperienza con [Certum](https://www.certum.eu/en/smime-certificates/).

Come estrarre una chiave pubblica da un certificato S/MIME:

```
openssl pkcs12 -in filename.pfx/p12 -clcerts -nokeys -out cert.pem
```

Puoi decrittografare le firme di S/MIME, etc., [qui](https://lapo.it/asn1js/).

La firma/crittografia di S/MIME è una funzionalità pro, ma tutte le altre operazioni PGP e S/MIME sono gratuite.

<br />

<a name="faq13"></a>
**(13) Come funziona la ricerca sul dispositivo/server?**

Puoi avviare la ricerca di messaggi sul testo del mittente (da), destinatario (a, cc, bcc), oggetto, parole chiave o del messaggio usando la lente di ingrandimento nella barra di azione di una cartella. Puoi anche cercare da ogni app selezionando *Cerca email* nel menu popup copia/incolla.

Ricercare nella casella di posta in entrata unificata cercherà in tutte le cartelle di tutti i profili, nell'elenco delle cartelle solo nel profilo associato e in una cartella solo in quella.

I messaggi saranno ricercati prima sul dispositivo. Ci sarà un pulsante di azione con un'icona di ricerca ancora in fondo per continuare la ricerca sul server. Puoi selezionare in quale cartella continuare la ricerca.

Il protocollo IMAP non supporta la ricerca in più di una cartella in contemporanea. La ricerca sul server è un'operazione costosa, dunque è impossibile selezionare cartelle multiple.

La ricerca dei messaggi locali è insensibile alle maiuscole e al testo parziale. Il testo del messaggio dei messaggi locali non sarà ricercato se questo non è ancora stato scaricato. La ricerca sul server potrebbe essere o no insensibile alle maiuscole e potrebbe essere su testo parziale o parole intere, in base al provider.

Alcuni server non possono gestire la ricerca nel testo del messaggio quando c'è un numero grande di messaggi. In questo caso esiste un'opzione per disabilitare la ricerca nel testo del messaggio.

Gli operatori di ricerca di Gmail sono utilizzabili prefissando un comando di ricerca con *raw:*. Se hai configurato solo un profilo di Gmail, puoi avviare una ricerca grezza direttamente sul server ricercando dalla casella di posta in entrata unificata. Se hai configurato più profili di Gmail, dovrai prima navigare all'elenco di cartelle o la cartella dell'archivio (tutti i messaggi) del profilo Gmail in cui vuoi cercare. Sei pregato di [vedere qui](https://support.google.com/mail/answer/7190) per gli operatori di ricerca possibili. Per esempio:

`
raw:larger:10M`

Cercare in un gran numero di messaggi sul dispositivo non è molto veloce per due limitazioni:

* [sqlite](https://www.sqlite.org/), il motore del database di Android ha un limite di dimensione del registro, prevenendo che i testi del messaggio siano memorizzati nel database
* Le app di Android hanno memoria limitata per funzionare, anche se il dispositivo ha molta memoria disponibile

Questo significa che cercare il testo di un messaggio richiede che i file contenenti i testi del messaggio devono essere aperti uno per uno per controllare se il testo cercato è contenuto nel file, che è un processo relativamente costoso.

Nelle *impostazioni miste* puoi abilitare *Costruisci indice di ricerca* per aumentare significativamente la velocità di ricerca sul dispositivo, ma sappi che questo aumenterà l'uso della batteria e dello spazio di archiviazione. L'indice di ricerca si basa sulle parole, quindi cercare il testo parziale è impossibile. Ricercare usando l'indice di ricerca è di default E, quindi cercare *mela arancia* cercherà mela E arancia. Le parole separate da virgole risulteranno in ricerche per O, quindi per esempio *mela, arancia* cercherà mela O arancia. Entrambi sono combinabili, quindi cercare *mela, arancia banana* cercherà mela O (arancia E banana). Usare l'indice di ricerca è una funzionalità pro.

Dalla versione 1.1315 è possibile usare le espressioni di ricerca come questa:

```
mela +banana -ciliegia ?noci
```

Questo risulterà nel cercare come in questo modo:

```
("mela" E "banana" E NON "ciliegia") O "noci"
```

Le espressioni di ricerca sono utilizzabili per ricercare sul dispositivo tramite l'indice di ricerca e per cercare sul server dell'email, ma non per cercare sul dispositivo senza l'indice di ricerca per motivi di prestazioni.

Cercare sul dispositivo è una funzionalità gratuita, usando l'indice di ricerca e cercare sul server è una funzionalità pro.

<br />

<a name="faq14"></a>
**(14) Come posso configurare un profilo di Outlook / Live / Hotmail?**

Un profilo Outlook / Live / Hotmail può essere configurato tramite la procedura guidata rapida e selezionando *Outlook*.

Per usare un profilo di Outlook, Live o Hotmail con l'autenticazione a due fattori abilitata, devi creare una password dell'app. Vedi [qui](https://support.microsoft.com/en-us/help/12409/microsoft-account-app-passwords-two-step-verification) per i dettagli.

Vedi [qui](https://support.office.com/en-us/article/pop-imap-and-smtp-settings-for-outlook-com-d088b986-291d-42b8-9564-9c414e2aa040) per le istruzioni di Microsoft.

Per configurare un profilo di Office 365, sei pregato di vedere [questa FAQ](#user-content-faq156).

<br />

<a name="faq15"></a>
**(15) Perché il testo del messaggio continua a caricare?**

L'intestazione e il corpo del messaggio sono recuperati separatamente dal server. Il testo del messaggio di messaggi più grandi non è pre-recuperato su connessioni misurate e sarà recuperato su domanda all'espansione di un messaggio. Il testo del messaggio continuerà a caricare se non c'è alcuna connessione al profilo, vedi anche la prossima domanda, o se ci sono altre operazioni, come la sincronizzazione dei messaggi, sono eseguite.

Puoi controllare il profilo e l'elenco delle cartelle per il profilo e lo stato della cartella (vedi la legenda per il significato delle icone) e l'elenco delle operazioni accessibile tramite il menu di navigazione principale per le operazioni in attesa (vedi [questa FAQ](#user-content-faq3) per il significato delle operazioni).

Se FairEmail si sta bloccando per problemi di connettività precedenti, sei pregato di vedere [questa FAQ](#user-content-faq123), puoi forzare la sincronizzazione tramite il menu a tre puntini.

Nelle impostazioni di ricezione puoi impostare la dimensione massima per scaricare automaticamente i messaggi su connessioni misurate.

Le connessioni mobili sono quasi sempre misurate e alcuni hotspot Wi-Fi (pagati) anche.

<br />

<a name="faq16"></a>
**(16) Perché i messaggi non sono sincronizzati?**

Cause possibili di messaggi non sincronizzati (inviati o ricevuti) sono:

* Il profilo o le cartelle non sono impostate per la sincronizzazione
* Il numero di giorni per sincronizzare i messaggi è impostato troppo basso
* Non c'è alcuna connessione internet utilizzabile
* Il server email è temporaneamente non disponibile
* Android ha interrotto il servizio di sincronizzazione

Quindi, controlla le impostazioni del tuo profilo e della cartella e controlla se i profili/le cartelle sono connesse (vedi la leggenda nel menu di navigazione per il significato delle icone).

Se ci sono messaggi di errore, sei pregato di vedere [questa FAQ](#user-content-faq22).

Su alcuni dispositivi, dove ci sono molte applicazioni che competono per la memoria, Android potrebbe interrompere il servizio di sincronizzazione come ultima risorsa.

Alcune versioni di Android interrompono le app e i servizi troppo aggressivamente. Vedi [questo sito web dedicato](https://dontkillmyapp.com/) e [questo problema Android](https://issuetracker.google.com/issues/122098785) per ulteriori informazioni.

Disabilitare le ottimizzazioni della batteria (passaggio 4 di configurazione) riduce la possibilità che Android interromperà il servizio di sincronizzazione.

In caso di errori di connessione consecutivi, FairEmail manterrà sempre di più per non drenare la batteria del tuo dispositivo. Questo è descritto in [questa FAQ](#user-content-faq123).

<br />

<a name="faq17"></a>
**~~(17) Perché la sincronizzazione manuale non funziona?~~**

~~Se il menu *Sincronizza ora* è attenuata, non esiste alcuna connessione al profilo.~~

~~Vedi la domanda precedente per ulteriori informazioni.~~

<br />

<a name="faq18"></a>
**(18) Perché l'anteprima del messaggio non è sempre mostrata?**

L'anteprima del testo del messaggio non è mostrabile se il corpo del messaggio non è ancora stato scaricato. Vedi anche [questa FAQ](#user-content-faq15).

<br />

<a name="faq19"></a>
**(19) Perché le funzionalità pro sono così costose?**

La domanda giusta è "*perché ci sono così tante tasse e commissioni?*":

* IVA: 25% (in base al tuo paese)
* Commissione di Google: 30 %
* Tassa sul reddito: 50 %
* <sub>Commissione di Paypal: 5-10 % in base al paese/importo</sub>

Quindi, cosa rimane per lo sviluppatore è solo una frazione di ciò che paghi.

Nota che solo alcune funzionalità di convenienza e avanzate devono essere acquistate il che significa che FairEmail è fondamentalmente libero all'uso.

Nota anche che gran parte delle app gratuite compariranno come non sostenibili alla fine, mentre FairEmail è propriamente mantenuto e supportato, e che le app gratuite potrebbero avere una cattura, come inviare informazioni sensibili alla privacy su internet.

Ho lavorato a FairEmail quasi ogni giorno per oltre due anni, quindi penso che il prezzo sia più che ragionevole. Per questo motivo non ci saranno nemmeno sconti.

<br />

<a name="faq20"></a>
**(20) Posso ottenere un rimborso?**

Se una funzionalità pro acquistata non funziona come intesa e non è causata da un problema nelle funzionalità gratuite e non posso risolvere il problema tempestivamente, puoi ottenere un rimborso. In tutti gli altri casi non è possibile alcun rimborso. In nessuna circostanza c'è la possibilità di rimborso per alcun problema correlato alle funzionalità gratuite, non essendo stato pagato nulla per essi e perché sono valutabili senza alcuna limitazione. Mi assumo la mia responsabilità come venditore per consegnare quanto promesso e prevedo di prendere la responsabilità per avervi informati su ciò che state acquistando.

<a name="faq21"></a>
**(21) Come abilito il Led di notifica?**

Prima di Android 8 Oreo: esiste un'opzione avanzata nelle impostazioni di notifica dell'app per questo.

Android 8 Oreo e successive: sei pregato di vedere [qui](https://developer.android.com/training/notify-user/channels) su come configurare i canali di notifica. Puoi usare il pulsante *Canale predefinito* nelle impostazioni di notifica dell'app per andare direttamente alle impostazioni corrette del canale di notifica di Android.

Nota che le app non possono più modificare le impostazioni di notifica, incluse quelle del Led di notifica, su Android 8 Oreo e successive.

Talvolta è necessario disabilitare l'impostazione *Mostra anteprima del messaggio nelle notifiche* o abilitare le impostazioni *Mostra solo le notifiche con un testo di anteprima* per risolvere bug in Android. Questo potrebbe applicarsi anche ai suoni e le vibrazioni di notifica.

Impostare un colore chiaro prima di Android 8 non è supportato ed è impossibile su Android 8 e successive.

<br />

<a name="faq22"></a>
**(22) Cosa significa l'errore profilo/cartella ...?**

FairEmail non nasconde gli errori come spesso fanno le app simili, quindi è più facile diagnosticare i problemi.

FairEmail proverà automaticamente a riconnettersi dopo un ritardo. Questo ritardo sarà raddoppiato dopo ogni tentativo fallito di prevenire il drenaggio della batteria e per prevenire che rimanga bloccato permanentemente.

Esistono degli errori generali e specifici ai profili di Gmail (vedi sotto).

**Errori generali**

L'errore *... Autenticazione fallita ...* o *... AUTHENTICATE fallito ...* potrebbe significare che il tuo nome utente o la tua password siano scorretti. Alcuni provider prevedono come nome utente solo il *nome utente* e altri il tuo indirizzo email completo *username@example.com*. Quando copi/incolli per inserire un nome utente o una password, potrebbero essere copiati i caratteri invisibili, che potrebbero anche causare questo problema. Anche alcuni gestori di password sono noti per farlo in modo scorretto. Il nome utente potrebbe essere sensibile alle maiuscole, quindi prova con soli caratteri minuscoli. La password è quasi sempre sensibile alle maiuscole. Alcuni provider richiedono l'uso di una password dell'app invece di quella del profilo, quindi sei pregato di controllare la documentazione del provider. A volte è necessario abilitare l'accesso esterno (IMAP/SMTP) sul sito web del provider prima. Altre cause possibili sono che il profilo è bloccato o che l'accesso è stato limitato amministrativamente in qualche modo, per esempio consentendo di accedere solo da certe reti / indirizzi IP.

L'errore *... Troppi tentativi errati di autenticazione ...* potrebbe significare che stai usando una password del profilo di Yahoo invece di una password dell'app. Sei pregato di vedere [questa FAQ](#user-content-faq88) su come configurare un profilo di Yahoo.

Il messaggio *... +OK ...* potrebbe significare che una porta POP3 (solitamente la porta numero 995) stia venendo utilizzata per un profilo IMAP (solitamente la porta numero 993).

Gli errori *... saluto non valido ...*, *... richiede indirizzo valido ...* e *... Parametro a HELO non conforme alla sintassi RFC ...* potrebbero essere risolti cambiando l'impostazione di identità avanzata *Usa indirizzo IP locale invece del nome dell'host*.

L'errore *... Impossibile connettersi all'host ...* significa che non c'è stata alcuna risposta dal server dell'email entro un tempo ragionevole (20 secondi di default). Principalmente questo indica problemi di connettività, possibilmente causati da una VPN o dall'app di un firewall. Puoi provare ad aumentare il timeout di connessione nelle impostazioni di connessione di FairEmail, per quando il server email è davvero lento.

L'errore *... Connessione rifiutata ...* significa che il server email o qualcosa tra il server email e l'app, come un firewall, ha attivamente rifiutato la connessione.

L'errore *... Rete irraggiungibile ...* significa che il server dell'email non era raggiungibile tramite la connessione a internet attuale, ad esempio perché il traffico internet è limitato al solo traffico locale.

L'errore *... Ospite non risolto ...*, *... Impossibile risolvere l'host ...* o *... Nessun indirizzo associato con il nome dell'host ...* significa che non è stato possibile risolvere l'indirizzo del server email in un indirizzo IP. Questo potrebbe essere causato da un VPN, un blocco degli annunci o un server [DNS](https://en.wikipedia.org/wiki/Domain_Name_System) irraggiungibile o non funzionante propriamente (locale).

L'errore *... Annullamento della connessione causato dal software ...* significa che il server dell'email o qualcosa tra FairEmail e il server dell'email ha terminato attivamente una connessione esistente. Questo può per esempio verificarsi quando la connessione è persa inaspettatamente. Un esempio tipico è l'attivazione della modalità aereo.

Gli errori *... Disconnettendo BYE ...*, *... Connessione ripristinata da pari ...* significa che il server dell'email ha terminato attivamente una connessione esistente.

L'errore *... Connessione chiusa da pari ...* potrebbe esser causata da un server non aggiornato di Exchange, vedi [qui](https://blogs.technet.microsoft.com/pki/2010/09/30/sha2-and-windows/) per ulteriori informazioni.

Gli errori *... Errore di lettura ...*, *... Errore di scrittura ...*, *... Lettura scaduta ...*, *... Conduttura rotta ...* significa che il server dell'email non risponde più o che la connessione internet non è buona.

L'errore *... Interruzione imprevista del flusso di input di zlib ...* significa che non tutti i dati sono stati ricevuti, possibilmente a causa di una connessione interrotta o non buona.

L'errore *... connessione fallita ...* potrebbe indicare [Troppe connessioni simultanee](#user-content-faq23).

L'avviso *... Crittografia non supportata ...* significa che la serie di caratteri del messaggio è sconosciuta o non supportata. FairEmail presumerà ISO-8859-1 (Latin1), che in gran parte dei casi risulterà nel mostrare correttamente il messaggio.

Sei pregato di [vedere qui](#user-content-faq4) per gli errori *... Inaffidabile ... non nel certificato ...*, *... Certificato di sicurezza non valido (Impossibile verificare l'identità del server) ...* o *... Ancoraggio di fiducia per il percorso di certificazione non trovato ...*

Sei pregato di [vedere qui](#user-content-faq127) per l'errore *... Argomenti HELO sintatticamente non validi ...*.

Sei pregato di [vedere qui](#user-content-faq41) per l'errore *... Stretta di mano fallita ...*.

Vedi [qui](https://linux.die.net/man/3/connect) per cosa significano i codici di errore come EHOSTUNREACH e ETIMEDOUT.

Cause possibili sono:

* Un firewall o router sta bloccando le connessioni al server
* Il nome dell'host o numero di porta non è valido
* Ci sono problemi con la connessione a internet
* Ci sono problemi con la risoluzione dei nomi di dominio (Yandex: prova a disabilitare il DNS privato nelle impostazioni di Android)
* Il server email si rifiuta di accettare le connessioni (esterne)
* Il server dell'email rifiuta di accettare un messaggio, ad esempio perché troppo grande o contenente collegamenti inaccettabili
* Ci sono troppe connessioni al server, vedi anche la prossima domanda

Molte reti Wi-Fi pubbliche bloccano le email in uscita per prevenire lo spam. A volte puoi risolverlo usando un'altra porta SMTP. Vedi la documentazione del provider per i numeri di porta utilizzabili.

Se stai usando una [VPN](https://en.wikipedia.org/wiki/Virtual_private_network), il provider della VPN potrebbe bloccare la connessione perché prova troppo aggressivamente a prevenire lo spam. Nota che anche [Google Fi](https://fi.google.com/) sta usando una VPN.

**Errori di invio**

I server SMTP possono rifiutare i messaggi per [varie motivazioni](https://en.wikipedia.org/wiki/List_of_SMTP_server_return_codes). Messaggi troppo grandi e l'innesco del filtro di spam di un server email sono i motivi più comuni.

* Il limite della dimensione dell'allegato per Gmail [è 25 MB](https://support.google.com/mail/answer/6584)
* Il limite delle dimensioni dell'allegato per Outlook e Office 365 [è 20 MB](https://support.microsoft.com/en-us/help/2813269/attachment-size-exceeds-the-allowable-limit-error-when-you-add-a-large)
* Il limite della dimensione dell'allegato per Yahoo [è 25 MB](https://help.yahoo.com/kb/SLN5673.html)
* *554 5.7.1 Servizio non disponibile; Host del client xxx.xxx.xxx.xxx bloccato*, sei pregato [di vedere qui](https://docs.gandi.net/en/gandimail/faq/error_types/554_5_7_1_service_unavailable.html)
* *501 Errore di sintassi – linea troppo lunga* è spesso causata dall'utilizzo di una lunga intestazione Autocrypt
* *503 5.5.0 Destinatario già specificato* significa principalmente che un indirizzo è in uso sia come indirizzo TO che CC
* *554 5.7.1 ... non è consentito trasmettere* significa che il server di posta elettronica non riconosce il nome utente/indirizzo email. Si prega di controllare il nome host e il nome utente/indirizzo email nelle impostazioni di identità.
* *550 Messaggio di spam rifiutato perché l'IP è elencato per ...* significa che il server dell'email ha rifiutato di inviare un messaggio dall'indirizzo di rete attuale (pubblico) perché usato erroneamente per inviare spam da qualcun altro (si spera) in precedenza. Sei pregato di provare ad abilitare la modalità aereo per 10 minuti per acquisire un nuovo indirizzo di rete.
* *571 5.7.1 Il messaggio contiene spam o virus o il mittente è bloccato ...* significa che il server dell'email ha considerato un messaggio come spam. Questo significa probabilmente che i filtri spam del server email sono troppo rigidi. Dovrai contattare il provider di posta elettronica per avere supporto a riguardo.

Se vuoi usare il server SMTP di Gmail per risolvere un filtro spam in uscita troppo severo o per migliorare la consegna dei messaggi:

* Verifica il tuo indirizzo email [qui](https://mail.google.com/mail/u/0/#settings/accounts) (dovrai usare un browser desktop)
* Cambia le impostazioni di identità come queste (configurazione, fase 2, tocca Gestisci, tocca identità):

&emsp;&emsp;Username: *your Gmail address*<br /> &emsp;&emsp;Password: *[an app password](#user-content-faq6)*<br /> &emsp;&emsp;Host: *smtp.gmail.com*<br /> &emsp;&emsp;Port: *465*<br /> &emsp;&emsp;Encryption: *SSL/TLS*<br /> &emsp;&emsp;Reply to address: *your email address* (advanced identity settings)<br />

<br />

**Errori di Gmail**

L'autorizzazione della configurazione dei profili di Gmail con la procedura guidata rapida devono essere ricaricati periodicamente tramite il [gestore del profilo Android](https://developer.android.com/reference/android/accounts/AccountManager). Questo richiede i permessi di contatto/profilo e connettività internet.

L'errore *... Autenticazione fallita ... Profilo non trovato ...* significa che un profilo di Gmail precedentemente autorizzato è stato rimosso dal dispositivo.

Gli errori *... Autenticazione fallita ... Nessun token ...* significa che il manager del profilo di Android non è riuscito a ricaricare l'autorizzazione di un profilo di Gmail.

L'errore *... Autenticazione fallita ... errore di rete ...* significa che il manager del profilo di Android non è riuscito a ricaricare l'autorizzazione di un profilo di Gmail a causa di problemi con la connessione a internet

L'errore *... Autenticazione fallita ... Credenziali non valide ...* potrebbe essere causato dal cambiamento della password del profilo o avendo revocati i permessi di profilo/contatti necessari. Nel caso in cui la password del profilo fosse cambiata, dovrai autenticare di nuovo le impostazioni del profilo di Android. Nel caso in cui fossero stati revocati i permessi, puoi avviare la procedura guidata di configurazione rapida di Gmail per garantire di nuovo i permessi richiesti (non devi riconfigurare il profilo).

L'errore *... ServiceDisabled ...* might be caused by enrolling in the [Advanced Protection Program](https://landing.google.com/advancedprotection/): "*To read your email, you can (must) use Gmail - You won’t be able to use your Google Account with some (all) apps & services that require access to sensitive data like your emails*", see [here](https://support.google.com/accounts/answer/7519408?hl=en&ref_topic=9264881).

When in doubt, you can ask for [support](#user-content-support).

<br />

<a name="faq23"></a>
**(23) Why do I get alert ... ?**

*General*

Alerts are warning messages sent by email servers.

*Too many simultaneous connections* or *Maximum number of connections exceeded*

This alert will be sent when there are too many folder connections for the same email account at the same time.

Possible causes are:

* Ci sono client email multipli connessi allo stesso profilo
* Lo stesso client email è connesso diverse volte allo stesso profilo
* Le connessioni precedenti sono state terminate bruscamente per esempio perdendo bruscamente la connettività internet

First try to wait some time to see if the problem resolves itself, else:

* o passa alla verifica periodica dei messaggi nelle impostazioni di ricezione, che risulterà nell'apertura delle cartelle una per volta
* o imposta alcune cartelle a sondaggio invece di sincronizzare (tenere premuto a lungo la cartella nell'elenco della cartella, modificare le proprietà)

An easy way to configure periodically checking for messages for all folders except the inbox is to use *Apply to all ...* in the three-dots menu of the folder list and to tick the bottom two advanced checkboxes.

The maximum number of simultaneous folder connections for Gmail is 15, so you can synchronize at most 15 folders simultaneously on *all* your devices at the same time. For this reason Gmail *user* folders are set to poll by default instead of synchronize always. When needed or desired, you can change this by long pressing a folder in the folder list and selecting *Edit properties*. See [here](https://support.google.com/mail/answer/7126229) for details.

When using a Dovecot server, you might want to change the setting [mail_max_userip_connections](https://doc.dovecot.org/settings/dovecot_core_settings/#mail-max-userip-connections).

Note that it will take the email server a while to discover broken connections, for example due to going out of range of a network, which means that effectively only half of the folder connections are available. For Gmail this would be just 7 connections.

<br />

<a name="faq24"></a>
**(24) What is browse messages on the server?**

Browse messages on the server will fetch messages from the email server in real time when you reach the end of the list of synchronized messages, even when the folder is set to not synchronize. You can disable this feature in the advanced account settings.

<br />

<a name="faq25"></a>
**(25) Why can't I select/open/save an image, attachment or a file?**

When a menu item to select/open/save a file is disabled (dimmed) or when you get the message *Storage access framework not available*, the [storage access framework](https://developer.android.com/guide/topics/providers/document-provider), a standard Android component, is probably not present. This might be because your custom ROM does not include it or because it was actively removed (debloated).

FairEmail does not request storage permissions, so this framework is required to select files and folders. No app, except maybe file managers, targeting Android 4.4 KitKat or later should ask for storage permissions because it would allow access to *all* files.

The storage access framework is provided by the package *com.android.documentsui*, which is visible as *Files* app on some Android versions (notable OxygenOS).

You can enable the storage access framework (again) with this adb command:

```
pm install -k --user 0 com.android.documentsui
```

Alternatively, you might be able to enable the *Files* app again using the Android app settings.

<br />

<a name="faq26"></a>
**(26) Can I help to translate FairEmail in my own language?**

Yes, you can translate the texts of FairEmail in your own language [on Crowdin](https://crowdin.com/project/open-source-email). Registration is free.

If you would like your name or alias to be included in the list of contributors in *About* the app, please [contact me](https://contact.faircode.eu/?product=fairemailsupport).

<br />

<a name="faq27"></a>
**(27) How can I distinguish between embedded and external images?**

External image:

![External image](https://github.com/M66B/FairEmail/blob/master/images/baseline_image_black_48dp.png)

Embedded image:

![Embedded image](https://github.com/M66B/FairEmail/blob/master/images/baseline_photo_library_black_48dp.png)

Broken image:

![Broken image](https://github.com/M66B/FairEmail/blob/master/images/baseline_broken_image_black_48dp.png)

Note that downloading external images from a remote server can be used to record you did see a message, which you likely don't want if the message is spam or malicious.

<br />

<a name="faq28"></a>
**(28) How can I manage status bar notifications?**

In the setup you'll find a button *Manage notifications* to directly navigate to the Android notifications settings for FairEmail.

On Android 8.0 Oreo and later you can manage the properties of the individual notification channels, for example to set a specific notification sound or to show notifications on the lock screen.

FairEmail has the following notification channels:

* Servizio: usato per la notifica del servizio di sincronizzazione, vedi anche [questa FAQ](#user-content-faq2)
* Invio: usato per le notifiche del servizio di invio
* Notifiche: usato per le notifiche dei nuovi messaggi
* Avviso: usato per le notifiche di avviso
* Errore: usato per le notifiche di errore

See [here](https://developer.android.com/guide/topics/ui/notifiers/notifications#ManageChannels) for details on notification channels. In short: tap on the notification channel name to access the channel settings.

On Android before Android 8 Oreo you can set the notification sound in the settings.

See [this FAQ](#user-content-faq21) if your device has a notification light.

<br />

<a name="faq29"></a>
**(29) How can I get new message notifications for other folders?**

Just long press a folder, select *Edit properties*, and enable either *Show in unified inbox* or *Notify new messages* (available on Android 7 Nougat and later only) and tap *Save*.

<br />

<a name="faq30"></a>
**(30) How can I use the provided quick settings?**

There are quick settings (settings tiles) available to:

* abilitare/disabilitare globalmente la sincronizzazione
* mostrare il numero di nuovi messaggi e contrassegnarli come visti (non letti)

Quick settings require Android 7.0 Nougat or later. The usage of settings tiles is explained [here](https://support.google.com/android/answer/9083864).

<br />

<a name="faq31"></a>
**(31) How can I use the provided shortcuts?**

There are shortcuts available to:

* comporre un nuovo messaggio a un contatto preferito
* configura profili, identità, etc

Shortcuts require Android 7.1 Nougat or later. The usage of shortcuts is explained [here](https://support.google.com/android/answer/2781850).

<br />

<a name="faq32"></a>
**(32) How can I check if reading email is really safe?**

You can use the [Email Privacy Tester](https://www.emailprivacytester.com/) for this.

<br />

<a name="faq33"></a>
**(33) Why are edited sender addresses not working?**

Most providers accept validated addresses only when sending messages to prevent spam.

For example Google modifies the message headers like this for *unverified* addresses:

```
Da: Qualcuno <somebody@example.org>
X-Google-Original-From: Qualcuno <somebody+extra@example.org>
```

This means that the edited sender address was automatically replaced by a verified address before sending the message.

Note that this is independent of receiving messages.

<br />

<a name="faq34"></a>
**(34) How are identities matched?**

Identities are as expected matched by account. For incoming messages the *to*, *cc*, *bcc*, *from* and *(X-)delivered/envelope/original-to* addresses will be checked (in this order) and for outgoing messages (drafts, outbox and sent) only the *from* addresses will be checked. Equal addresses have precedence over partially matching addresses, except for *delivered-to* addresses.

The matched address will be shown as *via* in the addresses section of received messages (between the message header and message text).

Note that identities needs to be enabled to be able to be matched and that identities of other accounts will not be considered.

Matching will be done only once on receiving a message, so changing the configuration will not change existing messages. You could clear local messages by long pressing a folder in the folder list and synchronize the messages again though.

It is possible to configure a [regex](https://en.wikipedia.org/wiki/Regular_expression) in the identity settings to match **the username** of an email address (the part before the @ sign).

Note that the domain name (the parts after the @ sign) always needs to be equal to the domain name of the identity.

If you like to match a catch-all email address, this regex is mostly okay:

```
.*
```

If you like to match the special purpose email addresses abc@example.com and xyx@example.com and like to have a fallback email address main@example.com as well, you could do something like this:

* Identità: abc@example.com; regex: **(?i)abc**
* Identità: xyz@example.com; regex: **(?i)xyz**
* Identità: main@example.com; regex: **^(?i)((?!abc|xyz).)\*$**

Matched identities can be used to color code messages. The identity color takes precedence over the account color. Setting identity colors is a pro feature.

<br />

<a name="faq35"></a>
**(35) Why should I be careful with viewing images, attachments, the original message, and opening links?**

Viewing remotely stored images (see also [this FAQ](#user-content-faq27)) and opening links might not only tell the sender that you have seen the message, but will also leak your IP address. See also this question: [Why email's link is more dangerous than web search's link?](https://security.stackexchange.com/questions/241139/why-emails-link-is-more-dangerous-than-web-searchs-link).

Opening attachments or viewing an original message might load remote content and execute scripts, that might not only cause privacy sensitive information to leak, but can also be a security risk.

Note that your contacts could unknowingly send malicious messages if they got infected with malware.

FairEmail formats messages again causing messages to look different from the original, but also uncovering phishing links.

Note that reformatted messages are often better readable than original messages because the margins are removed, and font colors and sizes are standardized.

The Gmail app shows images by default by downloading the images through a Google proxy server. Since the images are downloaded from the source server [in real-time](https://blog.filippo.io/how-the-new-gmail-image-proxy-works-and-what-this-means-for-you/), this is even less secure because Google is involved too without providing much benefit.

You can show images and original messages by default for trusted senders on a case-by-case basis by checking *Do not ask this again for ...*.

If you want to reset the default *Open with* apps, please [see here](https://www.androidauthority.com/how-to-set-default-apps-android-clear-621269/).

<br />

<a name="faq36"></a>
**(36) How are settings files encrypted?**

Short version: AES 256 bit

Long version:

* La chiave a 256 bit è generata con *PBKDF2WithHmacSHA1* usando un sale casuale sicuro a 128 bit e 65536 iterazioni
* La cifra è *AES/CBC/PKCS5Padding*

<br />

<a name="faq37"></a>
**(37) How are passwords stored?**

All supported Android versions [encrypt all user data](https://source.android.com/security/encryption), so all data, including usernames, passwords, messages, etc, is stored encrypted.

If the device is secured with a PIN, pattern or password, you can make the account and identity passwords visible. If this is a problem because you are sharing the device with other people, consider to use [user profiles](https://www.howtogeek.com/333484/how-to-set-up-multiple-user-profiles-on-android/).

<br />

<a name="faq39"></a>
**(39) How can I reduce the battery usage of FairEmail?**

Recent Android versions by default report *app usage* as a percentage in the Android battery settings screen. **Confusingly, *app usage* is not the same as *battery usage* and is not even directly related to battery usage!** The app usage (while in use) will be very high because FairEmail is using a foreground service which is considered as constant app usage by Android. However, this doesn't mean that FairEmail is constantly using battery power. The real battery usage can be seen by navigating to this screen:

*Android settings*, *Battery*, three-dots menu *Battery usage*, three-dots menu *Show full device usage*

As a rule of thumb the battery usage should be below or in any case not be much higher than *Mobile network standby*. If this isn't the case, please turn on *Auto optimize* in the receive settings. If this doesn't help, please [ask for support](https://contact.faircode.eu/?product=fairemailsupport).

It is inevitable that synchronizing messages will use battery power because it requires network access and accessing the messages database.

If you are comparing the battery usage of FairEmail with another email client, please make sure the other email client is setup similarly. For example comparing always sync (push messages) and (infrequent) periodic checking for new messages is not a fair comparison.

Reconnecting to an email server will use extra battery power, so an unstable internet connection will result in extra battery usage. Also, some email servers prematurely terminate idle connections, while [the standard](https://tools.ietf.org/html/rfc2177) says that an idle connection should be kept open for 29 minutes. In these cases you might want to synchronize periodically, for example each hour, instead of continuously. Note that polling frequently (more than every 30-60 minutes) will likely use more battery power than synchronizing always because connecting to the server and comparing the local and remote messages are expensive operations.

[On some devices](https://dontkillmyapp.com/) it is necessary to *disable* battery optimizations (setup step 4) to keep connections to email servers open. In fact, leaving battery optimizations enabled can result in extra battery usage for all devices, even though this sounds contradictory!

Most of the battery usage, not considering viewing messages, is due to synchronization (receiving and sending) of messages. So, to reduce the battery usage, set the number of days to synchronize message for to a lower value, especially if there are a lot of recent messages in a folder. Long press a folder name in the folders list and select *Edit properties* to access this setting.

If you have at least once a day internet connectivity, it is sufficient to synchronize messages just for one day.

Note that you can set the number of days to *keep* messages for to a higher number than to *synchronize* messages for. You could for example initially synchronize messages for a large number of days and after this has been completed reduce the number of days to synchronize messages for, but leave the number of days to keep messages for. After decreasing the number of days to keep messages for, you might want to run the cleanup in the miscellaneous settings to remove old files.

In the receive settings you can enable to always synchronize starred messages, which will allow you to keep older messages around while synchronizing messages for a limited number of days.

Disabling the folder option *Automatically download message texts and attachments* will result in less network traffic and thus less battery usage. You could disable this option for example for the sent folder and the archive.

Synchronizing messages at night is mostly not useful, so you can save on battery usage by not synchronizing at night. In the settings you can select a schedule for message synchronization (this is a pro feature).

FairEmail will by default synchronize the folder list on each connection. Since folders are mostly not created, renamed and deleted very often, you can save some network and battery usage by disabling this in the receive settings.

FairEmail will by default check if old messages were deleted from the server on each connection. If you don't mind that old messages that were delete from the server are still visible in FairEmail, you can save some network and battery usage by disabling this in the receive settings.

Some providers don't follow the IMAP standard and don't keep connections open long enough, forcing FairEmail to reconnect often, causing extra battery usage. You can inspect the *Log* via the main navigation menu to check if there are frequent reconnects (connection closed/reset, read/write error/timeout, etc). You can workaround this by lowering the keep-alive interval in the advanced account settings to for example 9 or 15 minutes. Note that battery optimizations need to be disabled in setup step 4 to reliably keep connections alive.

Some providers send every two minutes something like '*Still here*' resulting in network traffic and your device to wake up and causing unnecessary extra battery usage. You can inspect the *Log* via the main navigation menu to check if your provider is doing this. If your provider is using [Dovecot](https://www.dovecot.org/) as IMAP server, you could ask your provider to change the [imap_idle_notify_interval](https://wiki.dovecot.org/Timeouts) setting to a higher value or better yet, to disable this. If your provider is not able or willing to change/disable this, you should consider to switch to periodically instead of continuous synchronization. You can change this in the receive settings.

If you got the message *This provider does not support push messages* while configuring an account, consider switching to a modern provider which supports push messages (IMAP IDLE) to reduce battery usage.

If your device has an [AMOLED](https://en.wikipedia.org/wiki/AMOLED) screen, you can save battery usage while viewing messages by switching to the black theme.

If auto optimize in the receive settings is enabled, an account will automatically be switched to periodically checking for new messages when the email server:

* Dice '*Ancora qui*' entro 3 minuti
* Il server email non supporta i messaggi push
* L'intervallo di mantenimento in vita è inferiore a 12 minuti

In addition, the trash and spam folders will be automatically set to checking for new messages after three successive [too many simultaneous connections](#user-content-faq23) errors.

<br />

<a name="faq40"></a>
**(40) How can I reduce the data usage of FairEmail?**

You can reduce the data usage basically in the same way as reducing battery usage, see the previous question for suggestions.

It is inevitable that data will be used to synchronize messages.

If the connection to the email server is lost, FairEmail will always synchronize the messages again to make sure no messages were missed. If the connection is unstable, this can result in extra data usage. In this case, it is a good idea to decrease the number of days to synchronize messages for to a minimum (see the previous question) or to switch to periodically synchronizing of messages (receive settings).

To reduce data usage, you could change these advanced receive settings:

* Controlla che i messaggi vecchi siano stati rimossi dal server: disabilitato
* Sincronizzazione (condivisa) dell'elenco delle cartelle: disabilitato

By default FairEmail does not download message texts and attachments larger than 256 KiB when there is a metered (mobile or paid Wi-Fi) internet connection. You can change this in the connection settings.

<br />

<a name="faq41"></a>
**(41) How can I fix the error 'Handshake failed' ?**

There are several possible causes, so please read to the end of this answer.

The error '*Handshake failed ... WRONG_VERSION_NUMBER ...*' might mean that you are trying to connect to an IMAP or SMTP server without an encrypted connection, typically using port 143 (IMAP) and port 25 (SMTP), or that a wrong protocol (SSL/TLS or STARTTLS) is being used.

Most providers provide encrypted connections using different ports, typically port 993 (IMAP) and port 465/587 (SMTP).

If your provider doesn't support encrypted connections, you should ask to make this possible. If this isn't an option, you could enable *Allow insecure connections* both in the advanced settings AND the account/identity settings.

See also [this FAQ](#user-content-faq4).

L'errore '*Stretta di mano fallita ... SSLV3_ALERT_ILLEGAL_PARAMETER ...*' is either caused by a bug in the SSL protocol implementation or by a too short DH key on the email server and can unfortunately not be fixed by FairEmail.

L'errore '*Stretta di mano fallita ... HANDSHAKE_FAILURE_ON_CLIENT_HELLO ...*' might be caused by the provider still using RC4, which isn't supported since [Android 7](https://developer.android.com/about/versions/nougat/android-7.0-changes.html#tls-ssl) anymore.

The error '*Handshake failed ... UNSUPPORTED_PROTOCOL or TLSV1_ALERT_PROTOCOL_VERSION ...*' might be caused by enabling hardening connections in the connection settings or by Android not supporting older protocols anymore, like SSLv3.

Android 8 Oreo and later [do not support](https://developer.android.com/about/versions/oreo/android-8.0-changes#security-all) SSLv3 anymore. There is no way to workaround lacking RC4 and SSLv3 support because it has completely been removed from Android (which should say something).

You can use [this website](https://ssl-tools.net/mailservers) or [this website](https://www.immuniweb.com/ssl/) to check for SSL/TLS problems of email servers.

<br />

<a name="faq42"></a>
**(42) Can you add a new provider to the list of providers?**

If the provider is used by more than a few people, yes, with pleasure.

The following information is needed:

```
<provider
    name="Gmail"
    link="https://support.google.com/mail/answer/7126229" // link to the instructions of the provider
    type="com.google"> // questo non è necessario
    <imap
        host="imap.gmail.com"
        port="993"
        starttls="false" />
    <smtp
        host="smtp.gmail.com"
        port="465"
        starttls="false" />
</provider>
```

The EFF [writes](https://www.eff.org/nl/deeplinks/2018/06/announcing-starttls-everywhere-securing-hop-hop-email-delivery): "*Additionally, even if you configure STARTTLS perfectly and use a valid certificate, there’s still no guarantee your communication will be encrypted.*"

So, pure SSL connections are safer than using [STARTTLS](https://en.wikipedia.org/wiki/Opportunistic_TLS) and therefore preferred.

Please make sure receiving and sending messages works properly before contacting me to add a provider.

See below about how to contact me.

<br />

<a name="faq43"></a>
**(43) Can you show the original ... ?**

Show original, shows the original message as the sender has sent it, including original fonts, colors, margins, etc. FairEmail does and will not alter this in any way, except for requesting [TEXT_AUTOSIZING](https://developer.android.com/reference/android/webkit/WebSettings.LayoutAlgorithm), which will *attempt* to make small text more readable.

<br />

<a name="faq44"></a>
**~~(44) Can you show contact photos / identicons in the sent folder?~~**

~~Contact photos and identicons are always shown for the sender because this is necessary for conversation threads.~~ ~~Getting contact photos for both the sender and receiver is not really an option because getting contact photo is an expensive operation.~~

<br />

<a name="faq45"></a>
**(45) How can I fix 'This key is not available. To use it, you must import it as one of your own!' ?**

You'll get the message *This key is not available. To use it, you must import it as one of your own!* when trying to decrypt a message with a public key. To fix this you'll need to import the private key.

<br />

<a name="faq46"></a>
**(46) Why does the message list keep refreshing?**

If you see a 'spinner' at the top of the message list, the folder is still being synchronized with the remote server. You can see the progress of the synchronization in the folder list. See the legend about what the icons and numbers mean.

The speed of your device and internet connection and the number of days to synchronize messages for determine how long synchronization will take. Note that you shouldn't set the number of days to synchronize messages for to more than one day in most cases, see also [this FAQ](#user-content-faq39).

<br />

<a name="faq47"></a>
**(47) How do I solve the error 'No primary account or no drafts folder' ?**

You'll get the error message *No primary account or no drafts folder* when trying to compose a message while there is no account set to be the primary account or when there is no drafts folder selected for the primary account. This can happen for example when you start FairEmail to compose a message from another app. FairEmail needs to know where to store the draft, so you'll need to select one account to be the primary account and/or you'll need to select a drafts folder for the primary account.

This can also happen when you try to reply to a message or to forward a message from an account with no drafts folder while there is no primary account or when the primary account does not have a drafts folder.

Please see [this FAQ](#user-content-faq141) for some more information.

<br />

<a name="faq48"></a>
**~~(48) How do I solve the error 'No primary account or no archive folder' ?~~**

~~You'll get the error message *No primary account or no archive folder* when searching for messages from another app. FairEmail needs to know where to search, so you'll need to select one account to be the primary account and/or you'll need to select a archive folder for the primary account.~~

<br />

<a name="faq49"></a>
**(49) How do I fix 'An outdated app sent a file path instead of a file stream' ?**

You likely selected or sent an attachment or image with an outdated file manager or an outdated app which assumes all apps still have storage permissions. For security and privacy reasons modern apps like FairEmail have no full access to all files anymore. This can result into the error message *An outdated app sent a file path instead of a file stream* if a file name instead of a file stream is being shared with FairEmail because FairEmail cannot randomly open files.

You can fix this by switching to an up-to-date file manager or an app designed for recent Android versions. Alternatively, you can grant FairEmail read access to the storage space on your device in the Android app settings. Note that this workaround [won't work on Android Q](https://developer.android.com/preview/privacy/scoped-storage) anymore.

See also [question 25](#user-content-faq25) and [what Google writes about it](https://developer.android.com/training/secure-file-sharing/share-file#RespondToRequest).

<br />

<a name="faq50"></a>
**(50) Can you add an option to synchronize all messages?**

You can synchronize more or even all messages by long pressing a folder (inbox) in the folder list of an account (tap on the account name in the navigation menu) and selecting *Synchronize more* in the popup menu.

<br />

<a name="faq51"></a>
**(51) How are folders sorted?**

Folders are first sorted on account order (by default on account name) and within an account with special, system folders on top, followed by folders set to synchronize. Within each category the folders are sorted on (display) name. You can set the display name by long pressing a folder in the folder list and selecting *Edit properties*.

The navigation (hamburger) menu item *Order folders* in the setup can be used to manually order the folders.

<br />

<a name="faq52"></a>
**(52) Why does it take some time to reconnect to an account?**

There is no reliable way to know if an account connection was terminated gracefully or forcefully. Trying to reconnect to an account while the account connection was terminated forcefully too often can result in problems like [too many simultaneous connections](#user-content-faq23) or even the account being blocked. To prevent such problems, FairEmail waits 90 seconds until trying to reconnect again.

You can long press *Settings* in the navigation menu to reconnect immediately.

<br />

<a name="faq53"></a>
**(53) Can you stick the message action bar to the top/bottom?**

The message action bar works on a single message and the bottom action bar works on all the messages in the conversation. Since there is often more than one message in a conversation, this is not possible. Moreover, there are quite some message specific actions, like forwarding.

Moving the message action bar to the bottom of the message is visually not appealing because there is already a conversation action bar at the bottom of the screen.

Note that there are not many, if any, email apps that display a conversation as a list of expandable messages. This has a lot of advantages, but the also causes the need for message specific actions.

<br />

<a name="faq54"></a>
**~~(54) How do I use a namespace prefix?~~**

~~A namespace prefix is used to automatically remove the prefix providers sometimes add to folder names.~~

~~For example the Gmail spam folder is called:~~

```
[Gmail]/Spam
```

~~By setting the namespace prefix to *[Gmail]* FairEmail will automatically remove *[Gmail]/* from all folder names.~~

<br />

<a name="faq55"></a>
**(55) How can I mark all messages as read / move or delete all messages?**

You can use multiple select for this. Long press the first message, don't lift your finger and slide down to the last message. Then use the three dot action button to execute the desired action.

<br />

<a name="faq56"></a>
**(56) Can you add support for JMAP?**

There are almost no providers offering the [JMAP](https://jmap.io/) protocol, so it is not worth a lot of effort to add support for this to FairEmail.

<br />

<a name="faq57"></a>
**(57) Can I use HTML in signatures?**

Yes, you can use [HTML](https://en.wikipedia.org/wiki/HTML). In the signature editor you can switch to HTML mode via the three-dots menu.

Note that if you switch back to the text editor that not all HTML might be rendered as-is because the Android text editor is not able to render all HTML. Similarly, if you use the text editor, the HTML might be altered in unexpected ways.

If you want to use preformatted text, like [ASCII art](https://en.wikipedia.org/wiki/ASCII_art), you should wrap the text in a *pre* element, like this:

```
<pre>
  |\_/|
 / @ @ \
( > º < )
 `>>x<<´
 /  O  \
 </pre>
```

<br />

<a name="faq58"></a>
**(58) What does an open/closed email icon mean?**

The email icon in the folder list can be open (outlined) or closed (solid):

![External image](https://github.com/M66B/FairEmail/blob/master/images/baseline_mail_outline_black_48dp.png)

Message bodies and attachments are not downloaded by default.

![External image](https://github.com/M66B/FairEmail/blob/master/images/baseline_email_black_48dp.png)

Message bodies and attachments are downloaded by default.

<br />

<a name="faq59"></a>
**(59) Can original messages be opened in the browser?**

For security reasons the files with the original message texts are not accessible to other apps, so this is not possible. In theory the [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider) could be used to share these files, but even Google's Chrome cannot handle this.

<br />

<a name="faq60"></a>
**(60) Did you know ... ?**

* Sapevi che i messaggi stellati possono essere sincronizzati/mantenuti sempre? (abilitabile nelle impostazioni di ricezione)
* Sapevi che puoi premere a lungo l'icona 'scrivi messaggio' per andare alla cartella delle bozze?
* Sapevi che esiste un'opzione avanzata per contrassegnare i messaggi come letti quando sono spostati? (anche archiviare e cestinare è spostare)
* Sapevi che puoi selezionare il testo (o un indirizzo email) in ogni app sulle versioni recenti di Android e farle cercare a FairEmail?
* Sapevi che FairEmail ha una modalità tablet? Ruota il tuo dispositivo in modalità orizzontale e le conversazioni saranno aperte in una seconda colonna in caso di abbastanza spazio su schermo.
* Sapevi che puoi premere a lungo un modello di risposta per creare una bozza dal modello?
* Sapevi che puoi tenere premuto a lungo, trattenere e scorrere per selezionare un intervallo di messaggi?
* Sapevi che puoi provare a inviare i messaggi usando tira in basso per ricaricare nella casella della posta in uscita?
* Sapevi che puoi scorrere a sinistra o destra una conversazione per andare alla successiva o precedente?
* Sapevi che puoi toccare su un'immagine per vedere da dove verrà scaricata?
* Sapevi che puoi tenere premuto a lungo l'icona della cartella nella barra di azione per selezionare un profilo?
* Sapevi che puoi tenere premuto a lungo l'icona della stella nel thread della conversazione per impostare una stella colorata?
* Sapevi che puoi aprire il pannello di navigazione scorrendo da sinistra, anche visualizzando una conversazione?
* Sapevi che puoi tenere premuto a lungo l'icona della persona per mostrare/nascondere i campi CC/BCC e ricordare lo stato di visibilità per la prossima volta?
* Sapevi che puoi inserire gli indirizzi email di un gruppo di contatti di Android tramite il menu di trabocco a tre puntini?
* Sapevi che se selezioni il testo e clicchi rispondi, solo il testo selezionato sarà citato?
* Sapevi che puoi tenere premuto a lungo le icone del cestino (sia nella barra di azione in fondo che nel messaggio) per eliminare permanentemente un messaggio o una conversazione? (versione 1.1368+)
* Sapevi che puoi tenere premuto a lungo l'azione di invio per mostrare la finestra di invio, anche se è disabilitata?
* Sapevi che puoi tenere premuto a lungo l'icona di schermo intero per mostrare solo il testo originale del messaggio?

<br />

<a name="faq61"></a>
**(61) Why are some messages shown dimmed?**

Messages shown dimmed (grayed) are locally moved messages for which the move is not confirmed by the server yet. This can happen when there is no connection to the server or the account (yet). These messages will be synchronized after a connection to the server and the account has been made or, if this never happens, will be deleted if they are too old to be synchronized.

You might need to manually synchronize the folder, for example by pulling down.

You can view these messages, but you cannot move these messages again until the previous move has been confirmed.

Pending [operations](#user-content-faq3) are shown in the operations view accessible from the main navigation menu.

<br />

<a name="faq62"></a>
**(62) Which authentication methods are supported?**

The following authentication methods are supported and used in this order:

* LOGIN
* PLAIN
* CRAM-MD5
* XOAUTH2 ([Gmail](https://developers.google.com/gmail/imap/xoauth2-protocol), [Yandex](https://tech.yandex.com/oauth/))
* NTLM (non testato)

SASL authentication methods, besides CRAM-MD5, are not supported because [JavaMail for Android](https://javaee.github.io/javamail/Android) does not support SASL authentication.

If your provider requires an unsupported authentication method, you'll likely get the error message *authentication failed*.

[Server Name Indication](https://en.wikipedia.org/wiki/Server_Name_Indication) is supported by [all supported Android versions](https://developer.android.com/training/articles/security-ssl).

<br />

<a name="faq63"></a>
**(63) How are images resized for displaying on screens?**

Large inline or attached [PNG](https://en.wikipedia.org/wiki/Portable_Network_Graphics) and [JPEG](https://en.wikipedia.org/wiki/JPEG) images will automatically be resized for displaying on screens. This is because email messages are limited in size, depending on the provider mostly between 10 and 50 MB. Images will by default be resized to a maximum width and height of about 1440 pixels and saved with a compression ratio of 90 %. Images are scaled down using whole number factors to reduce memory usage and to retain image quality. Automatically resizing of inline and/or attached images and the maximum target image size can be configured in the send settings.

If you want to resize images on a case-by-case basis, you can use [Send Reduced](https://f-droid.org/en/packages/mobi.omegacentauri.SendReduced/) or a similar app.

<br />

<a name="faq64"></a>
**~~(64) Can you add custom actions for swipe left/right?~~**

~~The most natural thing to do when swiping a list entry left or right is to remove the entry from the list.~~ ~~The most natural action in the context of an email app is moving the message out of the folder to another folder.~~ ~~You can select the folder to move to in the account settings.~~

~~Other actions, like marking messages read and snoozing messages are available via multiple selection.~~ ~~You can long press a message to start multiple selection. See also [this question](#user-content-faq55).~~

~~Swiping left or right to mark a message read or unread is unnatural because the message first goes away and later comes back in a different shape.~~ ~~Note that there is an advanced option to mark messages automatically read on moving,~~ ~~which is in most cases a perfect replacement for the sequence mark read and move to some folder.~~ ~~You can also mark messages read from new message notifications.~~

~~If you want to read a message later, you can hide it until a specific time by using the *snooze* menu.~~

<br />

<a name="faq65"></a>
**(65) Why are some attachments shown dimmed?**

Inline (image) attachments are shown dimmed. [Inline attachments](https://tools.ietf.org/html/rfc2183) are supposed to be downloaded and shown automatically, but since FairEmail doesn't always download attachments automatically, see also [this FAQ](#user-content-faq40), FairEmail shows all attachment types. To distinguish inline and regular attachments, inline attachments are shown dimmed.

<br />

<a name="faq66"></a>
**(66) Is FairEmail available in the Google Play Family Library?**

The price of the few pro features is too low, lower than the price of most similar apps, and there are [too many fees and taxes](#user-content-faq19), to justify making FairEmail available in the [Google Play Family Library](https://support.google.com/googleone/answer/7007852). Note that Google promotes the Family libray, but lets developers pay for it.

<br />

<a name="faq67"></a>
**(67) How can I snooze conversations?**

Multiple select one of more conversations (long press to start multiple selecting), tap the three dot button and select *Snooze ...*. Alternatively, in the expanded message view use *Snooze ...* in the message three-dots 'more' menu or the timelapse action in the bottom action bar. Select the time the conversation(s) should snooze and confirm by tapping OK. The conversations will be hidden for the selected time and shown again afterwards. You will receive a new message notification as reminder.

It is also possible to snooze messages with [a rule](#user-content-faq71).

You can show snoozed messages by unchecking *Filter out* > *Hidden* in the three dot overflow menu.

You can tap on the small snooze icon to see until when a conversation is snoozed.

By selecting a zero snooze duration you can cancel snoozing.

<br />

<a name="faq68"></a>
**~~(68) Why can Adobe Acrobat reader not open PDF attachments / Microsoft apps not open attached documents?~~**

~~Adobe Acrobat reader and Microsoft apps still expects full access to all stored files,~~ ~~while apps should use the [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider) since Android KitKat (2013)~~ ~~to have access to actively shared files only. This is for privacy and security reasons.~~

~~You can workaround this by saving the attachment and opening it from the Adobe Acrobat reader / Microsoft app,~~ ~~but you are advised to install an up-to-date and preferably open source PDF reader / document viewer,~~ ~~for example one listed [here](https://github.com/offa/android-foss#-document--pdf-viewer).~~

<br />

<a name="faq69"></a>
**(69) Can you add auto scroll up on new message?**

The message list is automatically scrolled up when navigating from a new message notification or after a manual refresh. Always automatically scrolling up on arrival of new messages would interfere with your own scrolling, but if you like you can enable this in the settings.

<br />

<a name="faq70"></a>
**(70) When will messages be auto expanded?**

When navigation to a conversation one message will be expanded if:

* C'è solo un messaggio nella conversazione
* C'è esattamente un messaggio non letto nella conversazione

There is one exception: the message was not downloaded yet and the message is too large to download automatically on a metered (mobile) connection. You can set or disable the maximum message size on the 'connection' settings tab.

Duplicate (archived) messages, trashed messages and draft messages are not counted.

Messages will automatically be marked read on expanding, unless this was disabled in the individual account settings.

<br />

<a name="faq71"></a>
**(71) How do I use filter rules?**

You can edit filter rules by long pressing a folder in the folder list of an account (tap the account name in the navigation/side menu).

New rules will be applied to new messages received in the folder, not to existing messages. You can check the rule and apply the rule to existing messages or, alternatively, long press the rule in the rule list and select *Execute now*.

You'll need to give a rule a name and you'll need to define the order in which a rule should be executed relative to other rules.

You can disable a rule and you can stop processing other rules after a rule has been executed.

The following rule conditions are available:

* Mittente contiene
* Destinatario contiene
* Oggetto contiene
* Ha allegati
* Intestazione contiene
* Giorno/ora tra

All the conditions of a rule need to be true for the rule action to be executed. All conditions are optional, but there needs to be at least one condition, to prevent matching all messages. If you want to match all senders or all recipients, you can just use the @ character as condition because all email addresses will contain this character.

Note that email addresses are formatted like this:

`
"Somebody" <somebody@example.org>`

You can use multiple rules, possibly with a *stop processing*, for an *or* or a *not* condition.

Matching is not case sensitive, unless you use [regular expressions](https://en.wikipedia.org/wiki/Regular_expression). Please see [here](https://developer.android.com/reference/java/util/regex/Pattern) for the documentation of Java regular expressions. You can test a regex [here](https://regexr.com/).

Note that a regular expression supports an *or* operator, so if you want to match multiple senders, you can do this:

`
.*alice@example\.org.*|.*bob@example\.org.*|.*carol@example\.org.*`

Note that [dot all mode](https://developer.android.com/reference/java/util/regex/Pattern#DOTALL) is enabled to be able to match [unfolded headers](https://tools.ietf.org/html/rfc2822#section-3.2.3).

You can select one of these actions to apply to matching messages:

* Nessun'azione (utile per *non*)
* Segna come letto
* Segna come non letto
* Nascondi
* Sopprimi notifica
* Posticipa
* Aggiungi stella
* Imposta importanza (priorità locale)
* Aggiungi parola chiave
* Sposta
* Copia (Gmail: etichetta)
* Rispondi/inoltra (con modello)
* Sintesi vocale (mittente e oggetto)
* Automatizzazione (Tasker, etc.)

Rules are applied directly after the message header has been fetched, but before the message text has been downloaded, so it is not possible to apply conditions to the message text. Note that large message texts are downloaded on demand on a metered connection to save on data usage.

If you want to forward a message, consider to use the move action instead. This will be more reliable than forwarding as well because forwarded messages might be considered as spam.

Since message headers are not downloaded and stored by default to save on battery and data usage and to save storage space it is not possible to preview which messages would match a header rule condition.

Some common header conditions (regex):

* *.&ast;Auto-Submitted:.&ast;* [RFC3834](https://tools.ietf.org/html/rfc3834)
* *.&ast;Content-Type: multipart/report.&ast;* [RFC3462](https://tools.ietf.org/html/rfc3462)

In the three-dots *more* message menu there is an item to create a rule for a received message with the most common conditions filled in.

The POP3 protocol does not support setting keywords and moving or copying messages.

Using rules is a pro feature.

<br />

<a name="faq72"></a>
**(72) What are primary accounts/identities?**

The primary account is used when the account is ambiguous, for example when starting a new draft from the unified inbox.

Similarly, the primary identity of an account is used when the identity is ambiguous.

There can be just one primary account and there can be just one primary identity per account.

<br />

<a name="faq73"></a>
**(73) Is moving messages across accounts safe/efficient?**

Moving messages across accounts is safe because the raw, original messages will be downloaded and moved and because the source messages will be deleted only after the target messages have been added

Batch moving messages across accounts is efficient if both the source folder and target folder are set to synchronize, else FairEmail needs to connect to the folder(s) for each message.

<br />

<a name="faq74"></a>
**(74) Why do I see duplicate messages?**

Some providers, notably Gmail, list all messages in all folders, except trashed messages, in the archive (all messages) folder too. FairEmail shows all these messages in a non obtrusive way to indicate that these messages are in fact the same message.

Gmail allows one message to have multiple labels, which are presented to FairEmail as folders. This means that messages with multiple labels will be shown multiple times as well.

<br />

<a name="faq75"></a>
**(75) Can you make an iOS, Windows, Linux, etc version?**

A lot of knowledge and experience is required to successfully develop an app for a specific platform, which is why I develop apps for Android only.

<br />

<a name="faq76"></a>
**(76) What does 'Clear local messages' do?**

The folder menu *Clear local messages* removes messages from the device which are present on the server too. It does not delete messages from the server. This can be useful after changing the folder settings to not download the message content (text and attachments), for example to save space.

<br />

<a name="faq77"></a>
**(77) Why are messages sometimes shown with a small delay?**

Depending on the speed of your device (processor speed and maybe even more memory speed) messages might be displayed with a small delay. FairEmail is designed to dynamically handle a large number of messages without running out of memory. This means that messages needs to be read from a database and that this database needs to be watched for changes, both of which might cause small delays.

Some convenience features, like grouping messages to display conversation threads and determining the previous/next message, take a little extra time. Note that there is no *the* next message because in the meantime a new message might have been arrived.

When comparing the speed of FairEmail with similar apps this should be part of the comparison. It is easy to write a similar, faster app which just displays a lineair list of messages while possible using too much memory, but it is not so easy to properly manage resource usage and to offer more advanced features like conversation threading.

FairEmail is based on the state-of-the-art [Android architecture components](https://developer.android.com/topic/libraries/architecture/), so there is little room for performance improvements.

<br />

<a name="faq78"></a>
**(78) How do I use schedules?**

In the receive settings you can enable scheduling and set a time period and the days of the week *when* messages should be *received*. Note that an end time equal to or earlier than the start time is considered to be 24 hours later.

Automation, see below, can be used for more advanced schedules, like for example multiple synchronization periods per day or different synchronization periods for different days.

It is possible to install FairEmail in multiple user profiles, for example a personal and a work profile, and to configure FairEmail differently in each profile, which is another possibility to have different synchronization schedules and to synchronize a different set of accounts.

It is also possible to create [filter rules](#user-content-faq71) with a time condition and to snooze messages until the end time of the time condition. This way it is possible to *snooze* business related messages until the start of the business hours. This also means that the messages will be on your device for when there is (temporarily) no internet connection.

Note that recent Android versions allow overriding DND (Do Not Disturb) per notification channel and per app, which could be used to (not) silence specific (business) notifications. Please [see here](https://support.google.com/android/answer/9069335) for more information.

For more complex schemes you could set one or more accounts to manual synchronization and send this command to FairEmail to check for new messages:

```
(adb shell) am startservice -a eu.faircode.email.POLL
```

For a specific account:

```
(adb shell) am startservice -a eu.faircode.email.POLL --es account Gmail
```

You can also automate turning receiving messages on and off by sending these commands to FairEmail:

```
(adb shell) am startservice -a eu.faircode.email.ENABLE
(adb shell) am startservice -a eu.faircode.email.DISABLE
```

To enable/disable a specific account:

```
(adb shell) am startservice -a eu.faircode.email.ENABLE --es account Gmail
(adb shell) am startservice -a eu.faircode.email.DISABLE --es account Gmail
```

Note that disabling an account will hide the account and all associated folders and messages.

You can automatically send commands with for example [Tasker](https://tasker.joaoapps.com/userguide/en/intents.html):

```
Nuova attività: Qualcosa di riconoscibile
Categoria d'Azione: Intento Varie/Invia
Azione: eu.faircode.email.ENABLE
Target: Servizio
```

To enable/disable an account with the name *Gmail*:

```
Extra: account:Gmail
```

Account names are case sensitive.

Scheduling is a pro feature.

<br />

<a name="faq79"></a>
**(79) How do I use synchronize on demand (manual)?**

Normally, FairEmail maintains a connection to the configured email servers whenever possible to receive messages in real-time. If you don't want this, for example to be not disturbed or to save on battery usage, just disable receiving in the receive settings. This will stop the background service which takes care of automatic synchronization and will remove the associated status bar notification.

You can also enable *Synchronize manually* in the advanced account settings if you want to manually synchronize specific accounts only.

You can use pull-down-to-refresh in a message list or use the folder menu *Synchronize now* to manually synchronize messages.

If you want to synchronize some or all folders of an account manually, just disable synchronization for the folders (but not of the account).

You'll likely want to disabled [browse on server](#user-content-faq24) too.

<br />

<a name="faq80"></a>
**~~(80) How do I fix the error 'Unable to load BODYSTRUCTURE' ?~~**

~~The error message *Unable to load BODYSTRUCTURE* is caused by bugs in the email server,~~ ~~see [here](https://javaee.github.io/javamail/FAQ#imapserverbug) for more details.~~

~~FairEmail already tries to workaround these bugs, but if this fail you'll need to ask for support from your provider.~~

<br />

<a name="faq81"></a>
**~~(81) Can you make the background of the original message dark in the dark theme?~~**

~~The original message is shown as the sender has sent it, including all colors.~~ ~~Changing the background color would not only make the original view not original anymore, it can also result in unreadable messages.~~

<br />

<a name="faq82"></a>
**(82) What is a tracking image?**

Please see [here](https://en.wikipedia.org/wiki/Web_beacon) about what a tracking image exactly is. In short tracking images keep track if you opened a message.

FairEmail will in most cases automatically recognize tracking images and replace them by this icon:

![External image](https://github.com/M66B/FairEmail/blob/master/images/baseline_my_location_black_48dp.png)

Automatic recognition of tracking images can be disabled in the privacy settings.

<br />

<a name="faq84"></a>
**(84) What are local contacts for?**

Local contact information is based on names and addresses found in incoming and outgoing messages.

The main use of the local contacts storage is to offer auto completion when no contacts permission has been granted to FairEmail.

Another use is to generate [shortcuts](#user-content-faq31) on recent Android versions to quickly send a message to frequently contacted people. This is also why the number of times contacted and the last time contacted is being recorded and why you can make a contact a favorite or exclude it from favorites by long pressing it.

The list of contacts is sorted on number of times contacted and the last time contacted.

By default only names and addresses to whom you send messages to will be recorded. You can change this in the send settings.

<br />

<a name="faq85"></a>
**(85) Why is an identity not available?**

An identity is available for sending a new message or replying or forwarding an existing message only if:

* l'identità è impostata per la sincronizzazione (invio messaggi)
* il profilo associato è impostato per la sincronizzazione (ricezione messaggi)
* il profilo associato ha una cartella delle bozze

FairEmail will try to select the best identity based on the *to* address of the message replied to / being forwarded.

<br />

<a name="faq86"></a>
**~~(86) What are 'extra privacy features'?~~**

~~The advanced option *extra privacy features* enables:~~

* ~~La ricerca del proprietario di un indirizzo IP di un link~~
* ~~Il rilevamento e la rimozione delle [immagini di monitoraggio](#user-content-faq82)~~

<br />

<a name="faq87"></a>
**(87) What does 'invalid credentials' mean?**

The error message *invalid credentials* means either that the user name and/or password is incorrect, for example because the password was changed or expired, or that the account authorization has expired.

If the password is incorrect/expired, you will have to update the password in the account and/or identity settings.

If the account authorization has expired, you will have to select the account again. You will likely need to save the associated identity again as well.

<br />

<a name="faq88"></a>
**(88) How can I use a Yahoo, AOL or Sky account?**

The preferred way to set up a Yahoo account is by using the quick setup wizard, which will use OAuth instead of a password and is therefore safer (and easier as well).

To authorize a Yahoo, AOL, or Sky account you will need to create an app password. For instructions, please see here:

* [per Yahoo](https://help.yahoo.com/kb/generate-third-party-passwords-sln15241.html)
* [per AOL](https://help.aol.com/articles/Create-and-manage-app-password)
* [per Sky](https://www.sky.com/help/articles/getting-started-with-sky-yahoo-mail) (sotto *Altre app di email*)

Please see [this FAQ](#user-content-faq111) about OAuth support.

Note that Yahoo, AOL, and Sky do not support standard push messages. The Yahoo email app uses a proprietary, undocumented protocol for push messages.

Push messages require [IMAP IDLE](https://en.wikipedia.org/wiki/IMAP_IDLE) and the Yahoo email server does not report IDLE as capability:

```
Y1 CAPABILITY
* CAPABILITY IMAP4rev1 ID MOVE NAMESPACE XYMHIGHESTMODSEQ UIDPLUS LITERAL+ CHILDREN X-MSG-EXT UNSELECT OBJECTID
Y1 OK CAPABILITY completed
```

<br />

<a name="faq89"></a>
**(89) How can I send plain text only messages?**

By default FairEmail sends each message both as plain text and as HTML formatted text because almost every receiver expects formatted messages these days. If you want/need to send plain text messages only, you can enable this in the advanced identity options. You might want to create a new identity for this if you want/need to select sending plain text messages on a case-by-case basis.

<br />

<a name="faq90"></a>
**(90) Why are some texts linked while not being a link?**

FairEmail will automatically link not linked web links (http and https) and not linked email addresses (mailto) for your convenience. However, texts and links are not easily distinguished, especially not with lots of [top level domains](https://en.wikipedia.org/wiki/List_of_Internet_top-level_domains) being words. This is why texts with dots are sometimes incorrectly recognized as links, which is better than not recognizing some links.

Links for the tel, geo, rtsp and xmpp protocols will be recognized too, but links for less usual or less safe protocols like telnet and ftp will not be recognized. The regex to recognize links is already *very* complex and adding more protocols will make it only slower and possibly cause errors.

Note that original messages are shown exactly as they are, which means also that links are not automatically added.

<br />

<a name="faq91"></a>
**~~(91) Can you add periodical synchronization to save battery power?~~**

~~Synchronizing messages is an expensive proces because the local and remote messages need to be compared,~~ ~~so periodically synchronizing messages will not result in saving battery power, more likely the contrary.~~

~~See [this FAQ](#user-content-faq39) about optimizing battery usage.~~

<br />

<a name="faq92"></a>
**(92) Can you add spam filtering, verification of the DKIM signature and SPF authorization?**

Spam filtering, verification of the [DKIM](https://en.wikipedia.org/wiki/DomainKeys_Identified_Mail) signature and [SPF](https://en.wikipedia.org/wiki/Sender_Policy_Framework) authorization is a task of email servers, not of an email client. Servers generally have more memory and computing power, so they are much better suited to this task than battery-powered devices. Also, you'll want spam filtered for all your email clients, possibly including web email, not just one email client. Moreover, email servers have access to information, like the IP address, etc of the connecting server, which an email client has no access to.

Spam filtering based on message headers might have been feasible, but unfortunately this technique is [patented by Microsoft](https://patents.google.com/patent/US7543076).

Recent versions of FairEmail can filter spam to a certain extend using a message classifier. Please see [this FAQ](#user-content-faq163) for more information about this.

Of course you can report messages as spam with FairEmail, which will move the reported messages to the spam folder and train the spam filter of the provider, which is how it is supposed to work. This can be done automatically with [filter rules](#user-content-faq71) too. Blocking the sender will create a filter rule to automatically move future messages of the same sender into the spam folder.

Note that the POP3 protocol gives access to the inbox only. So, it is won't be possible to report spam for POP3 accounts.

Note that you should not delete spam messages, also not from the spam folder, because the email server uses the messages in the spam folder to "learn" what spam messages are.

If you receive a lot of spam messages in your inbox, the best you can do is to contact the email provider to ask if spam filtering can be improved.

Also, FairEmail can show a small red warning flag when DKIM, SPF or [DMARC](https://en.wikipedia.org/wiki/DMARC) authentication failed on the receiving server. You can enable/disable [authentication verification](https://en.wikipedia.org/wiki/Email_authentication) in the display settings.

FairEmail can show a warning flag too if the domain name of the (reply) email address of the sender does not define an MX record pointing to an email server. This can be enabled in the receive settings. Be aware that this will slow down synchronization of messages significantly.

If legitimate messages are failing authentication, you should notify the sender because this will result in a high risk of messages ending up in the spam folder. Moreover, without proper authentication there is a risk the sender will be impersonated. The sender might use [this tool](https://www.mail-tester.com/) to check authentication and other things.

<br />

<a name="faq93"></a>
**(93) Can you allow installation/data storage on external storage media (sdcard)?**

FairEmail uses services and alarms, provides widgets and listens for the boot completed event to be started on device start, so it is not possible to store the app on external storage media, like an sdcard. See also [here](https://developer.android.com/guide/topics/data/install-location).

Messages, attachments, etc stored on external storage media, like an sdcard, can be accessed by other apps and is therefore not safe. See [here](https://developer.android.com/training/data-storage) for the details.

When needed you can save (raw) messages via the three-dots menu just above the message text and save attachments by tapping on the floppy icon.

If you need to save on storage space, you can limit the number of days messages are being synchronized and kept for. You can change these settings by long pressing a folder in the folder list and selecting *Edit properties*.

<br />

<a name="faq94"></a>
**(94) What does the red/orange stripe at the end of the header mean?**

The red/orange stripe at the left side of the header means that the DKIM, SPF or DMARC authentication failed. See also [this FAQ](#user-content-faq92).

<br />

<a name="faq95"></a>
**(95) Why are not all apps shown when selecting an attachment or image?**

For privacy and security reasons FairEmail does not have permissions to directly access files, instead the Storage Access Framework, available and recommended since Android 4.4 KitKat (released in 2013), is used to select files.

If an app is listed depends on if the app implements a [document provider](https://developer.android.com/guide/topics/providers/document-provider). If the app is not listed, you might need to ask the developer of the app to add support for the Storage Access Framework.

Android Q will make it harder and maybe even impossible to directly access files, see [here](https://developer.android.com/preview/privacy/scoped-storage) and [here](https://www.xda-developers.com/android-q-storage-access-framework-scoped-storage/) for more details.

<br />

<a name="faq96"></a>
**(96) Where can I find the IMAP and SMTP settings?**

The IMAP settings are part of the (custom) account settings and the SMTP settings are part of the identity settings.

<br />

<a name="faq97"></a>
**(97) What is 'cleanup' ?**

About each four hours FairEmail runs a cleanup job that:

* Rimuove i testi dei messaggi vecchi
* Rimuove gli allegati dei messaggi vecchi
* Rimuove immagini di vecchi file
* Rimuove contatti locali vecchi
* Rimuove le vecchie voci del registro

Note that the cleanup job will only run when the synchronize service is active.

<br />

<a name="faq98"></a>
**(98) Why can I still pick contacts after revoking contacts permissions?**

After revoking contacts permissions Android does not allow FairEmail access to your contacts anymore. However, picking contacts is delegated to and done by Android and not by FairEmail, so this will still be possible without contacts permissions.

<br />

<a name="faq99"></a>
**(99) Can you add a rich text or markdown editor?**

FairEmail provides common text formatting (bold, italic, underline, text size and color) via a toolbar that appears after selecting some text.

A [Rich text](https://en.wikipedia.org/wiki/Formatted_text) or [Markdown](https://en.wikipedia.org/wiki/Markdown) editor would not be used by many people on a small mobile device and, more important, Android doesn't support a rich text editor and most rich text editor open source projects are abandoned. See [here](https://forum.xda-developers.com/showpost.php?p=79061829&postcount=4919) for some more details about this.

<br />

<a name="faq100"></a>
**(100) How can I synchronize Gmail categories?**

You can synchronize Gmail categories by creating filters to label categorized messages:

* Crea un nuovo filtro tramite Gmail > Impostazioni (rotellina) > Filtri e Indirizzi Bloccati > Crea un nuovo filtro
* Inserisci una ricerca della categoria (vedi sotto) nel campo *Contiene le parole* e clicca *Crea filtro*
* Spunta *Applica l'etichetta* e selezionane una e clicca *Crea filtro*

Possible categories:

```
categoria: social
categoria: aggiornamenti
categoria: forum
categoria: promozioni
```

Unfortunately, this is not possible for snoozed messages folder.

You can use *Force sync* in the three-dots menu of the unified inbox to let FairEmail synchronize the folder list again and you can long press the folders to enable synchronization.

<br />

<a name="faq101"></a>
**(101) What does the blue/orange dot at the bottom of the conversations mean?**

The dot shows the relative position of the conversation in the message list. The dot will be show orange when the conversation is the first or last in the message list, else it will be blue. The dot is meant as an aid when swiping left/right to go to the previous/next conversation.

The dot is disabled by default and can be enabled with the display settings *Show relative conversation position with a dot*.

<br />

<a name="faq102"></a>
**(102) How can I enable auto rotation of images?**

Images will automatically be rotated when automatic resizing of images is enabled in the settings (enabled by default). However, automatic rotating depends on the [Exif](https://en.wikipedia.org/wiki/Exif) information to be present and to be correct, which is not always the case. Particularly not when taking a photo with a camara app from FairEmail.

Note that only [JPEG](https://en.wikipedia.org/wiki/JPEG) and [PNG](https://en.wikipedia.org/wiki/Portable_Network_Graphics) images can contain Exif information.

<br />

<a name="faq104"></a>
**(104) What do I need to know about error reporting?**

* Le segnalazioni degli errori aiuteranno a migliorare FairEmail
* La segnalazione degli errori è opzionale e su adesione
* La segnalazione degli errori può essere abilitata/disabilitata nelle impostazioni, sezione varie
* Le segnalazioni degli errori saranno inviate automaticamente e anonimamente a [Bugsnag](https://www.bugsnag.com/)
* Bugsnag per Android è [open-source](https://github.com/bugsnag/bugsnag-android)
* Vedi [qui](https://docs.bugsnag.com/platforms/android/automatically-captured-data/) quali dati saranno inviati in caso di errori
* Vedi [qui](https://docs.bugsnag.com/legal/privacy-policy/) la politica della privacy di Bugsnag
* Le segnalazioni di errori saranno inviate a *sessions.bugsnag.com:443* e *notify.bugsnag.com:443*

<br />

<a name="faq105"></a>
**(105) How does the roam-like-at-home option work?**

FairEmail will check if the country code of the SIM card and the country code of the network are in the [EU roam-like-at-home countries](https://en.wikipedia.org/wiki/European_Union_roaming_regulations#Territorial_extent) and assumes no roaming if the country codes are equal and the advanced roam-like-at-home option is enabled.

So, you don't have to disable this option if you don't have an EU SIM or are not connected to an EU network.

<br />

<a name="faq106"></a>
**(106) Which launchers can show a badge count with the number of unread messages?**

Please [see here](https://github.com/leolin310148/ShortcutBadger#supported-launchers) for a list of launchers which can show the number of unread messages.

Note that the notification setting *Show launcher icon with number of new messages* needs to be enabled (default enabled).

Only *new* unread messages in folders set to show new message notifications will be counted, so messages marked unread again and messages in folders set to not show new message notification will not be counted.

Depending on what you want, the notification settings *Let the number of new messages match the number of notifications* needs to be enabled or disabled.

This feature depends on support of your launcher. FairEmail merely 'broadcasts' the number of unread messages using the ShortcutBadger library. If it doesn't work, this cannot be fixed by changes in FairEmail.

Some launchers display '1' for [the monitoring notification](#user-content-faq2), despite FairEmail explicitly requesting not to show a badge for this notification. This could be caused by a bug in the launcher app or in your Android version. Please double check if the notification dot is disabled for the receive (service) notification channel. You can go to the right notification channel settings via the notification settings of FairEmail. This might not be obvious, but you can tap on the channel name for more settings.

Note that Tesla Unread is [not supported anymore](https://forum.xda-developers.com/android/general/bad-news-tesla-unread-devoloper-t3920415).

FairEmail does send a new message count intent as well:

```
eu.faircode.email.NEW_MESSAGE_COUNT
```

The number of new, unread messages will be in an integer "*count*" parameter.

<br />

<a name="faq107"></a>
**(107) How do I use colored stars?**

You can set a colored star via the *more* message menu, via multiple selection (started by long pressing a message), by long pressing a star in a conversation or automatically by using [rules](#user-content-faq71).

You need to know that colored stars are not supported by the IMAP protocol and can therefore not be synchronized to an email server. This means that colored stars will not be visible in other email clients and will be lost on downloading messages again. However, the stars (without color) will be synchronized and will be visible in other email clients, when supported.

Some email clients use IMAP keywords for colors. However, not all servers support IMAP keywords and besides that there are no standard keywords for colors.

<br />

<a name="faq108"></a>
**~~(108) Can you add permanently delete messages from any folder?~~**

~~When you delete messages from a folder the messages will be moved to the trash folder, so you have a chance to restore the messages.~~ ~~You can permanently delete messages from the trash folder.~~ ~~Permanently delete messages from other folders would defeat the purpose of the trash folder, so this will not be added.~~

<br />

<a name="faq109"></a>
**~~(109) Why is 'select account' available in official versions only?~~**

~~Using *select account* to select and authorize Google accounts require special permission from Google for security and privacy reasons.~~ ~~This special permission can only be acquired for apps a developer manages and is responsible for.~~ ~~Third party builds, like the F-Droid builds, are managed by third parties and are the responsibility of these third parties.~~ ~~So, only these third parties can acquire the required permission from Google.~~ ~~Since these third parties do not actually support FairEmail, they are most likely not going to request the required permission.~~

~~You can solve this in two ways:~~

* ~~Passa alla versione ufficiale di FairEmail, vedi [qui](https://github.com/M66B/FairEmail/blob/master/README.md#downloads) le opzioni~~
* ~~Usa le password specifiche dell'app, vedi [questa FAQ](#user-content-faq6)~~

~~Using *select account* in third party builds is not possible in recent versions anymore.~~ ~~In older versions this was possible, but it will now result in the error *UNREGISTERED_ON_API_CONSOLE*.~~

<br />

<a name="faq110"></a>
**(110) Why are (some) messages empty and/or attachments corrupt?**

Empty messages and/or corrupt attachments are probably being caused by a bug in the server software. Older Microsoft Exchange software is known to cause this problem. Mostly you can workaround this by disabling *Partial fetch* in the advanced account settings:

Setup > Step 1 > Manage > Tap account > Tap advanced > Partial fetch > uncheck

After disabling this setting, you can use the message 'more' (three dots) menu to 'resync' empty messages. Alternatively, you can *Delete local messages* by long pressing the folder(s) in the folder list and synchronize all messages again.

Disabling *Partial fetch* will result in more memory usage.

<br />

<a name="faq111"></a>
**(111) Is OAuth supported?**

OAuth for Gmail is supported via the quick setup wizard. The Android account manager will be used to fetch and refresh OAuth tokens for selected on-device accounts. OAuth for non on-device accounts is not supported because Google requires a [yearly security audit](https://support.google.com/cloud/answer/9110914) ($15,000 to $75,000) for this. You can read more about this [here](https://www.theregister.com/2019/02/11/google_gmail_developer/).

OAuth for Yandex and Yahoo is supported via the quick setup wizard.

OAuth for Office 365 accounts is supported, but Microsoft does not offer OAuth for Outlook, Live and Hotmail accounts (yet?).

<br />

<a name="faq112"></a>
**(112) Which email provider do you recommend?**

FairEmail is an email client only, so you need to bring your own email address.

There are plenty of email providers to choose from. Which email provider is best for you depends on your wishes/requirements. Please see the websites of [Restore privacy](https://restoreprivacy.com/secure-email/) or [Privacy Tools](https://www.privacytools.io/providers/email/) for a list of privacy oriented email providers with advantages and disadvantages.

Some providers, like ProtonMail, Tutanota, use proprietary email protocols, which make it impossible to use third party email apps. Please see [this FAQ](#user-content-faq129) for more information.

Using your own (custom) domain name, which is supported by most email providers, will make it easier to switch to another email provider.

<br />

<a name="faq113"></a>
**(113) How does biometric authentication work?**

If your device has a biometric sensor, for example a fingerprint sensor, you can enable/disable biometric authentication in the navigation (hamburger) menu of the setup screen. When enabled FairEmail will require biometric authentication after a period of inactivity or after the screen has been turned off while FairEmail was running. Activity is navigation within FairEmail, for example opening a conversation thread. The inactivity period duration can be configured in the miscellaneous settings. When biometric authentication is enabled new message notifications will not show any content and FairEmail won't be visible on the Android recents screen.

Biometric authentication is meant to prevent others from seeing your messages only. FairEmail relies on device encryption for data encryption, see also [this FAQ](#user-content-faq37).

Biometric authentication is a pro feature.

<br />

<a name="faq114"></a>
**(114) Can you add an import for the settings of other email apps?**

The format of the settings files of most other email apps is not documented, so this is difficult. Sometimes it is possible to reverse engineer the format, but as soon as the settings format changes things will break. Also, settings are often incompatible. For example, FairEmail has unlike most other email apps settings for the number of days to synchronize messages for and for the number of days to keep messages for, mainly to save on battery usage. Moreover, setting up an account/identity with the quick setup is simple, so it is not really worth the effort.

<br />

<a name="faq115"></a>
**(115) Can you add email address chips?**

Email address [chips](https://material.io/design/components/chips.html) look nice, but cannot be edited, which is quite inconvenient when you made a typo in an email address.

Note that FairEmail will select the address only when long pressing an address, which makes it easy to delete an address.

Chips are not suitable for showing in a list and since the message header in a list should look similar to the message header of the message view it is not an option to use chips for viewing messages.

Reverted [commit](https://github.com/M66B/FairEmail/commit/2c80c25b8aa75af2287f471b882ec87d5a5a5015).

<br />

<a name="faq116"></a>
**~~(116) How can I show images in messages from trusted senders by default?~~**

~~You can show images in messages from trusted senders by default by enabled the display setting *Automatically show images for known contacts*.~~

~~Contacts in the Android contacts list are considered to be known and trusted,~~ ~~unless the contact is in the group / has the label '*Untrusted*' (case insensitive).~~

<br />

<a name="faq38"></a>
<a name="faq117"></a>
**(117) Can you help me restore my purchase?**

Google manages all purchases, so as a developer I have little control over purchases. So, basically the only thing I can do, is give some advice:

* Assicurati di avere una connessione attiva e funzionante a internet
* Assicurati di essere connesso con il giusto profilo di Google e che non ci sia niente di sbagliato con il tuo profilo di Google
* Assicurati di aver installato FairEmail tramite il giusto profilo Google se hai diversi profili Google configurati sul tuo dispositivo
* Assicurati che l'app del Play Store sia aggiornata, sei pregato di [vedere qui](https://support.google.com/googleplay/answer/1050566?hl=en)
* Apri l'app del Play Store e attendi almeno un minuto per dargli tempo di sincronizzarsi con i server di Google
* Apri FairEmail e naviga alla schermata delle funzionalità pro per far controllare a FairEmail gli acquisti; talvolta aiuta toccare il pulsante *compra*

You can also try to clear the cache of the Play store app via the Android apps settings. Restarting the device might be necessary to let the Play store recognize the purchase correctly.

Note that:

* Se ottieni *ITEM_ALREADY_OWNED*, l'app del Play Store potrebbe dover essere aggiornata, sei pregato di [vedere qui](https://support.google.com/googleplay/answer/1050566?hl=en)
* Gli acquisti sono archiviati nel cloud di Google e non sono mai perduti
* Non c'è limite di tempo sugli acquisti, quindi non scadono
* Google non espone i dettagli (nome, email, etc.) sugli acquirenti agli sviluppatori
* Un'app come FairEmail non può selezionare quale profilo Google usare
* Potrebbe volerci un po' fino alla sincronizzazione di un acquisto su un altro dispositivo dall'app del Play Store
* Gli acquisti del Play Store non sono utilizzabili senza il Play Store, il che non è anche consentito dalle regole del Play Store

If you cannot solve the problem with the purchase, you will have to contact Google about it.

<br />

<a name="faq118"></a>
**(118) What does 'Remove tracking parameters' exactly?**

Checking *Remove tracking parameters* will remove all [UTM parameters](https://en.wikipedia.org/wiki/UTM_parameters) from a link.

<br />

<a name="faq119"></a>
**~~(119) Can you add colors to the unified inbox widget?~~**

~~The widget is designed to look good on most home/launcher screens by making it monochrome and by using a half transparent background.~~ ~~This way the widget will nicely blend in, while still being properly readable.~~

~~Adding colors will cause problems with some backgrounds and will cause readability problems, which is why this won't be added.~~

Due to Android limitations it is not possible to dynamically set the opacity of the background and to have rounded corners at the same time.

<br />

<a name="faq120"></a>
**(120) Why are new message notifications not removed on opening the app?**

New message notifications will be removed on swiping notifications away or on marking the associated messages read. Opening the app will not remove new message notifications. This gives you a choice to leave new message notifications as a reminder that there are still unread messages.

On Android 7 Nougat and later new message notifications will be [grouped](https://developer.android.com/training/notify-user/group). Tapping on the summary notification will open the unified inbox. The summary notification can be expanded to view individual new message notifications. Tapping on an individual new message notification will open the conversation the message it is part of. See [this FAQ](#user-content-faq70) about when messages in a conversation will be auto expanded and marked read.

<br />

<a name="faq121"></a>
**(121) How are messages grouped into a conversation?**

By default FairEmail groups messages in conversations. This can be turned of in the display settings.

FairEmail groups messages based on the standard *Message-ID*, *In-Reply-To* and *References* headers. FairEmail does not group on other criteria, like the subject, because this could result in grouping unrelated messages and would be at the expense of increased battery usage.

<br />

<a name="faq122"></a>
**~~(122) Why is the recipient name/email address show with a warning color?~~**

~~The recipient name and/or email address in the addresses section will be shown in a warning color~~ ~~when the sender domain name and the domain name of the *to* address do not match.~~ ~~Mostly this indicates that the message was received *via* an account with another email address.~~

<br />

<a name="faq123"></a>
**(123) What will happen when FairEmail cannot connect to an email server?**

If FairEmail cannot connect to an email server to synchronize messages, for example if the internet connection is bad or a firewall or a VPN is blocking the connection, FairEmail will retry two times after waiting 4 and 8 seconds while keeping the device awake (=use battery power). If this fails, FairEmail will schedule an alarm to retry after 15, 30 and eventually every 60 minutes and let the device sleep (=no battery usage).

Note that [Android doze mode](https://developer.android.com/training/monitoring-device-state/doze-standby) does not allow to wake the device earlier than after 15 minutes.

*Force sync* in the three-dots menu of the unified inbox can be used to let FairEmail attempt to reconnect without waiting.

Sending messages will be retried on connectivity changes only (reconnecting to the same network or connecting to another network) to prevent the email server from blocking the connection permanently. You can pull down the outbox to retry manually.

Note that sending will not be retried in case of authentication problems and when the server rejected the message. In this case you can pull down the outbox to try again.

<br />

<a name="faq124"></a>
**(124) Why do I get 'Message too large or too complex to display'?**

The message *Message too large or too complex to display* will be shown if there are more than 100,000 characters or more than 500 links in a message. Reformatting and displaying such messages will take too long. You can try to use the original message view, powered by the browser, instead.

<br />

<a name="faq125"></a>
**(125) What are the current experimental features?**

*Message classification (version 1.1438+)*

Please see [this FAQ](#user-content-faq163) for details.

Since this is an experimental feature, my advice is to start with just one folder.

<br />

<a name="faq126"></a>
**(126) Can message previews be sent to my wearable?**

FairEmail fetches a message in two steps:

1. Recupera intestazioni del messaggio
1. Recupera testo e allegati del messaggio

Directly after the first step new messages will be notified. However, only until after the second step the message text will be available. FairEmail updates exiting notifications with a preview of the message text, but unfortunately wearable notifications cannot be updated.

Since there is no guarantee that a message text will always be fetched directly after a message header, it is not possible to guarantee that a new message notification with a preview text will always be sent to a wearable.

If you think this is good enough, you can enable the notification option *Only send notifications with a message preview to wearables* and if this does not work, you can try to enable the notification option *Show notifications with a preview text only*. Note that this applies to wearables not showing a preview text too, even when the Android Wear app says the notification has been sent (bridged).

If you want to have the full message text sent to your wearable, you can enable the notification option *Preview all text*. Note that some wearables are known to crash with this option enabled.

If you use a Samsung wearable with the Galaxy Wearable (Samsung Gear) app, you might need to enable notifications for FairEmail when the setting *Notifications*, *Apps installed in the future* is turned off in this app.

<br />

<a name="faq127"></a>
**(127) How can I fix 'Syntactically invalid HELO argument(s)'?**

The error *... Syntactically invalid HELO argument(s) ...* means that the SMTP server rejected the local IP address or host name. You can likely fix this error by enabling or disabling the advanced indentity option *Use local IP address instead of host name*.

<br />

<a name="faq128"></a>
**(128) How can I reset asked questions, for example to show images?**

You can reset asked questions via the three dots overflow menu in the miscellaneous settings.

<br />

<a name="faq129"></a>
**(129) Are ProtonMail, Tutanota supported?**

ProtonMail uses a proprietary email protocol and [does not directly support IMAP](https://protonmail.com/support/knowledge-base/imap-smtp-and-pop3-setup/), so you cannot use FairEmail to access ProtonMail.

Tutanota uses a proprietary email protocol and [does not support IMAP](https://tutanota.com/faq/#imap), so you cannot use FairEmail to access Tutanota.

<br />

<a name="faq130"></a>
**(130) What does message error ... mean?**

A series of lines with orangish or red texts with technical information means that debug mode was enabled in the miscellaneous settings.

The warning *No server found at ...* means that there was no email server registered at the indicated domain name. Replying to the message might not be possible and might result in an error. This could indicate a falsified email address and/or spam.

The error *... ParseException ...* means that there is a problem with a received message, likely caused by a bug in the sending software. FairEmail will workaround this is in most cases, so this message can mostly be considered as a warning instead of an error.

The error *...SendFailedException...* means that there was a problem while sending a message. The error will almost always include a reason. Common reasons are that the message was too big or that one or more recipient addresses were invalid.

The warning *Message too large to fit into the available memory* means that the message was larger than 10 MiB. Even if your device has plenty of storage space Android provides limited working memory to apps, which limits the size of messages that can be handled.

Please see [here](#user-content-faq22) for other error messages in the outbox.

<br />

<a name="faq131"></a>
**(131) Can you change the direction for swiping to previous/next message?**

If you read from left to right, swiping to the left will show the next message. Similarly, if you read from right to left, swiping to the right will show the next message.

This behavior seems quite natural to me, also because it is similar to turning pages.

Anyway, there is a behavior setting to reverse the swipe direction.

<br />

<a name="faq132"></a>
**(132) Why are new message notifications silent?**

Notifications are silent by default on some MIUI versions. Please see [here](http://en.miui.com/thread-3930694-1-1.html) how you can fix this.

There is a bug in some Android versions causing [setOnlyAlertOnce](https://developer.android.com/reference/android/app/Notification.Builder#setOnlyAlertOnce(boolean)) to mute notifications. Since FairEmail shows new message notifications right after fetching the message headers and FairEmail needs to update new message notifications after fetching the message text later, this cannot be fixed or worked around by FairEmail.

Android might rate limit the notification sound, which can cause some new message notifications to be silent.

<br />

<a name="faq133"></a>
**(133) Why is ActiveSync not supported?**

The Microsoft Exchange ActiveSync protocol [is patented](https://en.wikipedia.org/wiki/Exchange_ActiveSync#Licensing) and can therefore not be supported. For this reason you won't find many, if any, other email clients supporting ActiveSync.

The Microsoft Exchange Web Services protocol [is being phased out](https://techcommunity.microsoft.com/t5/Exchange-Team-Blog/Upcoming-changes-to-Exchange-Web-Services-EWS-API-for-Office-365/ba-p/608055).

Note that the desciption of FairEmail starts with the remark that non-standard protocols, like Microsoft Exchange Web Services and Microsoft ActiveSync are not supported.

<br />

<a name="faq134"></a>
**(134) Can you add deleting local messages?**

*POP3*

In the account settings (Setup, step 1, Manage, tap account) you can enable *Leave deleted messages on server*.

*IMAP*

Since the IMAP protocol is meant to synchronize two ways, deleting a message from the device would result in fetching the message again when synchronizing again.

However, FairEmail supports hiding messages, either via the three-dots menu in the action bar just above the message text or by multiple selecting messages in the message list. Basically this is the same as "leave on server" of the POP3 protocol with the advantage that you can show the messages again when needed.

Note that it is possible to set the swipe left or right action to hide a message.

<br />

<a name="faq135"></a>
**(135) Why are trashed messages and drafts shown in conversations?**

Individual messages will rarely be trashed and mostly this happens by accident. Showing trashed messages in conversations makes it easier to find them back.

You can permanently delete a message using the message three-dots *delete* menu, which will remove the message from the conversation. Note that this irreversible.

Similarly, drafts are shown in conversations to find them back in the context where they belong. It is easy to read through the received messages before continuing to write the draft later.

<br />

<a name="faq136"></a>
**(136) How can I delete an account/identity/folder?**

Deleting an account/identity/folder is a little bit hidden to prevent accidents.

* Profilo: Configurazione > Passaggio 1 > Gestione > Tocca profilo
* Identità: Configurazione > Passaggio 2 > Gestione > Tocca identità
* Cartella: Lunga pressione sulla cartella nell'elenco delle cartelle > Modifica proprietà

In the three-dots overflow menu at the top right there is an item to delete the account/identity/folder.

<br />

<a name="faq137"></a>
**(137) How can I reset 'Don't ask again'?**

You can reset all questions set to be not asked again in the miscellaneous settings.

<br />

<a name="faq138"></a>
**(138) Can you add calendar/contact management/synchronizing?**

Calendar and contact management can better be done by a separate, specialized app. Note that FairEmail is a specialized email app, not an office suite.

Also, I prefer to do a few things very well, instead of many things only half. Moreover, from a security perspective, it is not a good idea to grant many permissions to a single app.

You are advised to use the excellent, open source [DAVx⁵](https://f-droid.org/packages/at.bitfire.davdroid/) app to synchronize/manage your calendars/contacts.

Most providers support exporting your contacts. Please [see here](https://support.google.com/contacts/answer/1069522) about how you can import contacts if synchronizing is not possible.

Note that FairEmail does support replying to calendar invites (a pro feature) and adding calendar invites to your personal calendar.

<br />

<a name="faq83"></a>
<a name="faq139"></a>
**(139) How do I fix 'User is authenticated but not connected'?**

In fact this Microsoft Exchange specific error is an incorrect error message caused by a bug in older Exchange server software.

The error *User is authenticated but not connected* might occur if:

* I messaggi push sono abilitati per troppe cartelle: vedi [questa FAQ](#user-content-faq23) per ulteriori informazioni e una soluzione
* La password del profilo è stata cambiata: cambiarla in FairEmail dovrebbe risolvere il problema
* L'indirizzo email di un alias è usato come nome utente invece che come indirizzo email principale
* Uno schema di accesso scorretto è in uso per una casella di posta condivisa: lo schema corretto è *username@domain\SharedMailboxAlias*

The shared mailbox alias will mostly be the email address of the shared account, like this:

```
you@example.com\shared@example.com
```

Note that it should be a backslash and not a forward slash.

<br />

<a name="faq140"></a>
**(140) Why does the message text contain strange characters?**

Displaying strange characters is almost always caused by specifying no or an invalid character encoding by the sending software. FairEmail will assume [ISO 8859-1](https://en.wikipedia.org/wiki/ISO/IEC_8859-1) when no character set or when [US-ASCII](https://en.wikipedia.org/wiki/ASCII) was specified. Other than that there is no way to reliably determine the correct character encoding automatically, so this cannot be fixed by FairEmail. The right action is to complain to the sender.

<br />

<a name="faq141"></a>
**(141) How can I fix 'A drafts folder is required to send messages'?**

To store draft messages a drafts folder is required. In most cases FairEmail will automatically select the drafts folders on adding an account based on [the attributes](https://www.iana.org/assignments/imap-mailbox-name-attributes/imap-mailbox-name-attributes.xhtml) the email server sends. However, some email servers are not configured properly and do not send these attributes. In this case FairEmail tries to identify the drafts folder by name, but this might fail if the drafts folder has an unusual name or is not present at all.

You can fix this problem by manually selecting the drafts folder in the account settings (Setup, step 1, tap account, at the bottom). If there is no drafts folder at all, you can create a drafts folder by tapping on the '+' button in the folder list of the account (tap on the account name in the navigation menu).

Some providers, like Gmail, allow enabling/disabling IMAP for individual folders. So, if a folder is not visible, you might need to enable IMAP for the folder.

Quick link for Gmail: [https://mail.google.com/mail/u/0/#settings/labels](https://mail.google.com/mail/u/0/#settings/labels)

<br />

<a name="faq142"></a>
**(142) How can I store sent messages in the inbox?**

Generally, it is not a good idea to store sent messages in the inbox because this is hard to undo and could be incompatible with other email clients.

That said, FairEmail is able to properly handle sent messages in the inbox. FairEmail will mark outgoing messages with a sent messages icon for example.

The best solution would be to enable showing the sent folder in the unified inbox by long pressing the sent folder in the folder list and enabling *Show in unified inbox*. This way all messages can stay where they belong, while allowing to see both incoming and outgoing messages at one place.

If this is not an option, you can [create a rule](#user-content-faq71) to automatically move sent messages to the inbox or set a default CC/BCC address in the advanced identity settings to send yourself a copy.

<br />

<a name="faq143"></a>
**~~(143) Can you add a trash folder for POP3 accounts?~~**

[POP3](https://en.wikipedia.org/wiki/Post_Office_Protocol) is a very limited protocol. Basically only messages can be downloaded and deleted from the inbox. It is not even possible to mark a message read.

Since POP3 does not allow access to the trash folder at all, there is no way to restore trashed messages.

Note that you can hide messages and search for hidden messages, which is similar to a local trash folder, without suggesting that trashed messages can be restored, while this is actually not possible.

Version 1.1082 added a local trash folder. Note that trashing a message will permanently remove it from the server and that trashed messages cannot be restored to the server anymore.

<br />

<a name="faq144"></a>
**(144) How can I record voice notes?**

To record voice notes you can press this icon in the bottom action bar of the message composer:

![External image](https://github.com/M66B/FairEmail/blob/master/images/baseline_record_voice_over_black_48dp.png)

This requires a compatible audio recorder app to be installed. In particular [this common intent](https://developer.android.com/reference/android/provider/MediaStore.Audio.Media.html#RECORD_SOUND_ACTION) needs to be supported.

For example [this audio recorder](https://f-droid.org/app/com.github.axet.audiorecorder) is compatible.

Voice notes will automatically be attached.

<br />

<a name="faq145"></a>
**(145) How can I set a notification sound for an account, folder or sender?**

Account:

* Abilita *Notifiche separate* nelle impostazioni avanzate del profilo (Configurazione, passaggio 1, Gestione, tocca profilo, tocca Avanzate)
* Premi a lungo il profilo nell'elenco dei profili (Configurazione, passaggio 1, Gestione) e seleziona *Modifica canale di notifica* per notificare il suono di notifica

Folder:

* Premi a lungo la cartella nell'elenco della cartella e seleziona *Crea canale di notifica*
* Premi a lungo la cartella nell'elenco della cartella e seleziona *Modifica canale di notifica* per cambiare il suono di notifica

Sender:

* Apri un messaggio dal mittente ed espandilo
* Espandi la sezione degli indirizzi toccando sulla freccia giù
* Tocca sull'icona della campanella per creare o modificare un canale di notifica e cambiare il suono di notifica

The order of precendence is: sender sound, folder sound, account sound and default sound.

Setting a notification sound for an account, folder or sender requires Android 8 Oreo or later and is a pro feature.

<br />

<a name="faq146"></a>
**(146) How can I fix incorrect message times?**

Since the sent date/time is optional and can be manipulated by the sender, FairEmail uses the server received date/time by default.

Sometimes the server received date/time is incorrect, mostly because messages were incorrectly imported from another server and sometimes due to a bug in the email server.

In these rare cases, it is possible to let FairEmail use either the date/time from the *Date* header (sent time) or from the *Received* header as a workaround. This can be changed in the advanced account settings: Setup, step 1, Manage, tap account, tap Advanced.

This will not change the time of already synchronized messages. To solve this, long press the folder(s) in the folder list and select *Delete local messages* and *Synchronize now*.

<br />

<a name="faq147"></a>
**(147) What should I know about third party versions?**

You likely came here because you are using a third party build of FairEmail.

There is **only support** on the latest Play store version, the latest GitHub release and the F-Droid build, but **only if** the version number of the F-Droid build is the same as the version number of the latest GitHub release.

F-Droid builds irregularly, which can be problematic when there is an important update. Therefore you are advised to switch to the GitHub release.

The F-Droid version is built from the same source code, but signed differently. This means that all features are available in the F-Droid version too, except for using the Gmail quick setup wizard because Google approved (and allows) one app signature only. For all other email providers, OAuth access is only available in Play Store versions and Github releases, as the email providers only permit the use of OAuth for official builds.

Note that you'll need to uninstall the F-Droid build first before you can install a GitHub release because Android refuses to install the same app with a different signature for security reasons.

Note that the GitHub version will automatically check for updates. When desired, this can be turned off in the miscellaneous settings.

Please [see here](https://github.com/M66B/FairEmail/blob/master/README.md#user-content-downloads) for all download options.

If you have a problem with the F-Droid build, please check if there is a newer GitHub version first.

<br />

<a name="faq148"></a>
**(148) How can I use an Apple iCloud account?**

There is a built-in profile for Apple iCloud, but if needed you can find the right settings [here](https://support.apple.com/en-us/HT202304).

When using two-factor authentication you might need to use an [app-specific password](https://support.apple.com/en-us/HT204397).

<br />

<a name="faq149"></a>
**(149) How does the unread message count widget work?**

The unread message count widget shows the number of unread messages either for all accounts or for a selected account, but only for the folders for which new message notifications are enabled.

Tapping on the notification will synchronize all folders for which synchronization is enabled and will open:

* la schermata di avvio quando sono selezionati tutti i profili
* un elenco delle cartelle quando è selezionato un profilo specifico e quando le notifiche dei nuovi messaggi sono abilitate per cartelle multiple
* un elenco di messaggi quando un profilo specifico è stato selezionato e all'abilitazione per una cartella delle notifiche di nuovi messaggi

<br />

<a name="faq150"></a>
**(150) Can you add cancelling calendar invites?**

Cancelling calendar invites (removing calendar events) requires write calendar permission, which will result in effectively granting permission to read and write *all* calendar events of *all* calendars.

Given the goal of FairEmail, privacy and security, and given that it is easy to remove a calendar event manually, it is not a good idea to request this permission for just this reason.

Inserting new calendar events can be done without permissions with special [intents](https://developer.android.com/guide/topics/providers/calendar-provider.html#intents). Unfortunately, there exists no intent to delete existing calendar events.

<br />

<a name="faq151"></a>
**(151) Can you add backup/restore of messages?**

An email client is meant to read and write messages, not to backup and restore messages. Note that breaking or losing your device, means losing your messages!

Instead, the email provider/server is responsible for backups.

If you want to make a backup yourself, you could use a tool like [imapsync](https://imapsync.lamiral.info/).

If you want to import an mbox file to an existing email account, you can use Thunderbird on a desktop computer and the [ImportExportTools](https://addons.thunderbird.net/nl/thunderbird/addon/importexporttools/) add-on.

<br />

<a name="faq152"></a>
**(152) How can I insert a contact group?**

You can insert the email addresses of all contacts in a contact group via the three dots menu of the message composer.

You can define contact groups with the Android contacts app, please see [here](https://support.google.com/contacts/answer/30970) for instructions.

<br />

<a name="faq153"></a>
**(153) Why does permanently deleting Gmail message not work?**

You might need to change [the Gmail IMAP settings](https://mail.google.com/mail/u/0/#settings/fwdandpop) on a desktop browser to make it work:

* Quando contrassegno un messaggio in IMAP come eliminato: Auto-Expunge off - Attendi che il client aggiorni il server.
* Quando un messaggio è contrassegnato come eliminato e cancellato dall'ultima cartella IMAP visibile: Elimina immediatamente il messaggio per sempre

Note that archived messages can be deleted only by moving them to the trash folder first.

Some background: Gmail seems to have an additional message view for IMAP, which can be different from the main message view.

<br />

<a name="faq154"></a>
**~~(154) Can you add favicons as contact photos?~~**

~~Besides that a [favicon](https://en.wikipedia.org/wiki/Favicon) might be shared by many email addresses with the same domain name~~ ~~and therefore is not directly related to an email address, favicons can be used to track you.~~

<br />

<a name="faq155"></a>
**(155) What is a winmail.dat file?**

A *winmail.dat* file is sent by an incorrectly configured Outlook client. It is a Microsoft specific file format ([TNEF](https://en.wikipedia.org/wiki/Transport_Neutral_Encapsulation_Format)) containing a message and possibly attachments.

You can find some more information about this file [here](https://support.mozilla.org/en-US/kb/what-winmaildat-attachment).

You can view it with for example the Android app [Letter Opener](https://play.google.com/store/apps/details?id=app.letteropener).

<br />

<a name="faq156"></a>
**(156) How can I set up an Office 365 account?**

An Office 365 account can be set up via the quick setup wizard and selecting *Office 365 (OAuth)*.

If the wizard ends with *AUTHENTICATE failed*, IMAP and/or SMTP might be disabled for the account. In this case you should ask the administrator to enable IMAP and SMTP. The procedure is documented [here](https://docs.microsoft.com/en-in/exchange/troubleshoot/configure-mailboxes/pop3-imap-owa-activesync-office-365).

If you've enabled *security defaults* in your organization, you might need to enable the SMTP AUTH protocol. Please [see here](https://docs.microsoft.com/en-us/exchange/clients-and-mobile-in-exchange-online/authenticated-client-smtp-submission) about how to.

<br />

<a name="faq157"></a>
**(157) How can I set up an Free.fr account?**

Veuillez [voir ici](https://free.fr/assistance/597.html) pour les instructions.

**SMTP est désactivé par défaut**, veuillez [voir ici](https://free.fr/assistance/2406.html) comment il peut être activé.

Veuillez [voir ici](http://jc.etiemble.free.fr/abc/index.php/trucs-astuces/configurer-smtp-free-fr) pour un guide détaillé.

<br />

<a name="faq103"></a>
<a name="faq158"></a>
**(158) Which camera / audio recorder do you recommend?**

To take photos and to record audio a camera and an audio recorder app are needed. The following apps are open source cameras and audio recorders:

* [Open Camera](https://play.google.com/store/apps/details?id=net.sourceforge.opencamera) ([F-Droid](https://f-droid.org/en/packages/net.sourceforge.opencamera/))
* [Audio Recorder versione 3.3.24+](https://play.google.com/store/apps/details?id=com.github.axet.audiorecorder) ([F-Droid](https://f-droid.org/packages/com.github.axet.audiorecorder/))

To record voice notes, etc, the audio recorder needs to support [MediaStore.Audio.Media.RECORD_SOUND_ACTION](https://developer.android.com/reference/android/provider/MediaStore.Audio.Media#RECORD_SOUND_ACTION). Oddly, most audio recorders seem not to support this standard Android action.

<br />

<a name="faq159"></a>
**(159) What are Disconnect's tracker protection lists?**

Please see [here](https://disconnect.me/trackerprotection) for more information about Disconnect's tracker protection lists.

After downloading the lists in the privacy settings, the lists can optionally be used:

* per avvisare sui collegamenti di monitoraggio all'apertura dei collegamenti
* per riconoscere le immagini di monitoraggio nei messaggi

Tracking images will be disabled only if the corresponding main 'disable' option is enabled.

Tracking images will not be recognized when the domain is classified as '*Content*', see [here](https://disconnect.me/trackerprotection#trackers-we-dont-block) for more information.

This command can be sent to FairEmail from an automation app to update the protection lists:

```
(adb shell) am startservice -a eu.faircode.email.DISCONNECT.ME
```

Updating once a week will probably be sufficient, please see [here](https://github.com/disconnectme/disconnect-tracking-protection/commits/master) for recent lists changes.

<br />

<a name="faq160"></a>
**(160) Can you add permanent deletion of messages without confirmation?**

Permanent deletion means that messages will *irreversibly* be lost, and to prevent this from happening accidentally, this always needs to be confirmed. Even with a confirmation, some very angry people who lost some of their messages through their own fault contacted me, which was a rather unpleasant experience :-(

Advanced: the IMAP delete flag in combination with the EXPUNGE command is not supportable because both email servers and not all people can handle this, risking unexpected loss of messages. A complicating factor is that not all email servers support [UID EXPUNGE](https://tools.ietf.org/html/rfc4315).

<br />

<a name="faq161"></a>
**(161) Can you add a setting to change the primary and accent color?***

If I could, I would add a setting to select the primary and accent color right away, but unfortunately Android themes are fixed, see for example [here](https://stackoverflow.com/a/26511725/1794097), so this is not possible.

<br />

<a name="faq162"></a>
**(162) Is IMAP NOTIFY supported?***

Yes, [IMAP NOTIFY](https://tools.ietf.org/html/rfc5465) has been supported since version 1.1413.

IMAP NOTIFY support means that notifications for added, changed or deleted messages of all *subscribed* folders will be requested and if a notification is received for a subscribed folder, that the folder will be synchronized. Synchronization for subscribed folders can therefore be disable, saving folder connections to the email server.

**Important**: push messages (=always sync) for the inbox and subscription management (receive settings) need to be enabled.

**Important**: most email servers do not support this! You can check the log via the navigation menu if an email server supports the NOTIFY capability.

<br />

<a name="faq163"></a>
**(163) What is message classification?**

*This is an experimental feature!*

Message classification will attempt to automatically group emails into classes, based on their contents, using [Bayesian statistics](https://en.wikipedia.org/wiki/Bayesian_statistics). In the context of FairEmail, a folder is a class. So, for example, the inbox, the spam folder, a 'marketing' folder, etc, etc.

You can enable message classification in the miscellaneous settings. This will enable 'learning' mode only. The classifier will 'learn' from new messages in the inbox and spam folder by default. The folder property *Classify new messages in this folder* will enable or disable 'learning' mode for a folder. You can clear local messages (long press a folder in the folder list of an account) and synchronize the messages again to classify existing messages.

Each folder has an option *Automatically move classified messages to this folder* ('auto classification' for short). When this is turned on, new messages in other folders which the classifier thinks belong to that folder will be automatically moved.

The option *Use local spam filter* in the report spam dialog will turn on message classification in the miscellaneous settings and auto classification for the spam folder. Please understand that this is not a replacement for the spam filter of the email server and can result in [false positives and false negatives](https://en.wikipedia.org/wiki/False_positives_and_false_negatives). See also [this FAQ](#user-content-faq92).

A practical example: suppose there is a folder 'marketing' and auto message classification is enabled for this folder. Each time you move a message into this folder you'll train FairEmail that similar messages belong in this folder. Each time you move a message out of this folder you'll train FairEmail that similar messages do not belong in this folder. After moving some messages into the 'marketing' folder, FairEmail will start moving similar messages automatically into this folder. Or, the other way around, after moving some messages out of the 'marketing' folder, FairEmail will stop moving similar messages automatically into this folder. This will work best with messages with similar content (email addresses, subject and message text).

Classification should be considered as a best guess - it might be a wrong guess, or the classifier might not be confident enough to make any guess. If the classifier is unsure, it will simply leave an email where it is.

To prevent the email server from moving a message into the spam folder again and again, auto classification out of the spam folder will not be done.

The message classifier calculates the probability a message belongs in a folder (class). There are two options in the miscellaneous settings which control if a message will be automatically moved into a folder, provided that auto classification is enabled for the folder:

* *Minimum class probability*: a message will only be moved when the confidence it belongs in a folder is greater than this value (default 20 %)
* *Minimum class difference*: a message will only be moved when the difference in confidence between one class and the next most likely class is greater than this value (default 50 %)

Both conditions must be satisfied before a message will be moved.

Considering the defaults option values:

* Il 40% delle mele e il 30% delle banane sarebbe scartato perché la differenza del 25% è inferiore al minimo di 50%
* Il 15% delle mele e il 5% delle banane sarebbe scartato perché la probabilità per le mele è inferiore al minimo del 20%
* Il 50% delle mele e il 20% delle banane risulterebbe nella selezione delle mele

Classification is optimized to use as little resources as possible, but will inevitably use some extra battery power.

You can delete all classification data by turning classification in the miscellaneous settings three times off.

[Filter rules](#user-content-faq71) will be executed before classification.

Message classification is a pro feature, except for the spam folder.

<br />

## Ricevi supporto

FairEmail is supported on smartphones, tablets and ChromeOS only.

Only the latest Play store version and latest GitHub release are supported. The F-Droid build is supported only if the version number is the same as the version number of the latest GitHub release. This also means that downgrading is not supported.

There is no support on things that are not directly related to FairEmail.

There is no support on building and developing things by yourself.

Requested features should:

* essere utili alla maggior parte delle persone
* non complicare l'uso di FairEmail
* adattarsi alla filosofia di FairEmail (orientate alla privacy, con un occhio alla sicurezza)
* conformarsi agli standard comuni (IMAP, SMTP, etc.)

Features not fulfilling these requirements will likely be rejected. This is also to keep maintenance and support in the long term feasible.

If you have a question, want to request a feature or report a bug, please use [this form](https://contact.faircode.eu/?product=fairemailsupport).

GitHub issues are disabled due to frequent misusage.

<br />

Copyright &copy; 2018-2021 Marcel Bokhorst.
