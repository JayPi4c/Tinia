package de.jaypi4c.tinia.frontend.service;

import de.jaypi4c.tinia.frontend.dto.EntryDto;

import java.util.List;

public interface OpenEhrService {

    List<EntryDto> fetchAll();

    List<String> fetchAllTemplates();

}
