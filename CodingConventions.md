# Coding Conventions (Vorschlag):

- Aufbau Klassen:

public class Irgendeineklasse {
    
    // Hier kommen die Attribute hin von einer Klasse

	// Hier kommen alle Konstrukturen einer Klasse

	// Hier kommen alle anderen Funktionen hin

	// Hier kommen nur getter und setter (kein JavaDoc benötigt, name sagt ja schon, was es macht)
}

- Coding-Sprache:
  Englisch

- Sonstiges:

-------------------------------------------------------------------------------------------------------
	Leerzeichen bei langen arithmetischen Ausdrücken für bessere
	lesbarkeit, ggf Arithmetische ausdrücke in andere Funktionen auslagern
	
	Beispiel:

	Falsch: i+=(a*i*i)/1+2-5*(3-1)
	Richtig: i += (a * i * i) / 1 + 2 - 5 * (3 - 1)

	
-------------------------------------------------------------------------------------------------------
	Leerzeichen bei Kontrollstrukturen für angenehmere Lesbarkeit

	Falsch: for(int i = 0; i < arr.length; i++){

		}

	Richtig: for (int i = 0; i < arr.length; i++) {

		 }

-------------------------------------------------------------------------------------------------------
	Bei Kontrollstrukturen IMMER alles in geschweifte Klammern

	Falsch: if (bitVector[i]==false) 
		    callsomeBadassFunction(bitvector[i]);

	Richtig: if (bitvector[i] == false) {
		     callSomeBadassFunction(bitvector[i]);
		 }

-------------------------------------------------------------------------------------------------------	
	Schleifen-iteratoren nicht einfach nur i und j nennen (auch bei for-each-Schleifen)

	Falsch: for(int i = 0; i < arr.length; i++) {}
	Richtig: for (int currentArrIdx = 0; i < arr.length; i++) {}

-------------------------------------------------------------------------------------------------------

	Geschweifte Klammern nicht nächste Zeile bei Schleifenkopf

	Falsch: for(int i = 0; i < arr.length; i++) 
		{

		}

	Richtig: for (int arrayIdx = 0; arrayIdx < arr.length; arrayIdx++) {
		 
		 }

-------------------------------------------------------------------------------------------------------
	