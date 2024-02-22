# Medikations-Pipeline

## Docker setup

Ehrbase per Docker starten:

```bash
cd resources/docker/ehrbase
docker compose up -d
```

Swagger kann anschließend
unter [localhost:8080/ehrbase/swagger-ui/index.html](localhost:8080/ehrbase/swagger-ui/index.html) aufgerufen werden.

Eine Anmeldung erfolgt mit den folgenden Informationen:<br>
**Username:** ehrbase-user<br>
**Password:** SuperSecretPassword

Da die neueren Versionen von Ehrbase Probleme mit Swagger haben, wurde die Version 0.30.0 verwendet.

## Hochladen eines Templates

Wähle ein Template in OPT Form (XML) von einem CKM aus.

Mit beispielsweise Postman kann anschließend eine Post-Request
an http://localhost:8080/ehrbase/rest/openehr/v1/definition/template/adl1.4
gesendet werden. Hier muss im Header der Parameter "Content-Type" auf "application/xml" gesetzt werden. Im Body muss das
Template eingefügt werden. Außerdem muss bei Authorization der Type Basic Auth mit den oben genannten Credentials
ausgewählt werden.

Für das Template des Medikationsplans übernimmt die Pipeline diese Aufgabe automatisch und überprüft bei jeden neuen
Start, ob das Template noch in EHRBase vorhanden ist.

## OpenEHR SDK Nutzung

Um Compositions zu EhrBase zu schicken, wird das OpenEHR SDK verwendet. Hierzu muss zunächst das bereits hochgeladene
Template in eine Java-Klasse umgewandelt werden. Dies kann mit dem generator von EhrBase gemacht werden. Als wichtige
Anleitung für die Nutzung des SDK wurde dieses [Tutorial-Video](https://www.youtube.com/watch?v=3SykJkbnT34) verwendet.
Der Generator ist Teil des openEHR_SDKs und kann selbst kompiliert werden. Hierzu muss das Projekt von
[GitHub](https://github.com/ehrbase/openEHR_SDK) geklont werden. Anschließend kann im Verzeichnis `generator` der Befehl
`mvn clean install` ausgeführt werden. Im Unterverzeichnis `target` wird dann `generator-2.x.y.jar` erstellt. Diese
Jar-Datei kann anschließend mit dem
Befehl `java -jar generator-2.x.y.jar -opt /path/to/template/Nephro_Medikation.opt -out /path/to/save/folder -package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions`
ausgeführt werden und erzeugt dabei die Java-Klassen für das Template.

Die generierten Klassen können dem Package entsprechend dem Projekt hinzugefügt werden.
