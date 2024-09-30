package com.jojo.ALU;

public class ALUUtils {
    public static void leftShift(Integer[] bits, int size, int count){
        // 1. 先将数组向左移动 k 位
        if (size - count >= 0) System.arraycopy(bits, count, bits, 0, size - count);
        // 2. 将剩余的部分填充为 0
        for (int i = size - count; i < size; i++) {
            bits[i] = 0;
        }
    }
    public static void rightShift(Integer[] bits, int size, int count){
        Integer[] result = new Integer[size];
        // 右移位，左侧补0
        for (int i = size-1; i >= 0; i--) {
            if ( i - count >= 0)
                bits[i] = bits[i-count];
            else
                bits[i] = 0;
        }
    }
    public static Integer[] numToB(int num,int size){
        Integer[] result = new Integer[size];
        String s = Integer.toBinaryString(num);
        int count = 0;
        for (int i = size-1; i >= 0; i--) {
            if (count < s.length()){
                result[i] = s.charAt(s.length()-1-count)-'0';
                count++;
            }else
                result[i] = 0;
        }
        return result;
    }
    public static Integer BToNum(Integer[] bits){
        int result = 0;
        int count = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            result += bits[i] * (1<<count);
            count++;
        }
        return result;
    }



}
