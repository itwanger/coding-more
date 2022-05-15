package com.codingmore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface IOssService {
    String upload(String url);
    String upload(MultipartFile file);
    String upload(InputStream inputStream,String name);
    boolean needUpload(String imageUrl);
}