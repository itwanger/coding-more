package top.lombok;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 2/24/22
 */
@Slf4j
public class Log4jDemo {
    public static void main(String[] args) {
        log.info("level:{}","info");
        log.warn("level:{}","warn");
        log.error("level:{}", "error");
    }
}
