package com.jaypi4c.ba.pipeline.medicationplan.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OutputSaver {

    @Value("${io.debugFolder}")
    private String debugFolder;

    private final JsonArray output = new JsonArray();
    private final Gson gson = new Gson();

    public void saveTable(String filename, String[][] table, int page, String date) {
        JsonObject entry = new JsonObject();
        entry.addProperty("date", date);
        entry.addProperty("page", page);
        entry.addProperty("filename", filename);
        entry.addProperty("table", gson.toJson(table));
        entry.addProperty("tableRows", table.length - 1);// subtract header
        output.add(entry);
    }

    public void save(String filename) {
        try {
            FileUtils.writeStringToFile(new File(debugFolder + filename + ".json"), gson.toJson(output), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to save output", e);
        }
    }
}
