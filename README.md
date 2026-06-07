# Tinia

Based on a bachelor thesis, Tinia is a software to extract table based medication plans from PDF files and store them in
an interoperable format (openEHR).

## Quickstart

Tinia provides a `docker-compose.yaml` file which can be used to quickly set up all services. Simply enter the
`infrastructure` folder and execute `docker compose up`

Once all services are started, you can open a browser at http://localhost and upload a PDF. Once processing is done, you
can start exploring the data either directly within EHRbase (http://localhost/ehrbase) with swagger or any other tool of
your choice or use the dedicated frontend at http://localhost/openehr.

## Templates

The required template is provided by the project and upon start Tinia will check if it is present in the EHR repository.
If not, it automatically uploads the template.

## Architecture

Tinia uses a microservice architecture. Currently, the following services are present:

- **Backend:** The Entry / Management node of the app. It will handle the requests and distribution to other services
- **Frontend:** The Frontend node for easier user experience with Tinia
- **Extractor:** Worker node to analyze image files and extract table data from the uploaded image
- **Validator:** Worker node to validate extracted information with external databases/services
- **OpenEHR:** Worker node to upload parsed medication data to an EHR repository

Due to the architecture, any node can be deployed multiple times and the work will be distributed to the workers as
need.
That being said, especially multiple Extractor nodes are advised, as they have the biggest workload to handle.

## Open Tasks

- [ ] Provide proper docker swarm setup
- [ ] Extend Wiki
- [ ] Add OCR node to not rely on OCR preprocessed data
- [ ] Add proper CI/CD workflow to build and push docker images upon releases
- [ ] Add SSE Updating process once processing is done
- [ ] Finish frontend page about compositions
- [ ] ...
