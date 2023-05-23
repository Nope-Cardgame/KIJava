# Nope - KI Spieler (Java)

Kurze Beschreibung des Repos und der übergriefenden Schnittstelle für die unterschiedlichen Clients.


## Dokumentation
[Link zum Doku-Ordner](https://github.com/Nope-Cardgame/Repo-Template/Doku-Ordner)


## Mitglieder
Mitglied | entwickelter Client |--- | --- |
[Marian Koge](https://github.com/MarianK99) | Client1
[Alexander Lauruhn](https://github.com/AlexanderLauruhn) | Client2
[Julius Emil Arendt](https://github.com/Aremju) | Client3


## Installation

Erklärung wie das Projekt ausgeführt wird

1. Installation von benötigten Softwarepakete
2. Klonen des GitHub-Repo
3. Ausführen des Programms


## Benutzung
Beschreibung, wie das Programm bedient werden muss, um eine Verbindung zum NOPE-Server herzustellen (welche Konsoleneingaben/GUI Interaktionen müssen getätigt werden) und ein NOPE Spiel zu starten.

### Client1
Beschreibung der Bedienung für Client1 um ein NOPE Spiel zu spielen

### Client2
Beschreibung der Bedienung für Client2 um ein NOPE Spiel zu spielen

### Client3

Hier ist eine Pseudocode-Beschreibung des aktuellen KI-Clients,<br> 
wie er in etwa funktionieren soll (er macht lediglich erstmal gültige Spielzüge)

    Algorithmus gueltigerSpielzug(game):
    Aktionsobjekt = null

        wenn letzte Aktion = takeCard:
            wenn oberste Karte = invisible und invisible einzige Karte:
                hole dir erste Karte mit dieser Farbe
                wenn gefunden:
                    wenn Karte = nominateCard
                        Aktionsobjekt = erstelle nominate mit dieser Farbe mit 2 Karten
                    ansonsten: 
                        Aktionsobjekt = erstelle discardCard mit dieser Karte
                ansonsten:
                    Aktionsobjekt = erstelle sayNope
            ansonsten wenn oberste Karte = numbercard:
                wenn karte 2 farben hat:
                    suche Farbe, für die Set vervollständigt
                schaue, ob set vervollständigt
                    ja: Aktionsobjekt = erstelle discardCard mit diesem Set, bzw. nominate, wenn beim Set nominate oben ist
                    nein: Aktionsobjekt = erstelle sayNope
            ansonsten:
                wenn oberste Karte = nominateCard:
                    suche Set mit der Nominate-Aktion
                    wenn gefunden:
                        wenn erste Karte == nominate:
                            Aktionsobjekt = erstelle nominateCard mit dieser Farbe und 2 karten sowie anderem spieler
                        ansonsten:
                            Aktionsobjekt = erstelle discardCard mit dieser Farbe
                    ansonsten:
                        Aktionsobjekt = erstelle sayNope
                ansonsten wenn oberste Karte = reset
                    suche irgendeine Karte
                    wenn karte = nominate:
                        Aktionsobjekt = erstelle nominate mit 2 karten und anderem spieler
                    ansonsten:
                        Aktionsobjekt = erstelle discardCard dieser Karte

        ansonsten:
            ansonsten wenn oberste karte = nominate und nominate einzige Karte:
                Aktionsobjekt = erstelle nominate ohne eine Karte (mit der Farbe) mit 2 Karten mit einem anderen Spieler
            ansonsten wenn oberste Karte = durchblick und durchblick einzige Karte:
                hole dir erste Karte mit dieser Farbe
                wenn gefunden:
                    wenn Karte = nominateCard
                        Aktionsobjekt = erstelle nominate mit dieser Farbe mit 2 Karten
                    ansonsten: 
                        Aktionsobjekt = erstelle discardCard mit dieser Karte
                ansonsten:
                    Aktionsobjekt = erstelle takeCard
	        ansonsten wenn oberste Karte = numbercard:
	            schaue, ob Set vervollständigt:
	                ja : erstelle discardCard mit diesem Set bzw. nominate, wenn die oberste Karte ein nominate ist
                    nein: erstelle takeCard
	        ansonsten wenn oberste Karte = aktionscard:
                wenn oberste Karte = nominate:
                    suche Set mit den letzten Nominate-Aktionen
                    wenn gefunden:
                        wenn oberste Karte nominate ist:
                            Aktionsobjekt = erstelle nominateCard mit diesem Set und einer Anzahl von 2
                        ansonsten:
                            Aktionsobjekt = erstelle discardCard mit diesen Karten
                    ansonsten:
                        Aktionsobjekt = erstelle takeCard
                ansonsten wenn Karte = reset
                    hole die erste Karte auf der Hand
                    wenn erste Karte = nominate:
                        Aktionsobjekt = erstelle nominateCard mit dieser Karte
                    ansonsten:
                        Aktionsobjekt = erstelle discardCard mit dieser Karte
        gebe Aktionsobject als JSON zurück
