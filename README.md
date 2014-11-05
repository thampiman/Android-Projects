FitWatch
=====
This is an open source pedometer application for Android phones that do not have the [hardware step detector](https://developer.android.com/about/versions/kitkat.html) built in. Another feature of this application is that the steps count can be beamed to your Android wearable device by clicking on the 'Watch' button. The count will be displayed and updated in real-time on the watch.

![FitWatch](fitwatch.png)

*TODO:*

- Check accuracy of pedometer by comparing with FitBit 

FlashCards
=====
This is an open source flash cards application to aid with learning. I personally built this app to help me with exam prep. The cards can be created easily using a simple text editor on your computer. The format is JSON, as shown below. It consists of a *deck* JSON object that contains the title of the deck of cards. The *cards* are represented as a JSON array and each card is a JSON object consisting of a *question*, a *hint* and an *answer*. The strings in the card can be formatted using HTML tags.

```json
{
  "deck": 
    {
       "title": "Barron's GRE Vocab 01"
    },
  "cards":
    [
      {
        "question": "Baleful (adjective)",
        "hint": "Context clue: a baleful glance",
        "answer": "Deadly; having a malign influence; ominous<p>Context sentence: a baleful glance</p>"
      },
      {
        "question": "Two synonyms for baleful",
        "hint": "Think cancer tumour that is harmful; also think something really bad that is about to happen",
        "answer": "Malignant, Ominous"
      },
      {
        "question": "Antipathy (noun)",
        "hint": "Context clue: Natural antipathy of cats and dogs",
        "answer": "Habitual aversion; intense dislike; natural repugnance; hatred<p>Anti means against; path means feeling</p>"
      },
      {
        "question": "Analogy: <p>SOLICITOUS : CONCERN :: HOSTILE : __________</p>",
        "hint": "Think of strong dislike or hatred",
        "answer": "ANTIPATHY<p>Somone solicitous or caring demonstrates concern</p>"
      },
      {
        "question": "Macabre (adjective)",
        "hint": "Context clue: a macabre ending",
        "answer": "gruesome and horrifying, ghastly; representing death<p>Pronounciation: muh-kah-bruh</p>"
      }
    ]
}
```
Once these cards are created, they have to be copied onto the SD storage of the device in the *FlashCards* folder. When the app is opened on the device, it processes all the JSON files in the *FlashCards* folder and persists each deck into an SQLite database. 

The following screenshots show how the JSON file is processed and displayed in the Android app. The cards in the deck are randomised every time the deck is opened.

![FlashCards Main Screen](flashcards_main.png)

![FlashCards Card 1](flashcards_card1.png)

![FlashCards Card 2](flashcards_card2.png)

*TODO:*

- Implement the [Leitner](http://en.wikipedia.org/wiki/Leitner_system) system to improve how the cards are displayed

Remotainment
=====
This is an open source remote controller application for the VLC media player. It consists of a server desktop app that interfaces with VLC on your PC, and an Android app that acts as the controller.

### Remotainment Server
This Java desktop application runs on your PC and acts as the middleman between the Android app and VLC. The app does all the hard work of setting VLC up and also provides additional features for the Android app, as detailed below. At the moment, this app is currently supported only on Windows. 

It listens on TCP port 1309 for connections from the client app. The connection is secured using a password so that it does not allow any client to connect to the server. The server supports only one client to connect at a time. Once a client connects on port 1309, the server attempts to connect to the client on port 2005 to establish a two-way link. 

The server supports the following queries from the client:

1. Launch / Quit VLC (see [VLCControl.java](https://github.com/thampiman/Android-Projects/blob/master/Remotainment/Remotainment_Server/src/com/crimsonsky/remotainment/intf/VLCControl.java))
2. Play / Pause / Next / Previous / Vol Up / Vol Down / Full Screen (see [VLCControl.java](https://github.com/thampiman/Android-Projects/blob/master/Remotainment/Remotainment_Server/src/com/crimsonsky/remotainment/intf/VLCControl.java))
3. Get Media List (see [RCServer.java](https://github.com/thampiman/Android-Projects/blob/master/Remotainment/Remotainment_Server/src/com/crimsonsky/remotainment/intf/RCServer.java))
   - The following file formats are supported: avi, asf, wmv, wma, mp4, mp3, mov, 3gp, ogg, ogm, mkv, rm, wav, flac, flv, mxf
   - By default, the entire hard disk is scanned but the user can set a specific folder to save time

The connection with VLC is made on port 1005.

### Remotainment Android App (*Work in Progress*)
The Android app is a simple remote controller. It allows the user to connect to the server app using the *power* button. A list of the media files on the PC can be obtained and specific files can be chosen for playback on VLC. This app is currently a work in progress.