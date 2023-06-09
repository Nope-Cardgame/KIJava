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

Die erste verwendete Strategie besagt, dass immer der Spieler ausgewählt werden soll, der die meisten Handkarten besitzt (ohne die eigne KI). Hierbei ist die Wahrscheinlichkeit am größten, dass der ausgewählte Spieler Karten abwerfen muss und die Wahrscheinlichkeit, selbst Karten abwerfen zu müssen, am geringsten ist. Diese Strategie wird immer dann verwendet, wenn die Aktionskarte "Auswahl" (nominate) gespielt wurde oder diese Aktionskarte zu Beginn des Spiels auf dem Ablagestapel liegt und die eigene KI auf diese Karte reagieren muss. Im gespielten Turnier spielt diese Strategie eher eine untergeordnete Rolle, da sich in einem 1 gegen 1 die Auswahlmöglichkeiten auf eine Person beschränken. Lediglich im Stechen (Sudden Death) um einen bestimmten Platz wird diese Strategie eingesetzt.

Eine weitere Strategie ermittelt, welche Farbe beim Spielen einer Auswahlkarte mit vier Farben ausgewählt werden soll. Hierbei wird ermittelt, welche Farben am wenigsten bzw. gar nicht auf der Hand der eigenen KI vorkommen. Ziel dieser Strategie ist es die Wahrscheinlichkeit keine oder wenige Karten abwerfen zu müssen zu erhöhen.

Zusätzlich zu der Auswahl einer bestimmten Farbe bei der Aktionskarte "Auswahl" wird berechnet, wie viele Karte zum Abwerfen für den ausgewählten Spieler bestimmt werden sollen. Hierbei wird der Fokus daraufgelegt. dass die eigene KI keinen Nachteil davon hat, einen bestimmten Wert ausgewählt zu haben, falls der Gegner auf diese Karte nichts abwerfen kann. Logischerweise wählt die KI somit den Wert 3 nur dann aus, wenn er selbst keine oder nur eine Karte selbst auf der Hand hat, sodass dieser in der nächsten Runde selbst nichts abwerfen kann. Besitzt der eigene Spieler zwei oder mehr Karten dieser Farbe, wählt die KI den Mittelwert zwei aus.

Zu Beginn des Zuges werden drei Listen mit den möglichen Sets/Aktionskarten ermittelt.

In der ersten Liste befinden sich die möglichen Aktionskarten. Diese werden, sobald sie als spielbar eingestuft wurden, nach dem folgenden Prinzip sortiert. Die sicherste aber auch eine der effizientesten Karten ist die Aktionskarte "Neustart" (reset), da der nächste Spieler garantiert eine Karte abwerfen muss unabhängig von den Karten auf seiner Hand. Des Weiteren bietet die Durchsichtskarte, solange die oberste Karte des Ablagestapels eine Wildcard oder eine Neustartkarte ist, den gleichen Effekt und wird bei der Sortierung deshalb bevorzugt. Anschließend kommt die Auswahlkarte, da dort die KI sich selbst Probleme machen kann, wenn der Gegner auf diese nichts abwirft, sowie die Durchblickskarte, wenn oben eine Auswahlkarte oder Zahlenkarte oben liegt.

Anschließend werden die spielbaren Sets aus Zahlenkarten getrennt nach der Farbe der Karte auf dem Ablagestapel ermittelt. Handelt es sich bei der Karte um eine einfarbige Karte, ist die zweite Liste in diesem Fall leer. Hierbei werden die Sets der möglichen Farbe der obersten Karte gefiltert. Die Strategie zur Filterung orientiert sich daran, möglichst viele Karten mit zwei Farben in einem oder mehreren Sets zu finden. Die Sets mit dem höchsten Anteil an zweifarbigen Karten werden dann abgespeichert (falls keine Sets gefunden werden konnten, werden die beiden Listen nicht gefiltert).

Daraufhin wird aus beiden Listen der jeweiligen Farbe ermittelt, welche Farbe am wenigsten von beiden auf der eigenen Hand vorkommen (falls die Karte oben auf dem Ablagestapel einfarbig ist, wird dieser Schritt übersprungen). Hierbei geht es darum, möglichst effizient eine Farbe zu eliminieren, sodass die KI, sobald eine Karte dieser Farbe oben liegt, wahrscheinlicher eine Karte ziehen kann.

