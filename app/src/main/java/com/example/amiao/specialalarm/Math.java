package com.example.amiao.specialalarm;

import java.util.Random;



public class Math {
    private static int num1;
    private static int num2;
    private static int sum;
    private int max = 200;
    private int min = 0;

    Math(){
        Random rand = new Random(System.currentTimeMillis());
        num1 = 17 + rand.nextInt(200);
        num2 = 17 + rand.nextInt(200);
    }

    public static void computeSum(){

        sum = num1 + num2;
    }

    public static int getNum1(){
        return num1;
    }

    public static int getNum2(){
        return num2;
    }

    public static int getSum(){
        return sum;
    }
}
