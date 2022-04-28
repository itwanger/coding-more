package com.codingmore.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 4/28/22
 */
public interface IContainerFactory {
    IContainerFactory defaultContainerFactory = new IContainerFactory() {
        public Map<String, Object> getAttrsMap() {
            return new HashMap();
        }
    };
    Map getAttrsMap();
}
