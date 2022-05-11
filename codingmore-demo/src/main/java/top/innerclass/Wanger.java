package top.innerclass;

public class Wanger {
    private int age;
    private double money;

    public Wanger () {
        new Wangxiaoer().print();
    }

    class Wangxiaoer {
        public Wangxiaoer() {

        }

        public void print() {
            System.out.println(age);
            System.out.println(money);
        }
    }
}
