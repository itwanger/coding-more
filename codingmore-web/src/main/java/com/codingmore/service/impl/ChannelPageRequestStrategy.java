package com.codingmore.service.impl;

import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.util.WebRequestParam;
import org.springframework.stereotype.Service;

/**
 * 栏目请求处理策略
 */
@Service("channelPageRequestStrategy")
public class ChannelPageRequestStrategy implements ILearnWebRequestStrategy {
    @Override
    public String handleRequest(WebRequestParam webRequestParam) {
        return "channel";
    }
}
