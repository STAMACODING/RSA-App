Meeting No. 3 (12.07.20, 18Uhr) 🚀


* Änderungen hauptsächlich für das Ver- und Entschlüsselunsgteam: (siehe PowerPoint von Henri, RSA-App/organization/all/rsa.pptx)

    - eine Nachricht (Message) besteht aus 3 Teilen LocalData localData, ProtectedData protectedData und ServerData serverData

      -> es werden nur die letzteren ver- und entschlüsselt


    - generell man Objekte direkt in Byte-Arrays umwandeln

      -> das heißt zum Beispiel für die Verschlüsselung, wir müssen nicht wie vorher die Nachricht, gespeichert im long-Dateityp, verschlüsseln, 
         in eine Zahl im Binärsystem umwandeln und halbieren (also die ersten und die letzten 8 Stellen der verschlüsselten Zahl)

      -> stattdessen können wir ein Objekt direkt in einen Byte-Array umwandeln, den dann in eine long konvertieren und die long dann verschlüsseln 
         (der Vorteil ist, dass wir die long nicht in eine Binärzahl wandeln und halbieren müssen, da wir schon wissen, dass die Zahl vorher in einem Byte-Array war
         und somit auch wieder einer werden kann, ohne dass Daten verloren gehen)

      -> die Entschlüsselung funktioniert einfach entgegengesetzt zur Verschlüsselung
   
   
* Lokale Einrichtung des Repositorys besprochen (also das Git-Tutorial)
