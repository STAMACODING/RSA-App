# Setup-Tutorial

Dieses Tutorial fasst **alles**, was du herunterladen, installieren und einrichten musst, zusammen, um bei diesem Projekt mitzumachen. Diese Anleitung gilt für Windows-Systeme (64bit).

## 1. Git Installieren

Git ist das Versionsverwaltungssystem, was wir nutzen, um die Entwicklung unseres Projekts in Versionen einzuteilen (mehr dazu [hier](https://docs.google.com/presentation/d/1CcJrfBZer-sNxg7leW6UvKjbexlPjSmiAAHfRJONaNM/edit#slide=id.g85a68de936_0_2)). Du musst Git auf deinem Rechner installieren, um unsere Projekt-Dateien vom Github-Server herunterzuladen, einfach zu bearbeiten und deine gemachten Änderungen wieder hochzuladen.

Unter Windows ist es sinnvoll _Git Bash_ zu installieren. Hier eine Schritt für Schritt Anleitung:

#### 1. Git Bash downloaden

Gehe auf diese [Webseite](https://gitforwindows.org/). Dann klicke auf **Download** um den Installer zu downloaden.

#### 2. Git Bash installieren

_Öffne_ den Installer (er müsste ungefähr den Namen _Git-2.26.2-64-bit.exe_) haben. _Akzeptiere_ die "GNU General Public License", _setze_ einen beliebigen Installationspfad für das Git Programm, _drücke_ solange "Next" bis du bei "Install" ankommst und _installiere_ Git.

## 2. Git einrichten

_Öffne_ jetzt einfach die Git Bash (Windows) oder das Terminal (Mac, Linux).

###### Nutzernamen einrichten

Gebe folgenden Befehl ein, um deinen Nutzernamen festzulegen.

```shell
git config --global user.name [Nutzername]
```

"[Nutzername]" ersetzt du bitte mit deinem Nutzernamen auf _Github_. 

_Beispiel_ 

```shell
git config --global user.name harrydehix
```

###### Email-Adresse einrichten

Danach gibst du bitte noch diesen Befehl ein.

```shell
git config --global user.email [Email-Adresse]
```

"[Email-Adresse]" ersetzt du bitte mit der Email-Adresse, die du in _Github_ verwendest.

_Beispiel_ 

```shell
git config --global user.email harry.holly02@gmail.com
```

## 3. Unser Repository klonen

Um unsere Projektdateien auf deinen Computer zu ziehen, musst du unser Repository "klonen". 

1. Dazu navigierst du mit dem File Explorer in den Ordner, wo du das Projekt eben gespeichert haben willst. 

2. Dann öffnest du in diesem Ordner per Rechtsklick die Git Bash (Windows) oder das Terminal (Linux, Mac OS). 

3. Danach gibst du folgenden Befehl ein:

   ```shell
   git clone https://github.com/STAMACODING/RSA-App.git
   ```

## 4. Oracle JDK 8 installieren

Damit wir alle mit dem selben Java arbeiten solltest du das JDK 8 auf deinem Rechner installieren. Die Installationsdatei habe ich [hier](https://mega.nz/file/BLIUmLzA#4h9a8CrRtnb_A6u43egoM19EwXZkH9XsDRJaZHNSPPw) hinterlegt. Downloade einfach die _.exe_-Datei und führe den Installer aus.

Falls du schon ein anderes JDK installiert hast ist das kein Problem. Man kann problemlos mehrere JDKs gleichzeitig haben.

## 5. Eclipse installieren

Eclipse ist die IDE, die wir für unser Projekt benutzen. 

1. Gehe auf [diese Webseite](https://www.eclipse.org/downloads/).
2. Downloade den dir vorgeschlagenen Installer (orangener "Download"-Button).
3. Öffne den Installer.
4. Wähle "Eclipse IDE for Java Developers".
5. Wähle bei der Einstellung "Java 1.8+ VM" das zuvor installiere JDK in der **Version 8** (liegt standardmäßig hier "C:\Program Files\Java\jdk-1.8.0_251").
6. Klicke "Install".

## 6. Eclipse auf JDK 8 einstellen

Obwohl wir soeben bei der Eclipse Installation das JDK 8 ausgewählt haben, kommt es häufiger vor, dass Eclipse das doch nicht so richtig gemacht hat. Daher überprüfen wir die Einstellung lieber noch einmal und passen sie gegenfalls an:

1. Öffne die Eclipse IDE.
2. Klicke "Window" => "Preferences"
3. Wähle den Pfeil neben "Java"
4. Wähle unter "Java" nun "Installed JREs" aus.
5. Wenn dort kein Paket, das _jdk-1.8.0_251_ heißt, liegt klicke "Add". 
   - Wenn dieses Paket in der Liste schon aufgelistet ist, musst du Eclipse nicht weiter einrichten. Wichtig wäre dann nur, das links neben dem Paket ein Häkchen ist.
6. Wähle "Standard VM" und klicke "Next"
7. Drücke rechts neben "JRE home" auf "Directory..."
8. Wähle den Ordner aus, wo du das _JDK 8_ installiert hast (ist standardmäßig "C:\Program Files\Java\jdk-1.8.0_251").
9. Klicke "Finish".
10. Setze ein Häkchen bei _jdk-1.8.0_251_.
11. Klicke "Apply and Close".


## 7. Eclipse Git-Addon installieren

Dieses Git-Addon ermöglicht es, dass Eclipse die Eclipse-Projekte, die innerhalb unseres Repositorys liegen nahezu automatisch erkennt. Nach einmaliger Einrichtung können wir dann sehr einfach und intuitiv den Source Code bearbeiten.

1. Öffne die Eclipse IDE.
2. Navigiere zu "Help" => "Install new Software"
3. Füge neben "Work with" diesen Link ein: http://download.eclipse.org/egit/updates.
4. Wähle alle erscheinenden Einträge aus und klicke "Next".
5. Klicke noch einmal "Next".
6. Akzeptiere die Lizenzvereinbarung.
7. Klicke "Finish" und starte nach der Installation des Addons (unten rechts zu sehen) Eclipse neu.

## 8. Repository in Eclipse übertragen

1. Öffne die Eclipse IDE.

2. Klicke "Window" => "Show View" => "Other..."

3. Suche nach "Git Repositories" und wähle den erscheinenden Eintrag aus.

4. Wähle unten links im erscheinenden "Git Repositories" Fenster "Add an existing local Git repository".

5. Klicke rechts neben "Directory" auf "Browse..."

6. Navigiere jetzt zum geklonten Repository und wähle den Ordner aus, der das Repository ist (müsste "RSA-App" heißen).

7. Setze jetzt Häkchen neben dem Eintrag der auf _RSA-App.git_ endet.

8. Klicke "Add".

9. Nun drücke mit der rechten Maustaste auf den Eintrag _RSA-App_ und wähle "Import Projects".

10. Wähle in der Liste bitte das Projekt "RSA-App\mavenProject"  aus, aber nicht das Projekt "RSA-App". Entferne also das Häkchen bei "RSA-App".

11. Drücke "Finish".

12. Jetzt erscheint das Maven-Projekt als "RSA-App" als ganz normales Projekt im Package Explorer.

    Den Source Code der App findest du unter _src/main/java_.

