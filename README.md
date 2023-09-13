# Medikations-Pipeline

## Docker setup

Ehrbase per Docker starten:
```bash
cd resources/docker
docker compose up -d
```

Swagger kann anschließend unter   [localhost:8080/ehrbase/swagger-ui/index.html](localhost:8080/ehrbase/swagger-ui/index.html) aufgerufen werden.

Eine Anmeldung erfolgt mit den folgenden Informationen:<br>
**Username:** ehrbase-user<br>
**Password:** SuperSecretPassword

Da die neueren Versionen von Ehrbase Probleme mit Swagger haben, wurde die Version 0.30.0 verwendet.


## Hochladen eines Templates

Wähle ein Template in OPT Form (XML) von einem CKM aus.

Mit beispielsweise Postman kann anschließend eine Post-Request an http://localhost:8080/ehrbase/rest/openehr/v1/definition/template/adl1.4 
gesendet werden. Hier muss im Header der Parameter "Content-Type" auf "application/xml" gesetzt werden. Im Body muss das 
Template eingefügt werden. Außerdem muss bei Authorization der Type Basic Auth mit den oben genannten Credentials ausgewählt 
werden.


## OpenEHR SDK Nutzung

Um Compositions zu EhrBase zu schicken, wird das OpenEHR SDK verwendet. Hierzu muss zunächst das bereits hochgeladene 
Template in eine Java-Klasse umgewandelt werden. Dies kann mit dem generator von EhrBase gemacht werden. Wichtig ist,
dass aufgrund mangelder Dokumentation von Ehrbase v2 die Version 1.x verwendet wurde. Entsprechend sollte bei der Nutzung 
des SDK auch darauf geachtet werden, dass immer ein Release von 1.x verwendet wird. Als wichtige Anleitung für die Nutzung 
des SDK wurde dieses [Tutorial-Video](https://www.youtube.com/watch?v=3SykJkbnT34) verwendet. 

Generierte Klassen können dann in das Projekt in das Package `com.jaypi4c.openehr.compositions` kopiert werden. Für die 
erleichterte Nutzung wurde dann noch die Klasse `NephroMedikationTemplateProvider` erstellt.
