package com.jojo.thread;

import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

public class TheadTest {
    @Test
    public void test01(){
        String s1 = "BX,AL,15";
        String[] splits = s1.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : splits) {
            //正则表达式判断
            if (Pattern.matches("^A[XHL]$", s)) {
                sb.append("AX");
                sb.append(",");
                if (Pattern.matches("^AX$",s))
                    sb.append("0,");
                else if (Pattern.matches("^AH$",s))
                    sb.append("1,");
                else if (Pattern.matches("^AL$",s))
                    sb.append("2,");
            } else if (Pattern.matches("^B[XHL]$", s)) {
                sb.append("BX");
                sb.append(",");
                if (Pattern.matches("^BX$",s))
                    sb.append("0,");
                else if (Pattern.matches("^BH$",s))
                    sb.append("1,");
                else if (Pattern.matches("^BL$",s))
                    sb.append("2,");
            } else if (Pattern.matches("^C[XHL]$", s)) {
                sb.append("CX");
                sb.append(",");
                if (Pattern.matches("^CX$",s))
                    sb.append("0,");
                else if (Pattern.matches("^CH$",s))
                    sb.append("1,");
                else if (Pattern.matches("^CL$",s))
                    sb.append("2,");
            }else if (Pattern.matches("^D[XHL]$", s)) {
                sb.append("DX");
                sb.append(",");
                if (Pattern.matches("^DX$",s))
                    sb.append("0,");
                else if (Pattern.matches("^DH$",s))
                    sb.append("1,");
                else if (Pattern.matches("^DL$",s))
                    sb.append("2,");
            } else if (Pattern.matches("^-?\\d+$",s)) {
                sb.append(s);
                sb.append(",");
            }else{
                throw new RuntimeException("译码出现错误");
            }
        }
        System.out.println(sb);
    }
}
