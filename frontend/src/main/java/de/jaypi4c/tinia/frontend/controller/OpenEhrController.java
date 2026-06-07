package de.jaypi4c.tinia.frontend.controller;

import de.jaypi4c.tinia.frontend.dto.EntryDto;
import de.jaypi4c.tinia.frontend.service.OpenEhrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/openehr")
public class OpenEhrController {

    private final OpenEhrService openEhrService;

    @GetMapping("/templates")
    public String templates(Model model) {
        List<String> templates = openEhrService.fetchAllTemplates();
        log.info(templates.toString());
        model.addAttribute("templates", templates);
        return "openehr/templates.html";
    }

    @GetMapping
    public String home(Model model) {
        List<EntryDto> entries = openEhrService.fetchAll();
        log.info(entries.toString());
        model.addAttribute("entries", entries);
        return "openehr/index.html";
    }

    @GetMapping("/ehr/{ehrId}/composition/{compositionID}")
    public String getComposition(@PathVariable String ehrId, @PathVariable String compositionID) {
        log.info("Fetching detail info for {} | {}", ehrId, compositionID);
        return "openehr/composition.html";
    }

}
