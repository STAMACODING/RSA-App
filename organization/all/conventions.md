# Konventionen

## Sprache

Die Sprache für die _Dokumentation des Projekts_ (Wiki, Source-Code, Changelog, README.md) ist **Englisch**.

Die Sprache für die _Kommunikation_ (Issues, Kommentare, Protokolle, Tutorials, Nützliche Links) ist **Deutsch**.

Dateien und Ordner sollten immer in **Englisch** benannt werden.

## Lower camel case

Variablen, Dateien, Funktionen und Methoden sollten nach dem _Lower Camel Case_ Schema benannt werden.

#### Beispiele

```markdown
int thisIsAVariable;
iAmAFile.java
public void methodName()
Object helloObject02 = new Object();
```

## Upper camel case

Java-Klassen sollten dagegen dem _Upper Camel Case_ Schema folgen.

#### Beispiele

```markdown
public class MeineKlasse{
 (...)
}
MeineKlasse objektName = new MeineKlasse();
```

## Commits

Diese Konvention ist von [hier](https://chris.beams.io/posts/git-commit/) abgeleitet.

- Ein Commit hat _immer_ ein **Subject** und einen **Body** 

  ```markdown
  git commit -m "Subject" -m "Body"
  ```

- **Subject** ist eine kurze Zusammenfassung der Änderungen

  - Antwortet auf die Frage ''**Was?**''
  - Nutzt den **Imperativ**
  - Maximal rund **50 Zeichen**
  - Endet niemals mit einem Punkt

- **Body** ist ein erklärender Text

  - Erklärt die Problematik
  - Maximal rund **70 Zeichen**
  - Beinhaltet volle Sätze

#### Beispiele

```markdown
Commit #23433:
	Subject: "Add new feature"
	Body: "Remove the 'state' and 'exceptmask' from serialize.h's stream implementations, as well as related methods."
```
```markdown
Commit #f23a23:
	Subject: "Revert 'Add the thing with the stuff'"
	Body: "This reverts commit cc87791524aedd593cff5a74532befe7ab69ce9d."
```
