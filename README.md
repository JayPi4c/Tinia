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

Mit beispielsweise Postman kann anschließend eine Post-Request an http://localhost:8080/ehrbase/rest/openehr/v1/definition/template/adl1.4 gesendet werden. Hier muss im Header der Parameter "Content-Type" auf "application/xml" gesetzt werden. Im Body muss das Template eingefügt werden. Außerdem muss bei Authorization der Type Basic Auth mit den oben genannten Credentials ausgewählt werden.