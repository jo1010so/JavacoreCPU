package com.jojo.stringToMethod;

import com.jojo.Register.GeneralRegister;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {

    @Test
    public  void main1()throws Exception {
        Class<?> clazz = Class.forName("com.jojo.stringToMethod.MyTest");
        Method hello = clazz.getMethod("hello",String.class);
        Object obj = clazz.newInstance();
        Object s = hello.invoke(obj, "xiao");
        System.out.println(s.toString());
    }

    @Test
    public void main2()throws Exception{
        Class<?> clazz = Class.forName("com.jojo.ALU.ALU");
        Object instance = clazz.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println(Arrays.toString(methods));
    }

    @Test
    public void main3()throws Exception{
        GeneralRegister ax = new GeneralRegister("AX",16);

    }

}
