<h1>GUI-Bedienungsanleitung</h1>

<h2>Einleitung:</h2>
Dieses Dokument behandelt den Aufbau des Graphical User Interfaces sowie die Funktion der verwendeten Java Swing Komponenten. 
Mithilfe einer Schritt-für-Schritt-Anleitung wird dem Nutzer erklärt, wie die gewünschte Funktionalität erreicht wird, und die GUI im Allgemeinen zu bedienen ist. 

<h2><br>Funktionen der GUI:</h2>
Im folgenden Absatz wird die Funktionalität der GUI beschrieben. In den beigefügten Bildern werden die einzelnen Elemente mit einem farbigen Rechteck und einer Zahl versehen, die sich hinter den einzelnen Stichpunkten wiederfinden lassen und dort die Funktion beschreiben.
<h3><br>Allgemein:</h3>
<ul>
<li>Darstellung des aktuellen Verbindungsstatus <b>(1.)</b></li>
</ul>
<h3>Anmelden/Registrieren:</h3>
<img src=".\images\gui_login.png">
<ul>
<br><li>Eingabe des Nutzernamens und des Passwortes <b>(2. und 3.)</b></li>
<li>Auslesen und automatisches Einfügen des gespeicherten Nutzers in die Eingabefelder</li>
<li>Anmelden mit den eingegeben Nutzerdaten an dem Server <b>(4.)</b></li>
<li>Registrierung eines neuen Nutzers <b>(5.)</b></li>
<li>Speichern der Nutzerdaten eines Nutzers <b>(6.)</b></li>
<li>Auswahl der zu verwendeten KI <b>(7.)</b></li>
</ul>
<h3>Spiel:</h3>
<img src=".\images\gui_game.png">
<ul>
<br><li>Darstellung der bereits gespielten Züge (Spielername, Aktion und Anzahl an Handkarten) in einer chronologischen Liste <b>(2.)</b></li>
<li>Anzeige der Handkarten des Spielers <b>(3.)</b></li>
<li>Darstellung der obersten Karte des Ablagestapels <b>(4.)</b></li>
<li>Informationen über die aktuelle Runde und den aktuellen Spieler <b>(5.)</b></li>
<li>Aktualisieren der Spielerliste zur Laufzeit <b>(6.)</b></li>
<li>Liste der aktuell verbundenen Clients zum Server <b>(7.)</b></li>
<li>Ein oder mehrere Spieler in eine Liste hinzufügen, um sie später einladen zu können <b>(8.)</b></li>
<li>Entfernen der ausgewählten Spieler aus der Liste <b>(9.)</b></li>
<li>Zeigt die zur Liste hinzugefügten Spieler an<b>(10.)</b></li>
<li>Lädt die sich in der Liste befindenden Spieler zu einem Spiel ein <b>(11.)</b></li>
<li>Einstellungsmöglichkeiten für das Spiel/Turnier <b>(12.)</b></li>
<li>Das Spiel lässt sich zur Laufzeit verzögern, um den Spielfluss besser nachvollziehen zu können <b>(13.)</b></li>
<li>Lädt die sich in der Liste befindenden Spieler zu einem Turnier ein <b>(14.)</b></li>
<li>Darstellung der eigenen Verbindung zum Server<b>(15.)</b></li>
</ul>

<h2><br>Verwendete Texturen in der GUI:</h2>
Im folgenden Kapitel werden die Texturen dargestellt, die für die Darstellung des Spiels benötigt werden.
<h3><br>Karten:</h3><br>
<img src=".\images\cards.gif"/>
<h3><br>Verwendete Textur, falls keine Karte gefunden wurde:</h3><br>
<img src=".\images\error.png" />
<br><h3><br>Hintergrund:</h3><br>
<img src=".\images\background.png"/>
<h3><br>Anzeige, dass der Nutzer eliminiert wurde:</h3>
<img src=".\images\eliminated.png"/>

<br><h2>Schritt-für-Schritt-Anleitung:</h2>
Beim ersten Ausführen des Programms ist kein Nutzer standartmäßig hinterlegt, sodass es sinvoll ist, (falls noch nicht vorhanden) einen Nutzernamen und ein Passwort in die Textfelder einzugeben und sich mit dem Knopf "Register" einen neuen Nutzer anzulegen. Dieser lässt sich vor dem Registrieren mit dem Knopf "Save data" abspeichern, sodass dieser bei den nächsten Starts des Programms dauerhaft hinterlegt bleibt (ist auch mit einem bereits registrierten Nutzer möglich). Wichtig hierbei ist, dass sich jeweils nur der letzte abgespeicherte Nutzer automatisch in die Textfelder laden lässt. Soll ein bereits registrierter Nutzer noch einmal verwendet werden, muss nun eine Anmeldung mit dem Knopf "Log in" durchgeführt werden.<br>
Bei erfolgreicher Registrierung/Anmeldung werden die Elemente, die für die Verbindung zum Server benötigt werden, deaktiviert und die Elemente für die Erstellung und Darstellung des Spiels aktiviert. In der Liste bei Punkt <b>7.</b> werden die verbundenen Clients aller Nutzer angezeigt. Diese Liste lässt sich mit dem Knopf "Reload player list" aktualisieren. Anschließend können die gewünschten Spieler durch Anklicken ausgewählt und über den Knopf "Add marked player to list" zu einer Liste <b>(10.)</b> hinzugefügt werden (lässt sich durch das Markieren der unerwünschten Spieler und dem Knopf "Remove marked player from list" rückgängig machen). Mit dem Knopf "Invite players to game" oder "Invite players to tournament" lässt sich ein Spiel bzw. Turnier mit den getroffenen Einstellungen und den ausgewählten Spielern starten. Hierbei ist es wichtig, dass mindestens ein Spieler in der Liste sein muss, damit das Spiel/Turnier gestartet werden kann.<br>
Anschließend sollte das Spiel beginnen und die Handkarten, die Karte auf dem Ablagestapel und die aktuellen Spielinformationen auf der linken Seite dargestellt werden. Zusätzlich werden diese Informationen ausführlicher in der Tabelle oberhalb <b>(2.)</b> dargestellt. Mit der JComboBox unterhalb der Schrift DELAY, lässt sich zur Laufzeit der Spielzug verlangsamen, um den Spielablauf besser nachvollziehen zu können.
Nachdem ein Spieler das Spiel gewonnen hat, können neue Spieler eingeladen werden, die Einstellungen geändert und neue Spiele und Turniere gespielt werden.

<br>
<h2>Weitere Bilder der GUI:</h2>
<img src=".\images\gui_example.png"/>
