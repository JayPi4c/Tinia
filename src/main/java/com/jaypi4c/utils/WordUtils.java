package com.jaypi4c.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WordUtils {

    public static String[] loadDictionary(String path) {
        List<String> lines = new ArrayList<>();

        try (InputStream is = WordUtils.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.error("Failed to load dictionary", e);
        }
        return lines.toArray(new String[0]);
    }


    public static LDResult findClosestWord(String word, String[] dict) {
        final LevenshteinDistance ld = LevenshteinDistance.getDefaultInstance();

        int minDistance = Integer.MAX_VALUE;
        String closestWord = "";

        for (String dictWord : dict) {
            int distance = ld.apply(word, dictWord);
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = dictWord;
                if (minDistance == 0) // we found exact match
                    break;
            }
        }
        return new LDResult(minDistance, closestWord, word);
    }

    public record LDResult(int distance, String closestWord, String targetWord) {
        public boolean exactMatch() {
            return distance == 0;
        }
    }
}
