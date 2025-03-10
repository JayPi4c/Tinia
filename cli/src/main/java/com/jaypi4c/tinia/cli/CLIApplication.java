package com.jaypi4c.tinia.cli;

import com.jaypi4c.tinia.cli.api.DefaultApi;

import java.io.File;
import java.util.List;

public class CLIApplication {

    public static void main(String[] args) throws ApiException {
        DefaultApi api = new DefaultApi();

        File f = new File("/home/jonas/Downloads/BA Abschluss.pdf");

        ApiResponse<?> resp = api.extractBmpWithHttpInfo(List.of(f), null);
        System.out.println(resp.getStatusCode());
    }

}
