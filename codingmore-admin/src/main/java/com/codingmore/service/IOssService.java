package com.codingmore.service;

import java.io.InputStream;

public interface IOssService {
    String upload(String url);
    String upload(InputStream inputStream,String name);
    String getEndPoint();
}