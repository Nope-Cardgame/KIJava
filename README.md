# Nope - KI Spieler (Java)

In diesem Projekt wird die Implementation eines Clients in der Programmiersprache Java umgesetzt. Mithilfe dieses Clients lässt sich eine Verbindung zu dem Projektserver herstellen und das Kartenspiel ["Nope"](https://www.brettspielversand.de/mediafiles/spieleanleitungen/gamefactory/240-0013_nope_anleitung.pdf) mit anderen verbundenen Clients spielen. Hierbei ist es nicht vorgesehen, dass der Nutzer selbst die Karten auswählt, sondern stattdessen eine eingebaute KI (Auswahl aus drei verschiedenen) für den Nutzer die Spielzüge übernimmt.<br>
In den folgenden Absätzen wird der Aufbau, die Funktionsweise und weitere wichtige Informationen bezüglich des Clients und der verwendeten KIs genauer erklärt.


## Dokumentation
[Link zum Doku-Ordner](https://github.com/Nope-Cardgame/KIJava/tree/finished_build/doku)<br><br>
<a id="anleitung">[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/GUI-Bedienungsanleitung.md)</a> beschrieben, wie die GUI aufgebaut ist und wie sie zu bedienen ist.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/JSON.md) erklärt, wie die JSON-Objekte in Java übersetzt werden (Server -> Client) und die Übersetzung von Java zurück in JSON-Objekte (Client -> Server) stattfindet.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/Packagestruktur.md) wird die softwareinterne Packagestruktur dargestellt und beschrieben.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/Rest.md) wir darauf eingegangen, wie der Client mittels Post-/Get-Request mit dem Server kommunzieren kann.<br>
[Hier](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/doku/SocketIO.md) wird erklärt, wie der Client sich mit dem Server mittels SocketIO verbindet und bis zum Beenden des Programms verbunden bleibt.

