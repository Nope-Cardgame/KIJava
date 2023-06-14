<h1>Package Struktur</h1>

<h2>Einleitung:</h2>
Dieses Dokument beschreibt den Aufbau der einzelnen Packages und gibt Auskunft darüber, welche Klassen mit welchen Funktionalitäten in welchen Packages zu finden sind.


<img src=".\images\package_structure.png">
<ul>
<br><li>Doku: enthält alle wichtigen Informationen und Bilder über das Projekt selbst <b></b></li>
<li>Sprites: enthält alle Bilder, die von der Gui verwendet werden</li>
<li>src: enthält gesamten Client Code für Funktionalität und Tests <b></b></li>
</ul>
<h3>Inhalt des src-Packages:</h3>
Das src-Package enthält den gesamten Programm- und Testcode des Clients. Darin enthalten sind die Packages main und test. main enthält alle
Klassen, um das Programm auszuführen. Das Test-Package enthält wichtige JUnit-Tests, um die Richtig der Funktionalität abzusichern. Getestet werden damit
alle Game-Objects sowie die KIs.
<h3>Inhalt des main-Packages:</h3>
<ul>
<br><li>AI: Dieses Package enthält die Klassen der verschiedenen Spielerbots sowie eine AI-Factory, die Objekte der einzelnen KI-Klassen anlegen kann <b></b></li>
<li>commmand: Dieses Package enthält den ActionListener, der in der Gui verwendet wird, umd das Anklicken der Buttons abzufragen bzw. mit entsprechenden Actionen zu reagieren <b></b></li>
<li>event handler: Dieses Package enthält die Klassen der Events wie beispielsweise Gameinvitation. Der EventHandler ist für die Kommunikation zwischen Server und Client verantwowrtlich.<b></b></li>
<li>game_objects: Dieses Package enthält die Klassen für alle Spielobjekte wie Game, Mode, Player, Tournament sowie zwei weitere Packages cards mit den Klassen für
  die Karten und das Package action für die Game-Actions. game objects stellen Spiezlzüge eines Spielers dar, wie beispielsweise das Ziehen oder Legen von Karten></b></li>
<li>logging: Dieses Package enthält den Formatter und Logger für die Konsolenausgabe. <b></b></li>
<li>logic: Dieses Package enthält die Klassen, die den grundlegenden Programmablauf bestimmen. Dazu gehören unter anderem die Klassen Main, Constants, Rest und ConnectionHandler  <b></b></li>
<li>view: Dieses Package enthält die Klassen für die Ausgabe in der graphischen Benutzeroberfläche. Dazu gehören die Klassen Gui, die das Gerüst für die graphische 
  Anzeige bietet, die Klasse ComponentPainter, die die Anzeige der Karten verwaltet sowie die Klasse ImageLoader, die die Dateien für die Kartenbilder lädt.<b></b></li>




