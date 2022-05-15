package top;

import cn.hutool.core.util.RandomUtil;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/10/22
 */
public class RandomDemo {
    public static void main(String[] args) {
        System.out.println("恭喜朋友圈这个逼：");
        System.out.println("大号小号，大号为 0，小号为 1");
        System.out.println(RandomUtil.randomInt(0,2));
        System.out.println("点赞的第几位");
        // 样本把二哥这个废人先除掉，从 1 开始
        System.out.println(RandomUtil.randomInt(1,666));
    }
}
