# Setup-Tutorial

Dieses Tutorial fasst alles, was du herunterladen, installieren und einrichten musst, zusammen, um bei diesem Projekt mitzumachen.

## 1. Git Installation

Git ist das Versionsverwaltungssystem, was wir nutzen, um die Entwicklung unseres Projekts in Versionen einzuteilen (mehr dazu [hier](https://docs.google.com/presentation/d/1CcJrfBZer-sNxg7leW6UvKjbexlPjSmiAAHfRJONaNM/edit#slide=id.g85a68de936_0_2)). Du musst Git auf deinem Rechner installieren, um unsere Projekt-Dateien vom Github-Server herunterzuladen, einfach zu bearbeiten und deine gemachten Änderungen wieder hochzuladen.

### Windows

Unter Windows ist es sinnvoll _Git Bash_ zu installieren. Hier eine Schritt für Schritt Anleitung:

#### 1. Git Bash downloaden

Gehe auf diese [Webseite](https://gitforwindows.org/). Dann klicke auf **Download** um den Installer zu öffnen.

#### 2. Git Bash installieren

_Öffne_ den Installer (er müsste ungefähr den Namen _Git-2.26.2-64-bit.exe_) haben. _Akzeptiere_ die "GNU General Public License", _setze_ einen beliebigen Installationspfad für das Git Programm, _drücke_ solange "Next" bis du bei "Install" ankommst und _installiere_ Git.

#### 3. Git Bash einrichten

_Öffne_ jetzt einfach die Git Bash.

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

### Mac OS



