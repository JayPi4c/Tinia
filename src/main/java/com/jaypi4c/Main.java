package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Starting application");

        String pdfPath = "/home/jonas/Studium/cloud/BA/BA Daten/";
        String file1 = "SF_20220104_50335_HA1_LETTER.pdf";
        String file2 = "SF_20220412_50061_HA1_LETTER.pdf";
        String file3 = "SF_20220511_50091_HA1_LETTER.pdf";
        String file4 = "SF_20220620_50193_HA1_LETTER.pdf";

        pdfPath += file4;

        String imageResult = "/home/jonas/Studium/cloud/BA/BA Daten/out.jpg";
        TableExtractor tableExtractor = new TableExtractor();
        tableExtractor.execute(pdfPath, imageResult);

        log.info("Finished application");
    }
}
