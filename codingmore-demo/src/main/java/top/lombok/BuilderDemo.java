package top.lombok;

import lombok.Builder;
import lombok.ToString;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 2/24/22
 */
@Builder
@ToString
public class BuilderDemo {
    private Long id;
    private String name;
    private Integer age;

    public static void main(String[] args) {
        BuilderDemo demo = BuilderDemo.builder().age(18).name("沉默王二").build();
        System.out.println(demo);
    }
}