Im Anschluss wird aus den übrig geblieben Sets das Set ausgewählt, wo am meisten Wildcards enthalten sind. Wildcards können dazu führen, schneller ein Set vervollständigen zu können, weshalb versucht wird, diese schnell ablegen zu wollen.

Zum Schluss wird entschieden, ob eine Aktionskarte oder ein Set gespielt werden soll. Hat die oberste Karte auf dem Ablagestapel den Wert 1, wird, sofern eine Zahlenkarte auf der Hand vorhanden ist, diese auch gespielt. Ist der Wert dieser Karte größer als 1, spielt die KI (falls möglich) die Aktionskarte an der Spitze der Liste. Die Idee dahinter ist, anstatt zwei oder drei Karten abwerfen zu müssen, lieber nur die Aktionskarte abzuwerfen.

Nachdem die Strategien nacheinander durchlaufen wurden, wird diese abhängig von ihrem Typ ausgespielt. Ist die Liste am Ende leer, kann die KI keine gültige Karte spielen und zieht entweder eine Karte oder sagt "Nope!" (wenn bereits gezogen wurde).



### <a id="#client2">Client2</a>
(Autor: Alexander Lauruhn)<br>
Der Algorithmus für diesen Client beruht auf einer risikoreichen Strategie, die darauf abziehlt, den Gegner zu Beginn des Spiels mit Nominierungen zu schwächen, bei denen 3 Karten verlangt werden. Der Client spielt Nominate-Karten immer aus, wenn diese verfügbar sind. Hat der Gegner mehr als 4 Karten, werden immer 3 Karten verlangt. Hat der Gegner weniger Karten, wird entsprechend mit dem Wert 2 oder 1 nominiert. Sofern die Möglichkeit besteht, beim Nominieren eine Farbe auszuwählen, so wird immer die Farbe gewählt, die der Client selbst am wenigsten hat. Damit soll verhindert werden, dass der Client selbst die Nominierung bedienen muss, sofern der Gegner dies nicht kann. Eine Abfrage, ob der Client selbst eine Nominierung bedienen kann, um damit das Risiko eines Kartenverlusts zu vermeiden, wurde nicht implementiert, da insbesondere das Nominieren zu Spielbeginn fast immer erfüllt wird und dem Gegner großen Schaden zufügt. Eine Selbstnominierung mit dem Ziel, Karten aufnehmen zu dürfen, weil man die Nomierung nicht erfüllen kann, wurde geprüft, aber aufgrund schlechterer Spielergebnisse verworfen. 

Sofern der Client über keine Nominierungskarte verfügt, werden (sofern vorhanden) invisible oder reset-Karten gespielt. Dadurch, dass Actionkarten immer gespielt werden, wenn welche vorhanden sind, hat den Vorteil, dass keine Actioncard gemeinsam mit Numbercards ausgespielt und damit verworfen wird. 
Sofern keine Actioncards vorhanden sind, muss der Client Numbercards ausspielen. Hierzu werden die Karten des Clients absteigend nach der Anzahl der Farben sortiert (zuerst vierfarbige Karten, dann zweifarbige, dann einfarbige). Damit wird sichergestellt, dass der Client nach einigen Spielzügen nur noch einfarbige Karten hat. Damit hat der Client dann bessere Changen, eine Forderung nicht bedienen zu können und dadurch weitere Karten über die Nope-Action zu erhalten.
Dieser Algorithmus basiert auf Erfahrungswerten und hat sich durch mehrfachen Spielen gut bewährt.


### <a id="#client3">Client3</a> 
(Autor: Julius Emil Arendt)

Implementierung der Strategie in: AIJulius.java sowie JAISmart.java

Prinzipiell wird bei einer Partie Nope die Strategie der KI folgendermaßen unterteilt:
einmal in Züge, die gespielt werden, wenn noch keine Karte von der KI genommen wurde
und Züge, die gemacht werden, wenn bereits eine Karte genommen worden ist.
Beide Unterteilungen sind im Prinzip gleich, nur dass bei der einen, wenn man keine Karte ablegen muss, eine Karte vom Stapel genommen wird, und bei der anderen wird einfach ein Nope-Json zurückgegeben.

Wenn bisher noch keine Karte genommen worden ist, muss geprüft werden, ob eine Auswahlkarte alleine auf dem Stapel
liegt, dann darf die KI einen anderen Spieler auswählen, ohne selbt eine Karte legen zu müssen.
Hier erfolgt die Auswahl schlau: die KI wählt immer den Spieler, der am wenigsten Karten auf der Hand hat, damit dieser aus dem Spiel schneller ausscheidet.
Selbstverständlich wird auch darauf geachtet, dass die KI sich nicht selbst auswählt.