## Mitglieder
Mitglied | Entwickelter Client | 
--- | --- |
[Marian Koge](https://github.com/MarianK99) | <a href="#client1">Client1</a>
[Alexander Lauruhn](https://github.com/AlexanderLauruhn) | <a href="#client2">Client2</a>
[Julius Emil Arendt](https://github.com/Aremju) | <a href="#client3">Client3</a>


## Installation

Erklärung wie das Projekt korrekt installiert und ausgeführt wird:

1. Installation der benötigten Softwarepakete (Git, IntelliJ und eine neuere Java-Version)
2. Klonen des GitHub-Repo
3. Ausführen des Programms durch die Klasse Main (Teilweise setzt IntelliJ nicht den Source-Root-Folder beim Klonen, dies müsste im Nachhinein (falls nicht automatisch geschehen) auf .\src\main\client gesetzt werden)<br>
* Optional lässt sich das Projekt auch per Download als .zip-Datei herunterladen und ausführen
<br>Hinweis: IntelliJ wird als Entwicklungsumgebung für das Ausführen des Projektes empfohlen.


## Benutzung
Beschreibung, wie das Programm bedient werden muss, um eine Verbindung zum NOPE-Server herzustellen (welche GUI Interaktionen getätigt werden müssen) und ein NOPE Spiel zu starten. Eine genaue Erklärung lässt sich beim Punkt Dokumentation unter dem  <a href="#anleitung">diesem Punkt</a> der Dokumenation finden.


## Coding-Conventions
[In diesem Dokument](https://github.com/Nope-Cardgame/KIJava/blob/finished_build/CodingConventions.md) werden die verschiedenen Richtlinien zur Umsetzung des Quellcodes beschrieben.

### <a id="#client1">Client1</a>
(Autor: Marian Koge)<br>
Dieser Absatz beinhaltet eine Zusammenfassung der Arbeitsweise der verwendeten KI.

Im Allgemeinen werden im Code acht verschiedene Strategien verwendet, die im JavaDoc der jeweiligen Methode durch das Wort "STRATEGY" gekennzeichnet worden sind.

Hierbei werden diese Strategien abhängig von der zu entscheidenden Aktion nacheinander ausgeführt, um die jeweilige Auswahl zu filtern, die Karten der Sets zu sortieren oder bestimmte Karten bewusst zu ignorieren.

Die erste verwendete Stratgie besagt, dass immer der Spieler ausgewählt werden soll, der die meisten Handkarten besitzt. Hierbei ist die Wahrscheinlichkeit am größten, dass der ausgewählte Spieler Karten abwerfen muss und die Wahrscheinlichkeit, selbst Karten abwerfen zu müssen, am geringsten ist. Diese Stratgie wird immer dann verwendet, wenn die Aktionskarte "Auswahl" (nominate) gespielt wurde oder diese Aktionskarte zu Beginn des Spiels auf dem Ablagestapel liegt und die eigene KI auf diese Karte reagieren muss. Im gespielten Turnier spielt diese Stratgie eher eine untergeordnete Rolle, da sich in einem 1 gegen 1 die Auswahlmöglichkeiten auf eine Person beschränken. Lediglich im Stechen (Sudden Death) um einen bestimmten Platz wird diese Stratgie eingesetzt.

Eine weitere Strategie ermittelt, welche Farbe beim Spielen einer Auswahlkarte mit vier Farben ausgewählt werden soll. Hierbei wird ermittelt, welche Farben am wenigsten bzw. gar nicht auf der Hand der eigenen KI vorkommen. Ziel dieser Strategie ist es die Wahrscheinlichkeit keine oder wenige Karten abwerfen zu müssen zu erhöhen. 

Zusätzlich zu der Auswahl einer bestimmten Farbe bei der Aktionskarte "Auswahl" wird berechnet, wie viele Karte zum Abwerfen für den ausgewählten Spieler bestimmt werden sollen. Hierbei wird der Fokus darauf gelegt. dass die eigene KI keinen Nachteil davon hat, einen bestimmten Wert ausgewählt zu haben, falls der Gegner auf diese Karte nichts abwerfen kann. Logischerweise wählt die KI somit den Wert 3 nur dann aus, wenn er selbst keine oder nur eine Karte selbst auf der Hand hat, sodass dieser in der nächsten Runde selbst nichts abwerfen kann. Besitzt der eigene Spieler zwei oder mehr Karten dieser Farbe, wählt die KI den Mittelwert zwei aus.

Zu Beginn des Zuges werden drei Listen mit den möglichen Sets/Aktionskarten ermittelt.<br>

In der ersten Liste befinden sich die möglichen Aktionskarten. Diese werden, sobald sie als spielbar eingestuft wurden, nach dem folgendem Prinzip sortiert. Die sicherste aber auch eine der effizientesten Karten ist die Aktionskarte "Neustart" (reset), da der nächste Spieler garantiert eine Karte abwerfen muss unabhängig von den Karten auf seiner Hand. Desweiteren bietet die Durchsichtskarte, solange die oberste Karte des Ablagestapels eine Wildcard oder eine Neustartkarte ist, den gleichen Effekt und wird bei der Sortierung deshalb bevorzugt. Anschließend kommt die Auswahlkarte, da dort die KI sich selbst Probleme machen kann, wenn der Gegner auf diese nichts abwirft, sowie die Durchblickskarte, wenn oben eine AuswahlKarte oder Zahlenkarte oben liegt.<br>

Anschließend werden die spielbaren Sets aus Zahlenkarten getrennt nach der Farbe der Karte auf dem Ablagestapel ermittelt. Handelt es sich bei der Karte um eine einfarbige Karte, ist die zweite Liste in diesem Fall leer.
Hierbei werden die Sets der möglichen Farbe der obersten Karte gefiltert. Die Strategie zur Filterung orientiert sich daran, möglichst viele Karten mit zwei Farben in einem oder mehreren Sets zu finden. Die Sets mit dem höchsten Anteil an zweifarbigen Karten werden dann abgespeichert (falls keine sets gefunden werden konnten, werden die beiden Listen nicht gefiltert).

Daraufhin wird aus beiden Listen der jeweiligen Farbe ermittelt, welche Farbe am wenigsten von beiden auf der eigenen Hand vorkommen (falls die Karte oben auf dem Ablagestapel einfarbig ist, wird dieser Schritt übersprungen). Hierbei geht es darum, möglichst effizient eine Farbe zu eliminieren, sodass die KI, sobald eine Karte dieser Farbe oben liegt, wahrscheinlicher eine Karte ziehen kann.

Im Anschluss wird aus den übrig geblieben Sets das Set ausgewählt, wo am meisten Wildcards enthalten sind. Wildcards können dazu führen, schneller ein Set vervollständigen zu können, weshalb versucht wird, diese schnell ablegen zu wollen.

Zum Schluss wird entschieden, ob eine Aktionskarte oder ein Set gespielt werden soll. Hat die oberste Karte auf dem Ablagestapel den Wert 1, wird, sofern eine Zahlenkarte auf der Hand vorhanden ist, diese auch gespielt. Ist der Wert dieser Karte größer als 1, spielt die KI (falls möglich) die Aktionskarte an der Spitze der Liste. Die Idee dahinter ist, anstatt zwei oder drei Karten abwerfen zu müssen, lieber nur die Aktionskarte abzuwerfen. 

Nachdem die Strategien nacheinander durchlaufen wurden, wird diese abhängig von ihrem Typ ausgespielt. Ist die Liste am Ende leer, kann die KI keine gültige Karte spielen und zieht entweder eine Karte oder sagt "Nope!" (wenn bereits gezogen wurde)

### <a id="#client2">Client2</a>
Beschreibung der Bedienung für Client2 um ein NOPE Spiel zu spielen

### <a id="#client3">Client3</a> 
(Autor: Julius Emil Arendt)

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
