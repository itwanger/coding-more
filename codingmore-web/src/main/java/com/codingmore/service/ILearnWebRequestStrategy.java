package com.codingmore.service;

import com.codingmore.util.WebRequestParam;

/**
 * 前端请求处理策略
 */
public interface ILearnWebRequestStrategy {
    String handleRequest(WebRequestParam webRequestParam);

}
