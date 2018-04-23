package com.wang.social.im;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-19 17:07
 * ============================================
 */
public class Test {

    interface IA{

    }

    static class B implements IA{

    }

    static class C implements IA{

    }

    public static void main(String[] args) {
        IA b = new B();
        IA c = new C();
        System.out.println(B.class.isInstance(b));
        System.out.println(B.class.isInstance(c));
    }
}
