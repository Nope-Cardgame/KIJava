# Rest

## Einleitung
In diesem Dokument wird beschrieben, wie der Client Requests an den Server senden kann, um bestimmte Informationen anzufordern.

## Aufbau einer einfachen Post-/Get-Request
Im unten stehenden Code wird die Methode dargestellt, die als Grundlage für das Erstellen dieser Rquests verwendet wird. Als Eingabeparameter wird hier die URL (urlString), 
das erzeugte WebToken (token) und die Art der Anfrage (requestType) benötigt.<br>

Der erste Teil der Methode stellt mithilfe der angegebenen Parameter eine Anfrage an die angegebene Adresse des Strings urlString. Hierbei kann zwischen einer Post- und einer Get-Request gewählt werden,
indem der passende Wert des Enums RequestType ausgewählt wird (POST oder GET). Zusätzlich wird bei der Anfrage automatisch das Token, welches der Server dem Client bei der Verbindung mitgegeben wurde, mitgeschickt.

      public static void request(String urlString, String token, RequestType requestType) throws IOException {

        LOG.info("current Connection: \n" + urlString);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // element and web token
        con.setRequestMethod(requestType.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + token);

Der zweite Teil ist dafür zuständig, den HTTP-Statuscode für die Anwendung und die Antwort des Servers z.B. für die Darstellung durch den Logger in der Konsole verfügbar zu machen.


        con.setDoOutput(true);

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }
    }
    
Diese Methode wurde für bestimmte Zwecke angepasst, sodass es bei einer ähnlichen Methode noch möglich ist, eine ID mitzugeben. Diese ID wird bei bestimmten Anfragen dafür benötigt, um z.B. nach bestimmten
Spielen oder Turnier anhand der gegebenen ID zu suchen. Zusätzlich wird der Ansatz dafür verwendet, in der GUI Spiele und Turniere zu starten. Hierbei werden neben dem normal verwendeten Header auch die benötigten 
Informationen (wie Liste der Spieler und die Einstellungen für das Turnier/Spiel mitgeschickt.
