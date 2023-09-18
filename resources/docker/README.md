# EhrBase query Beispiele

## Anzeigen von EHR_ID, subject_id und type & namespace
**Adresse:** http://localhost:8080/ehrbase/rest/openehr/v1/query/aql?q= <br>
**Query:** 
```aql
SELECT 
    e/ehr_id/value, 
    e/ehr_status/subject/external_ref/id/value, 
    e/ehr_status/subject/external_ref/namespace, 
    e/ehr_status/subject/external_ref/type 
FROM 
    EHR e
```


## Anzeigen von EHR_ID und composition_id

**Adresse:** http://localhost:8080/ehrbase/rest/openehr/v1/query/aql?q= <br>
**Query:** 
```aql
SELECT 
    e/ehr_id/value, 
    e/ehr_status/subject/external_ref/id/value, 
    c/uid/value
FROM 
    EHR e
    CONTAINS 
        COMPOSITION c
```