# Javadoc Tutorial
In dieser Datei werden dir die Grundlagen vermittelt, um deine Funktionen, Klassen und Variablen in Zukunft ordentlich zu dokumentieren.

## Wieso das Ganze?

1. Andere können so deinen Code deutlich besser und schneller nachvollziehen. Im Umkehrschluss kannst auch du den Code von anderen besser nachvollziehen.
2. Auch du selbst kannst nach ein bisschen Zeit schnell wieder verstehen, was dein Code eigentlich macht, wenn du ihn sauber dokumentiert hast.
3. Das ganze Projekt bekommt dadurch eine einsehbare logische Struktur.

Dadurch können **Bugs verhindert** werden und im Gesamten **spart man sich die Zeit** ein, die ansonsten für das (Wieder-)Einlesen in den Code, Team-Mitglieder-Befragen und Fixen-Der-Bugs draufgegangen wäre.

## Schritt 1: Javadoc ist kein Hexenwerk

Das Tolle an Javadoc ist, dass es so einfach ist. Das heißt mit wenig Aufwand kannst du die obigen Vorteile für dein Projekt dazugewinnen. In den meisten Fällen brauchst du nur **1-3 Schlüsselwörter** - und die werden auch noch vollkommen auf Englisch ausgeschrieben. Besser geht's wohl kaum?

## Schritt 2: Javadoc = Normale Kommentare + Struktur

Als zweiten Punkt will ich dir nochmal die altbekannten Wege, deinen Code zu dokumentieren, in Erinnerung rufen.

```java
public static int add(int a, int b){
    int z = a + b;
    return z;
}
```
Stell dir mal vor, du hättest die Funktion `add(int a, int b)` geschrieben. Sie addiert zwei Zahlen und gibt das Ergebnis zurück. 
Jetzt möchtest du sie auf altbekanntem Weg *dokumentieren*. Wir erinnern uns:

- einzeilige Kommentare gingen so:
```java
// Einzeiliger Kommentar
```
- mehrzeilige Kommentare auf diese Weise: 
```java
/*
 *  Mehrzeiliger
 *  Kommentar
 */
```
Deine dokumentiere Funktion könnte also so aussehen:
```java
/*
 * Addiert zwei Zahlen. A und b sind ganze Zahlen. Gibt die Summe der Zahlen zurück.
 */
public static int add(int a, int b){
    // Addiert a und b und speichert es in z
    int z = a + b;
    // Gibt z zurück
    return z;
}
```
Ist ganz nützlich, aber so richtig strukturiert ist das doch nicht? Mit **Javadoc** könnte das Ganze so aussehen:
```java
/**
 * Addiert zwei Zahlen.
 * @param a die erste Zahl
 * @param b die zweite Zahl
 * @return die Summe der Zahlen
 */
public static int add(int a, int b){
    int z = a + b;
    return z;
}
```
Irgendwie schon besser, oder?

Und es kommt noch besser: Bevor wir zu den genauen Erklärungen der `@tag` Dingern kommen, möchte ich dir noch einen oder sogar zwei Vorteile von **Javadoc** erklären.

1. Eclipse zeigt die Erklärungen der Funktion richtig übersichtlich an, wenn wir die Funktion irgendwo nutzen wollen und mit dem Cursor über sie fahren.

    ![Bild, das zeigt, was Eclipse so Cooles macht](https://i.ibb.co/XCHCfbD/Unbenannt.png)

2. Wir können später mit nur einem Klick eine .html-Seite generieren, die unsere Erläuterungen grafisch besser darstellt.

    ![Bild, das zeigt, wie die Funktion als Teil einer .html-Seite dargestellt wird](https://i.ibb.co/4KPSynD/Unbenannt2.png)

Aber genug der Features und der Betonung der Einfachheit! Wie funktioniert das ganze denn jetzt?!

## Schritt 3: Selbst Javadoc-Kommentare schreiben

1. Javadoc-Kommentare zeichnen sich syntaktisch dadurch aus, dass die eigentlich wie ein normaler mehrzeiliger Kommentar aussehen, aber zu Beginn zwei von diesen '*' haben.
    ```java
    /**
    * Javadoc-
    * Kommentar
    */
    ```
    ```java
    /*
    * Normaler mehrzeiliger
    * Kommentar
    */
    ```
2. Zudem können sie nicht wie normale Kommentare innerhalb von Funktionen stehen. Nein, sie sind nur an drei Orten zu finden:

    - Vor der Definition einer Klasse
        ```java
        /**
        * Dies ist eine Test-Klasse. Sie tut rein gar nichts.
        */
        public class Test{
        ...
        ```
    - Vor der Definition einer Variable
        ```java
        /** Diese Variable beschreibt das Alter meiner Katze. */
        private int alterKatze;
        ...
        ```
    - Vor der Definition einer Funktion/Methode
        ```java
        /**
        * Diese Funktion addiert zwei Zahlen.
        */
        public static int add(int a, int b){
        ...
        ```
3. Es gibt coole **Tags**, womit wir unsere Beschreibung der Klasse/Funktion/Variable besser gliedern können. Die wichtigsten Tags hier kurz erklärt:
    - Mit dem `@author Autor`-Tag können wir beschreiben, wer den Code geschrieben hat.
    - Mit dem `@return Beschreibung`-Tag beschreiben wir bei Funktionen, die etwas zurückgeben (also nicht _void_), was denn genau zurück gegeben wird.
    - Mit dem `@param Parametername Beschreibung`-Tag können wir je einen Parameter genauer beschreiben.
    - Mit `{@code System.out.println("Hallo Welt")}` können wir innerhalb unserer Erklärungen auf Code zurückgreifen.
    - Mit `{@link Klasse#funktion()}` können wir auf andere Funktionen, Variablen oder Klassen verlinken.

## Schritt 4: Ein paar Beispiele

Nun ein paar Beispiele, die zeigen, wie man **Javadoc**-Kommentare in der Praxis verwendet. Wichtig ist hier übrigens, dass alles in **Englisch** dokumentiert sein sollte 😉.

```java
/**
* Adds two numbers.
* @param a the first number
* @param b the second number
* @return the sum
* @author Klaus Günter
*/
public static int add(int a, int b){
    int z = a + b;
    return z;
}
```
```java
/**
 * An instance of this class represents a message with all its different attributes.
 * @author Klaus Günter
 */
public class Message{
```
```java
/** The server's unique public ip-address */
public static String IP = "192.344.122";
```
