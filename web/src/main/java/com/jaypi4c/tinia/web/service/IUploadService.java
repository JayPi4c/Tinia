package com.jaypi4c.tinia.web.service;

import java.io.InputStream;

public interface IUploadService {

    String processFile(InputStream inputStream, String filename);

}