Wenn die Auswahl-Karte alle vier Farben hat, wählt die KI die Farbe, die am wenigsten auf der eigenen Hand vorhanden ist. Die Wahrscheinlichkeit ist hoch, dass der gegnerische Spieler die Farbe und die Anzahl an Karten auf seiner Hand hat. Ebenfalls wird dadurch verringert, dass die KI selbst möglicherweise ein Kartenset ablegen muss.

Bei jeder Auswahl-Aktion, die selbst getätigt wird, verlangt die KI immer 2 Karten oder wenn der gegnerische Spieler nur noch eine Karte auf der Hand hat, 
eine Karte. Zwei Karten sind der Median der Zahlenmenge 1 bis 3 und daher ein gutes Mittelmaß für das nominieren von Karten.

Die KI arbeitet stets nach diesem Verfahren, wenn sie eine Auswahl-Karte auf der Hand hat.

Wenn eine Karte abgelegt werden muss, zum Beispiel, wenn die Durchblickkarte als einzige Karte oben liegt oder ein Joker oder ein Neustart oben liegt, dann wird immer die aktuell beste Karte aus der Hand gesucht (entweder mit Farbe, beispielsweise, wenn nur eine einzige Invisible liegt) oder ohne Farbe.
Dabei wird folgendermaßen gearbeitet: 

  - zuerst Neustart-Karten (der Andere Spieler muss definitiv eine Karte ablegen), dadurch
    sinkt dadurch die Wahrscheinlichkeit, dass die KI ein vollständiges Set besitzt
  - dann Auswahl-Karten (Spieler muss mehrere Karten ablegen, ggf. von einer bestimmten Farbe)
  - dann Joker (Spieler muss definitiv eine Karte ablegen, ggf. geringere Wahrscheinlichkeit auf    Set-Vervollständigung)
  - Multiple Farben bei Zahlenkarten (die Wahrscheinlichkeit wird gesenkt, sodass die KI kein vollständiges Set mehr auf der Hand hat)
  - ganz zum Schluss normale Farbenkarten

Ansonsten wenn oben einfach eine Zahlenkarte liegt, wird, ob ein Set vollständig ist, dabei ist es erstmal egal,
ob das Set nur aus Nummernkarten oder aus Aktionskarten besteht. Ist kein gültiges Set vorhanden, kann entweder je nach Lage eine Karte genommen werden oder "nope" gesagt werden.

Ist jedoch ein Set vorhanden, wird erst nachgeschaut, auf dem Ablagestapel eine zweifarbige Nummernkarte liegt. Ist das der Fall, wird für jede Farbe geprüft, ob ein vollständiges Set vorhanden ist. Wird eine Farbe gefunden, auf welche dies zutrifft, merkt sich die KI diese für das Set.

Anschließend schaut die KI, ob sie Aktionskarten auf der Hand hat von dieser Farbe, Denn es sollen möglichst wenig Karten auf einmal abgeworfen werden, und bei einem vollständigen Set kann die KI ggf. eine Aktionskarte abwerfen und so sofort ihren Zug beenden.

Hat die KI eine Aktionskarte, prüfe sie, ob sie eine Auswahlkarte hat, ist das der Fall, arbeitet sie nach der oben genannten Strategie für Auswahlkarten,
ansonsten legt die KI die Aktionskarte regulär auf dem Ablagestapel ab.

Besitzt die KI jedoch keine Aktionskarte, wird von ihr ein Set erstellt und sie wirft dieses Set ab. Das Set wird schlau erstellt, indem zuerst nach Jokern, dann nach zweifarbigen Zahlenkarten, und schlussendlich nach einfarbigen Zahlenkarten gesucht wird und die entsprechend in einer Liste gespeichert werden. Somit verringert sich die Wahrscheinlichkeit, dass die KI möglicherweise ein vollständiges Set auf der Hand hat und in Zukunft haben wird.

Liegt oben auf dem Kartenstapel eine Aktionskarte, zum Beispiel Neustart, dann hole sich die KI einfach die aktuell schlaueste Karte, wie oben
bereits beschrieben. Wenn jedoch eine Auswahl-Karte oben liegen sollte, arbeitet die KI ähnlich wie bei den Zahlenkarten.