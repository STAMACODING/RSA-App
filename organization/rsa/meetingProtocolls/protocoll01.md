# Meeting No. 3 (06.06.20, 18Uhr) 🚀

## Zusammenfassung Verschlüsselung

Ausgehend von der Idee, dass wir einen String haben, der die Nachricht enthält (z.B. ```String nachricht = "Hallo Welt!" ```), soll grob gesagt die Verschlüsselung im Gesamten so ablaufen:

1. Dieser String wird nun in einen **Array von Zahlen** (Datentyp: **long**) konvertiert. Das bedeutet, dass jedem Zeichen eine Zahl zugewiesen wird. 

   _Beispiel (Pseudocode)_

    ```Java
   String nachricht = "Hallo Welt!";
   'H' => 7
   'a' => 26
    ...
   long[] array = {7, 26, 37, 37, ...}
    ```
   
   Es wurde festgelegt, dass wir uns aus übertragungstechnischen Gründen auf **256 Zeichen** (Zahlen von 0 bis 255) begrenzen. Dies entspricht einem **unsigned byte** in Java.
   
2. Nun wird mit Hilfe des **öffentlichen Schlüssels** des Empfängers jeder **einzelne Eintrag** des  Arrays verschlüsselt. Das Schlüsselgenerierungsteam muss sicherstellen, dass das Modul n des Schlüssels kleiner gleich 65 536 ist.

   _Beispiel (Pseudocode)_

   ```Java
   {7, 26, 37, ...} => {2323, 2334, 1212, ...}
   ```
   
3. Um die Daten an den Server zu schicken, müssen sie mit Hilfe eines Arrays - aus **Byte-Zahlen** bestehend -  verschickt werden. Da die einzelnen Einträge im **Long-Array** aber zu groß für das Byte-Format sind, teilen wir jedem _Long-Eintrag_ **zwei Bytes** zu. Dies ermöglicht die maximale Zahl 65 536 und erklärt auch wieso die Modulgrenze auf diesen Wert begrenzt sein muss.

   _Beispiel (Pseudocode)_

   ```Java
   long[] encryptedData = {2323, 2334, 1212, ...}
   2323 => 0000100100010011 => [00001001; 00010011]
   2334 => 0000100100011110 => [00001001; 00011110]
   ...
   byte[] encryptedData = {00001001, 00010011, 00001001, 00011110, ...}
   ```

## Zusammenfassung Entschlüsselung

Die Entschlüsselung kehrt das um, was in der Verschlüsselung vollbracht wurde.

Wir haben ja nach der Verschlüsselung einen **Byte-Array**, in dem jeweils zwei aufeinanderfolgende Bytes zusammen ein verschlüsseltes Zeichen darstellen.

1. Daher müssen wir im ersten Schritt diese zusammengehörigen Bytes jeweils zu einer Zahl machen und alle Einträge in einem **Long-Array** speichern.

    _Beispiel (Pseudocode)_

      ```Java
   byte[] encryptedData = {00001001, 00010011, 00001001, 00011110, ...}
   [00001001; 00010011] => 0000100100010011 => 2323
   [00001001; 00011110] => 0000100100011110 => 2334
   ...
   long[] encryptedData = {2323, 2334, 1212, ...}
     ```

2. Danach können wir die einzelnen Zahlen mit unserem **privaten Schlüssel** entschlüsseln.
 _Beispiel (Pseudocode)_

   ```Java
   {2323, 2334, 1212, ...} => {7, 26, 37, ...}
   ```
   

3. Anschließend ordnen wir den Zahlen entsprechende **Zeichen** zu, sodass wir wieder den finalen String erhalten.

   ```Java
   long[] array = {7, 26, 37, 37, ...}
   7 => 'H'
   26 => 'a'
   ...
   String nachricht = "Hallo Welt!";
   ```

## Aufteilung in Sub-Teams

1. **Konvertierungsteam**
   - Für Konvertierungsfunktionen zuständig
     - jeweils Punkt 1 & 3 bei Ver- und Entschlüsselung
2. **Ver-/Entschlüsselungsteam**
   - Für die reine Ver- und Entschlüsselung zuständig
     - jeweils Punkt 2, jedoch _ohne_ die Schlüsselgenerierung
3. **Schlüsselgenerierungsteam**
   - Generiert private und öffentliche Schlüssel
     - Modul n darf dabei maximal die Zahl 65 536 sein
