package com.codingmore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface IOssService {
    String upload(String url);
    String upload(MultipartFile file);
    boolean needUpload(String imageUrl);
}