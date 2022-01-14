package com.codingmore.service.impl;

import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.util.WebRequestParam;
import org.springframework.stereotype.Service;

/**
 * 内容请求处理策略
 */
@Service("contentPageRequestStrategy")
public class ContentPageRequestStrategy implements ILearnWebRequestStrategy {
    @Override
    public String handleRequest(WebRequestParam webRequestParam) {
        return "content";
    }
}
