package com.ccy.easyzhihu;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu
 * @date 2019/4/10
 */
public class Test {

    public static void main(String[] args) {

            int n = 12;
            n |= n >>> 1;
        System.out.println(n);
            n |= n >>> 2;
        System.out.println(n);
            n |= n >>> 4;
        System.out.println(n);
            n |= n >>> 8;
        System.out.println(n);
            n |= n >>> 16;
        System.out.println( (n < 0) ? 1 : (n >= 1<<30) ? 1<<30 : n + 1);
    }
}
