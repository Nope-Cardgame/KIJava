# Nope - KI Spieler (Java)

Kurze Beschreibung des Repos und der übergreifenden Schnittstelle für die unterschiedlichen Clients.


## Dokumentation
[Link zum Doku-Ordner](https://github.com/Nope-Cardgame/KIJava/tree/finished_build/doku)<br><br>
<a href="#anleitung">[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/GUI-Bedienungsanleitung.md)</a> beschrieben, wie die GUI aufgebaut ist und wie sie zu bedienen ist.<br>
<a id="anleitung">[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/GUI-Bedienungsanleitung.md)</a> beschrieben, wie die GUI aufgebaut ist und wie sie zu bedienen ist.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/Packagestruktur.md) wird die softwareinterne Packagestruktur dargestellt und beschrieben.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/Rest.md) wir darauf eingegangen, wie der Client mittels Post-/Get-Request mit dem Server kommunzieren kann.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/SocketIO.md) wird erklärt, wie der Client sich mit dem Server mittels SocketIO verbindet und bis zum Beenden des Programms verbunden bleibt.

## Mitglieder
Mitglied | Entwickelter Client | 
--- | --- |
[Marian Koge](https://github.com/MarianK99) | Client1
[Alexander Lauruhn](https://github.com/AlexanderLauruhn) | Client2
[Julius Emil Arendt](https://github.com/Aremju) | Client3


## Installation

Erklärung wie das Projekt ausgeführt wird:

1. Installation der benötigten Softwarepakete
2. Klonen des GitHub-Repo
3. Ausführen des Programms (Teilweise setzt IntelliJ nicht den Source-Root-Folder beim Klonen, dies müsste im Nachhinein (falls nicht automatisch geschehen) auf .\src\main\client gesetzt werden)<br>
* Optional lässt sich das Projekt auch per Download als .zip-Datei herunterladen und ausführen
<br>Hinweis: IntelliJ wird als Entwicklungsumgebung für das Ausführen des Projektes empfohlen.


## Benutzung
Beschreibung, wie das Programm bedient werden muss, um eine Verbindung zum NOPE-Server herzustellen (welche GUI Interaktionen getätigt werden müssen) und ein NOPE Spiel zu starten. Eine genaue Erklärung lässt sich beim Punkt Dokumentation unter dem  <a href="#anleitung">zweiten Punkt</a> der Dokumenation finden.

### Client1
Beschreibung der Bedienung für Client1 um ein NOPE Spiel zu spielen

### Client2
Beschreibung der Bedienung für Client2 um ein NOPE Spiel zu spielen

### Client3 (Autor: Julius Emil Arendt)

Prinzipiell wird bei einer Partie Nope meine Strategie folgendermaßen unterteilt:
einmal in Züge, die gespielt werden, wenn noch keine Karte von mir selber genommen wurde
und Züge, die gemacht werden, wenn bereits eine Karte genommen worde ist.

Beide Unterteilungen sind im Prinzip gleich, nur dass bei der einen, wenn man keine Karte ablegen muss,
TakeCard gesagt wird, und bei der anderen geben wir einfach ein Nope-Json zurück.

Wenn bisher noch keine Karte genommen worden ist, müssen wir prüfen, ob nominate alleine auf dem Stapel
liegt, dann dürfen wir nämlich einen anderen Spieler auswählen, ohne selber legen zu müssen.
Hier erfolg die Auswahl schlau: und zwar wird immer derjenige Spieler genommen, der am wenigsten Karten auf der Hand hat.
Dies mache ich, damit ich mit höherer Wahrscheinlichkeit einen konkurrenten weniger im Spiel habe, um mich dann anschließend um
die anderen Konkurrenten kümmern zu können. Selbstverständlich wird auch darauf geachtet, dass man sich nicht selber nominiert.

Wenn die Nominate-Karte alle vier farben hat, dann wähle ich die Farbe schlau aus, und zwar wähle ich genau die Farbe, wovon ich
selbst als Spieler am wenigsten auf der Hand habe, dadurch steigt die Wahrscheinlichkeit, dass der andere Spieler diese Farbe auf
der Hand hat. Ebenfalls verringere ich dadurch die Wahrscheinlichkeit, dass ich selbst auch noch Karten ablegen muss.

Bei jeder Nominate-Aktion, die ich selber tätige, verlange ich immer 2 Karten oder wenn er nur noch eine Karte auf der Hand hat, 
eine Karte. Zwei Karten ist der Median der Zahlenmenge 1 bis 3 und daher ein gutes Mittelmaß für das nominieren von Karten.

Immer, wenn ich eine Nominate-Karte ablege, verfahre ich nach genau dieser Strategie.

Wenn ich eine Karte ablegen muss, zum Beispiel, wenn invisible als einzige Karte oben liegt oder 
eine wildcard oder ein reset oben liegt, dann suche ich mir immer die beste aktuelle Karte aus meinem
Inventar (entweder mit Farbe, beispielsweise, wenn nur eine einzige Invisible liegt) oder ohne Farbe.
Dabei verfahre ich nach folgendem Muster: 

  - zuerst reset-Karten (der Andere Spieler muss definitiv eine Karte ablegen), außerdem
    sinkt dadurch die Wahrscheinlichkeit, dass ich ein Set vervollständige etwas
  - dann nominate (Spieler muss mehrere Karten ablegen, ggf. von einer Farbe, die ich gerne hätte)
  - dann wildcards (Spieler muss definitiv eine Karte ablegen, ggf. geringere Wahrscheinlichkeit auf    Set-Vervollständigung)
  - Multiple Farben bei Zahlenkarten (Wahrscheinlichkeit senken, dass man selbst ein vollständiges Set auf der Hand hat)
  - erst dann normale Farbenkarten

Ansonsten wenn oben einfach eine numbercard liegt, prüfe ich, ob ich ein vollständiges Set habe, dabei ist es erstmal egal,
ob das Set nur aus Nummernkarten oder aus Aktionskarten besteht. Habe ich kein Set, kann ich je nach Situation eine Karte
nehmen oder Nope sagen. Habe ich jedoch eins, schaue ich erst nach, ob ich eine zweifarbige Nummernkarte oben
auf dem Ablagestapel liegen habe. Ist das der Fall, prüfe ich für jede Farbe einfach, ob ich ein Set vervollständige, finde 
ich eine Farbe, speichere ich diese und nehme diese für das Set.

Anschließend schaue ich nach, ob ich Aktionskarten auf der Hand habe von dieser Farbe. Denn wir wollen möglichst wenig Karten
auf einmal abwerfen, und bei einem vollständigen Set kann man einfach eine Aktionskarte abwerfen und den Zug sofort beenden.
Habe ich eine Aktionskarte, prüfe ich, ob ich nominate habe, ist das der fall, nehme ich die von oben bekannte Nominate-Strategie,
ansonsten lege ich die Aktionskarte regulär auf dem Ablagestapel ab.

Habe ich jedoch keine Aktionskarte der Farbe, erstelle ich ein Set, und lege dieses ab. Das Set wird schlau erstellt, indem ich
möglichst wildcards und mehrfarbige Karten ablege, und dann erst die eigentliche Farbe ablege. Somit verringert sich die Wahrscheinlichkeit, dass ich möglicherweise ein vollständiges Set auf der Hand in Zukunft haben werde/ haben könnte.

Habe ich auf dem Stapel eine Aktionskarte liegen, zum Beispiel Reset, dann hole ich mir einfach die aktuell schlaueste Karte, wie oben
bereits beschrieben. Wenn jedoch nominate oben liegen sollte, verfahre ich ähnlich wie bei den Zahlenkarten.
