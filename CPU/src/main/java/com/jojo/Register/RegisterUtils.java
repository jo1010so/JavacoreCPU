package com.jojo.Register;

public class RegisterUtils{
    // 将有符号整数转换为指定长度的二进制补码表示
    public static Integer[] dataToBits(int bitLength,Integer data) {
        // 计算补码
        String binaryString = Integer.toBinaryString(data);
        // 对于负数，确保补码的位数符合指定的 bitLength
        if (data < 0) {
            int mask = (1 << bitLength) - 1; // 计算掩码
            binaryString = Integer.toBinaryString(data & mask);
        }
        // 如果二进制字符串的长度不足 bitLength，需要前置零
        if (binaryString.length() < bitLength) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bitLength - binaryString.length(); i++) {
                sb.append('0');
            }
            sb.append(binaryString);
            binaryString = sb.toString();
        }
        // 处理超过 bitLength 的情况
        if (binaryString.length() > bitLength) {
            binaryString = binaryString.substring(binaryString.length() - bitLength);
        }
        Integer[] integers = new Integer[bitLength];
        for (int i = 0; i < bitLength; i++) {
            integers[i] = binaryString.charAt(i) - '0';
        }
        return integers;
    }
    public static Integer bitsToData(int size,Integer[] bits){
        // 处理二进制补码表示
        int data = 0;
        data = (int) (data - bits[0] * Math.pow(2,size-1));
        for (int i = 1; i < size; i++) {
            data += bits[i] * Math.pow(2,size-i-1);
        }
        return data;
    }
    //判断数组中是否只有0和1两个数
    public static boolean checkedArray(Integer[] array) {
        for (int num : array) {
            if (num != 0 && num != 1) {
                return true;
            }
        }
        return false;
    }

}
