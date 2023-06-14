# SocketIO

## Einleitung

In diesem Dokument wird die Verbindung zum Server via SocketIO beschrieben.
Hier wird auf Events, Authentifizierung und das allgemeine Senden und
Empfangen von JSON-Datenpaketen eingegangen.

## Authentifizierung

Die Authentifizierung findet per JSON-Webtoken statt. Dieses spezielle Token erhält jeder User
vom Server und es existiert in der Datenbank lediglich ein einziges Mal.

Baut man eine Verbindung per SocketIO auf, wird dieses per Map an ein Optionsobjekt übergeben und dieses wiederum wird an die Websocketverbindung übergeben:

    // Build Authentification for JSON-Webtoken
    Map<String, String> map = Collections.singletonMap("token", token);
    IO.Options options = IO.Options.builder().setAuth(map).build();
    Socket socket = null;

    try {
        socket = IO.socket(Constants.DOMAIN.get(), options);
    } catch (URISyntaxException exception){
        return false;
    }

    try {
        socket = IO.socket(Constants.DOMAIN.get(), options);
    } catch (URISyntaxException exception){
        return false;
    }



Anschließend wird eine Verbindung zum Server aufgebaut via connect:

    options.forceNew = true;

    // Connect the Socket
    try {
        newInstance.connect(socket);
    } catch (InterruptedException exception){
        return false;
    }

## Events

Nach dem Aufbau der Verbindung muss auf bestimmte Events reagiert werden. Diese werden
in der Klasse ServerEventHandler.java registriert. Auf folgende Events wird 
entsprechend reagiert:

1. error: Die Fehlernachricht wird über den Logger ausgegeben.
2. eliminated: Die Eliminierungsnachricht wird über den Logger ausgegeben
3. gameInvite: Je nachdem, ob der User an einem Nope-Spiel teilnehmen möchte oder nicht, wird
   eine Entsprechende Antwort an den Server über das Event "ready" gesendet.
4. gameEnd: Das Spiel wird per Logger ausgegeben.
5. tournamentInvite: Je nachdem, ob der User an einem Nope-Turnier teilnehmen möchte oder nicht,     wird eine entsprechende Antwort an den Server über das Event "ready" gesendet.
6. tournamentEnd: Das Turnier wird mit allen Daten per Logger auf der Konsole ausgegeben.
7. gameState: jedes Mal, wenn sich etwas am aktuellen Spiel ändert, erhält der User ein Game-Objekt.
Wenn der User selbst am Zug ist, berechnet seine Strategie einen gültigen (und ggf. schlauen Zug). Dieser Zug wird als JSON-Object an den Server gesendet über das Event "playAction".

