package com.jojo;


import com.jojo.ALU.ALU;
import com.jojo.ALU.ALUUtils;
import com.jojo.Register.GeneralRegister;
import com.jojo.Register.RegisterConstants;
import org.junit.Test;

import java.util.Arrays;

public class RegisterTest {

    @Test
    public void te(){

    }

    @Test
    public void test(){

        GeneralRegister register = new GeneralRegister("AX", 16);
        register.setHighData(24);
        System.out.println(register.getData());
        System.out.println(Arrays.toString(register.getHighBits()));
        ALU alu = new ALU();
        alu.SHR(register, RegisterConstants.H,3);
        System.out.println(Arrays.toString(register.getHighBits()));
        System.out.println(Arrays.toString(register.getLowBits()));
        System.out.println(register.getHighData());
        System.out.println(register.getData());
    }

    @Test
    public void set() {
        GeneralRegister register = new GeneralRegister("AX", 16);
        register.setHighData(24);
        System.out.println(Arrays.toString(register.getHighBits()));
        Integer[] bits = register.getHighBits();
        int count = 4;
        ALUUtils.rightShift(bits, 8, count);
        System.out.println(Arrays.toString(bits));
    }

    /*// 1. 先将数组向左移动 k 位
        for (int i = 0; i < n - k; i++) {
        array[i] = array[i + k];
    }

    // 2. 将剩余的部分填充为 0
        for (int i = n - k; i < n; i++) {
        array[i] = 0;
    }*/

}
