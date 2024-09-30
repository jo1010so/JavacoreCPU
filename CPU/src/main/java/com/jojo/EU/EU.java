package com.jojo.EU;

import com.jojo.CPU;
import com.jojo.Memory.Segment;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class EU {
    public void loadCodes(String filePath,Segment<String> CodeSegment){
        Integer index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                CodeSegment.write(index,line);
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public String decode(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                //进行译码
                String[] split = line.split(" ");
                String opcode = split[0];
                String operands = split[1];
                StringBuilder sb = new StringBuilder();
                String[] splits = operands.split(",");
                for (String s:splits) {
                    //正则表达式判断
                    if (Pattern.matches(s,"^[ABCD]X$")){
                        sb.append(s);
                        sb.append(",");
                        sb.append("0,");
                    }else if(Pattern.matches(s,"^[ABCD]H$")){
                        sb.append(s);
                        sb.append(",");
                        sb.append("1,");
                    }else if(Pattern.matches(s,"^[ABCD]L$")){
                        sb.append(s);
                        sb.append(",");
                        sb.append("2,");
                    }else if (Pattern.matches(s,"^-?\\d+$")){
                        sb.append(s);
                        sb.append(",");
                    }else{
                        throw new RuntimeException("译码出现错误");
                    }
                }
                //TODO 将加入执行队列

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @SneakyThrows
    public void decode(CPU cpu) {
        while (true)
            while (!cpu.getDecodeQueue().isEmpty()) {
            String line = cpu.getDecodeQueue().take();
            //进行译码
            Thread.sleep(100); //模拟译码用时
            System.out.println("开始译码 :"+line);
            String[] split = line.split(" ");
            String opcode = split[0];
            String operands = "";
            if (split.length>1)
                operands = split[1];
            StringBuilder sb = new StringBuilder();
            String[] splits = operands.split(",");
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
                }else if(s.equals("")){
                    sb.append("");
                } else{
                    throw new RuntimeException("译码出现错误");
                }
            }
            if (Pattern.matches("^(MUL|DIV)$",opcode)){
                sb.append("AX,DX");
            }
            Instruction instruction = new Instruction(opcode, sb.toString());
            //将解码后的指令加入执行队列  (操作码和参数字符串)
            cpu.getExecuteQueue().put(instruction);
            System.out.println("将译码后的数据放入执行队列: "+instruction);
            if (opcode.equals("HLT"))
                return;
        }
    }

}
