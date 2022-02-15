package com.codingmore.service.impl;

import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.util.WebRequestParam;
import org.springframework.stereotype.Service;

/**
 * 首页请求处理策略
 */
@Service("indexPageRequestStrategy")
public class IndexPageRequestStrategy implements ILearnWebRequestStrategy {
    @Override
    public String handleRequest(WebRequestParam webRequestParam) {
        return "index.html";
    }
}
