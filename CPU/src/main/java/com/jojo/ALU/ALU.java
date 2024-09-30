package com.jojo.ALU;

import com.jojo.Register.GeneralRegister;
import com.jojo.Register.RegisterConstants;

public class ALU {
    private Boolean CF = false;  // 进位标志：用于反映运算是否产生进位或借位。如果运算结果的最高位产生一个进位或借位，则CF置1，否则置0。运算结果的最高位包括字操作的第15位和字节操作的第7位。移位指令也会将操作数的最高位或最低位移入CF。
    private Boolean ZF = false;  // 零标志：用于判断结果是否为0。运算结果0，ZF置1，否则置0。
    private Boolean SF = false;  // 符号标志：用于反映运算结果的符号，运算结果为负，SF置1，否则置0。因为有符号数采用补码的形式表示，所以SF与运算结果的最高位相同。
    private Boolean OF = false;  // 溢出标志：反映有符号数加减运算是否溢出。如果运算结果超过了8位或者16位有符号数的表示范围，则OF置1，否则置0。
    private Boolean AF = false;  // 辅助进位标志：算数操作结果的第三位（从0开始计数）如果产生了进位或者借位则将其置为1，否则置为0，常在BCD(binary-codedecimal)算术运算中被使用。
    private Boolean IF = false;  // 中断标志：决定CPU是否响应外部可屏蔽中断请求。IF为1时，CPU允许响应外部的可屏蔽中断请求。
    private Boolean DF = false;  // 方向标志：决定串操作指令执行时有关指针寄存器调整方向。当DF为1时，串操作指令按递减方式改变有关存储器指针值，每次操作后使SI、DI递减。
    private Boolean TF = false;  // 跟踪标志：当TF被设置为1时，CPU进入单步模式，所谓单步模式就是CPU在每执行一步指令后都产生一个单步中断。主要用于程序的调试。8086/8088中没有专门用来置位和清零TF的命令，需要用其他办法。
    private Boolean PF = false;  // 奇偶标志：用于反映运算结果低8位中“1”的个数。“1”的个数为偶数，则PF置1，否则置0。

    public void printFlags(){
        System.out.println(
                "CF:" + CF.toString()+
                "ZF:" + ZF.toString()+
                "SF:" + SF.toString()+
                "OF:" + OF.toString()+
                "AF:" + AF.toString()+
                "IF:" + IF.toString()+
                "DF:" + DF.toString()+
                "TF:" + TF.toString()+
                "PF:" + PF.toString());
    }

    public String showFlags(){
        return " CF:" + CF.toString()+
                " ZF:" + ZF.toString()+
                " SF:" + SF.toString()+
                " OF:" + OF.toString()+
                " AF:" + AF.toString()+
                " IF:" + IF.toString()+
                " DF:" + DF.toString()+
                " TF:" + TF.toString()+
                " PF:" + PF.toString();
    }

    public void setPF(Integer[] bits){
        int count = 0;
        for (Integer bit:bits) {
            if (bit == 1)
                count++;
        }
        PF = count%2==0;
    }

    public Integer checkOF(Integer result,int size){
        if (size == 16){
            if (result > 0x7FFF || result < -0x8000){
                System.out.println("运算发生溢出");
                OF = true;
                //截断，只取后16位
                result = result & 0xFFFF;
                if ((result & 0x8000) == 0x8000)
                    result = (result & 0xFFFF) - 2*0x8000;
            }else{
                OF = false;
            }
        }else if(size == 8){
            if (result > 0x7F || result < -0x80){
                System.out.println("运算发生溢出");
                OF = true;
                //截断，只取后8位
                result = result & 0xFF;
                if ((result & 0x80) == 0x80)
                    result = (result & 0xFF) - 2*0x80;
            }else{
                OF = false;
            }
        }
        return result;
    }

    public void ADD(GeneralRegister register, Integer type, Integer num){
        if (num > 0x7FFF || num<-0x8000){
            throw new IllegalArgumentException("参数赋值超出范围");
        }
        int result = 0;
        if (type.equals(RegisterConstants.X)){
            Integer data = register.getData();
            result = data + num;
            CF = (result & 0x10000) != 0;
            result = checkOF(result,register.getSize());
            register.setData(result);
            setPF(register.getBits());
        }else if (type.equals(RegisterConstants.H)){
            if (num > 0x7F || num<-0x80){
                throw new IllegalArgumentException("参数赋值超出范围");
            }
            Integer data = register.getHighData();
            result = data + num;
            CF = (result & 0x100) != 0;
            result = checkOF(result,8);
            register.setHighData(result);
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            if (num > 0x7F || num<-0x80){
                throw new IllegalArgumentException("参数赋值超出范围");
            }
            Integer data = register.getLowData();
            result = data + num;
            CF = (result & 0x100) != 0;
            result = checkOF(result,8);
            register.setLowData(result);
            setPF(register.getLowBits());
        }
        ZF = result == 0;
        SF = result < 0;
    }

    public void ADD(GeneralRegister register, Integer type,GeneralRegister register2,Integer type2){
        if (type == 0 && type2 == 1 || type == 0 && type2 == 2 ||
                type == 1 && type2 == 0 || type == 2 && type2 == 0){
            throw new IllegalArgumentException("8位寄存器不能与16位寄存器做ADD运算");
        }
        int result = 0;
        int num;
        if (type.equals(RegisterConstants.X)){
            num = register2.getData();
            Integer data = register.getData();
            result = data + num;
            CF = (result & 0x10000) != 0;
            result = checkOF(result,16);
            register.setData(result);
            setPF(register.getBits());
        }else if (type.equals(RegisterConstants.H)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H)){
                num = register2.getHighData();
            }
            Integer data = register.getHighData();
            result = data + num;
            CF = (result & 0x100) != 0;
            result = checkOF(result,8);
            register.setHighData(result);
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H)){
                num = register2.getHighData();
            }
            Integer data = register.getLowData();
            result = data + num;
            CF = (result & 0x100) != 0;
            result = checkOF(result,8);
            register.setLowData(result);
            setPF(register.getLowBits());
        }
        ZF = result == 0;
        SF = result < 0;
    }

    public void SUB(GeneralRegister register, Integer type, Integer num){
        if (num > 0x7FFF || num<-0x8000){
            throw new IllegalArgumentException("参数赋值超出范围");
        }
        int result = 0;
        if (type.equals(RegisterConstants.X)) {
            Integer data = register.getData();
            result = data - num;
            CF = data < num;
            result = checkOF(result,16);
            register.setData(result);
            setPF(register.getBits());
        }else if(type.equals(RegisterConstants.H)){
            if (num > 0x7F || num<-0x80){
                throw new IllegalArgumentException("参数赋值超出范围");
            }
            Integer data = register.getHighData();
            result = data - num;
            CF = data < num;
            result = checkOF(result,8);
            register.setHighData(result);
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            if (num > 0x7F || num<-0x80){
                throw new IllegalArgumentException("参数赋值超出范围");
            }
            Integer data = register.getLowData();
            result = data - num;
            CF = data < num;
            result = checkOF(result,8);
            register.setLowData(result);
            setPF(register.getLowBits());
        }
        ZF = result == 0;
        SF = result < 0;
    }

    public void SUB(GeneralRegister register, Integer type,GeneralRegister register2, Integer type2){
        if (type == 0 && type2 == 1 || type == 0 && type2 == 2 ||
                type == 1 && type2 == 0 || type == 2 && type2 == 0){
            throw new IllegalArgumentException("8位寄存器不能与16位寄存器做SUB运算");
        }
        int result = 0;
        Integer num;
        if (type.equals(RegisterConstants.X)) {
            num = register2.getData();
            Integer data = register.getData();
            result = data - num;
            CF = data < num;
            result = checkOF(result,16);
            register.setData(result);
            setPF(register.getBits());
        }else if(type.equals(RegisterConstants.H)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            Integer data = register.getHighData();
            result = data - num;
            CF = data < num;
            result = checkOF(result,8);
            register.setHighData(result);
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            Integer data = register.getLowData();
            result = data - num;
            CF = data < num;
            result = checkOF(result,8);
            register.setLowData(result);
            setPF(register.getLowBits());
        }
        ZF = result == 0;
        SF = result < 0;
    }

    public void MOV(GeneralRegister register, Integer type, Integer num){
        if (num > 0x7FFF || num<-0x8000){
            throw new IllegalArgumentException("参数赋值超出范围");
        }
        if (type.equals(RegisterConstants.X)){
            register.setData(num);
        }else if (type.equals(RegisterConstants.H)){
            if (num > 0x7F || num<-0x80){
                throw new IllegalArgumentException("参数赋值超出范围");
            }
            register.setHighData(num);
        }else if (type.equals(RegisterConstants.L)){
            if (num > 0x7F || num<-0x80){
                throw new IllegalArgumentException("参数赋值超出范围");
            }
            register.setLowData(num);
        }
        CF = false;
        ZF = false;
        SF = false;
        OF = false;
        AF = false;
        IF = false;
        DF = false;
        TF = false;
        PF = false;
    }

    public void MOV(GeneralRegister register, Integer type,GeneralRegister register2, Integer type2){
        if (type == 0 && type2 == 1 || type == 0 && type2 == 2 ||
                type == 1 && type2 == 0 || type == 2 && type2 == 0){
            throw new IllegalArgumentException("8位寄存器不能与16位寄存器做MOV操作");
        }
        Integer data;
        if (type.equals(RegisterConstants.X)){
            register.setData(register2.getData());
        }else if (type.equals(RegisterConstants.H)){
            data = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                data = register2.getHighData();
            register.setHighData(data);
        }else if (type.equals(RegisterConstants.L)){
            data = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                data = register2.getHighData();
            register.setLowData(data);
        }
    }

    public void INC(GeneralRegister register, Integer type){
        Integer result;
        if (type.equals(RegisterConstants.X)){
            Integer data = register.getData();
            result = data + 1;
            result = checkOF(result,16);
            ZF = result == 0;
            SF = result < 0;
            register.setData(result);
        }else if (type.equals(RegisterConstants.H)){
            Integer data = register.getHighData();
            result = data + 1;
            result = checkOF(result,8);
            ZF = result == 0;
            SF = result < 0;
            register.setHighData(result);
        }else if (type.equals(RegisterConstants.L)){
            Integer data = register.getLowData();
            result = data + 1;
            result = checkOF(result,8);
            ZF = result == 0;
            SF = result < 0;
            register.setLowData(result);
        }
    }

    public void DEC(GeneralRegister register, Integer type){
        Integer result;
        if (type.equals(RegisterConstants.X)){
            Integer data = register.getData();
            result = data - 1;
            result = checkOF(result,16);
            ZF = result == 0;
            SF = result < 0;
            register.setData(result);
        }else if (type.equals(RegisterConstants.H)){
            Integer data = register.getHighData();
            result = data - 1;
            result = checkOF(result,8);
            ZF = result == 0;
            SF = result < 0;
            register.setHighData(result);
        }else if (type.equals(RegisterConstants.L)){
            Integer data = register.getLowData();
            result = data - 1;
            result = checkOF(result,8);
            ZF = result == 0;
            SF = result < 0;
            register.setLowData(result);
        }
    }

    public void SHL(GeneralRegister register, Integer type,Integer count) {
        if (count <= 0) {
            throw new IllegalArgumentException("移位数不合法");
        }
        Integer[] bits;
        int size;
        if (type.equals(RegisterConstants.X)) {
            if (count >= register.getSize()){
                register.setData(0);
                SF = false;
                ZF = true;
                CF = count == register.getSize() && register.getBits()[register.getSize() - 1] == 1;
            }
            else {
                bits = register.getBits();
                CF = bits[count-1] == 1;
                size = register.getSize();
                ALUUtils.leftShift(bits, size, count);
                register.setBits(bits);
                Integer data = register.getData();
                ZF = data == 0;
                SF = data < 0;
            }
        } else if (type.equals(RegisterConstants.H)) {
            if (count >= (register.getLsize())){
                register.setHighData(0);
                SF = false;
                ZF = true;
                CF = count == register.getLsize() && register.getHighBits()[register.getLsize() - 1] == 1;
            }
            else {
                bits = register.getHighBits();
                CF = bits[count-1] == 1;
                size = register.getLsize();
                ALUUtils.leftShift(bits, size, count);
                register.setHighBits(bits);
                Integer data = register.getHighData();
                ZF = data == 0;
                SF = data < 0;
            }
        }else if (type.equals(RegisterConstants.L)){
            if (count >= (register.getLsize())){
                register.setLowData(0);
                SF = false;
                ZF = true;
                CF = count == register.getLsize() && register.getLowBits()[register.getLsize() - 1] == 1;
            }
            else {
                bits = register.getLowBits();
                CF = bits[count-1] == 1;
                size = register.getLsize();
                ALUUtils.leftShift(bits, size, count);
                register.setLowBits(bits);
                Integer data = register.getLowData();
                ZF = data == 0;
                SF = data < 0;
            }
        }
        CF = false;
        ZF = false;
        SF = false;
        OF = false;
        AF = false;
        IF = false;
        DF = false;
        TF = false;
        PF = false;
    }

    public void SHR(GeneralRegister register, Integer type,Integer count){
        if (count <= 0) {
            throw new IllegalArgumentException("移位数不合法");
        }
        Integer[] bits;
        int size;
        if (type.equals(RegisterConstants.X)) {
            if (count >= register.getSize()){
                register.setData(0);
                SF = false;
                ZF = true;
                CF = count == register.getSize() && register.getBits()[0] == 1;
            } else {
                bits = register.getBits();
                CF = bits[register.getSize()-count-1] == 1;
                size = register.getSize();
                ALUUtils.rightShift(bits, size, count);
                register.setBits(bits);
                Integer data = register.getData();
                ZF = data == 0;
                SF = data < 0;
            }
        } else if (type.equals(RegisterConstants.H)){
            if (count >= register.getLsize()){
                register.setHighData(0);
                SF = false;
                ZF = true;
                CF = count == register.getLsize() && register.getHighBits()[0] == 1;
            } else {
                bits = register.getHighBits();
                CF = bits[register.getLsize()-count-1] == 1;
                size = register.getLsize();
                ALUUtils.rightShift(bits, size, count);
                register.setHighBits(bits);
                Integer data = register.getHighData();
                ZF = data == 0;
                SF = data < 0;
            }
        }else if (type.equals(RegisterConstants.L)){
            if (count >= register.getLsize()){
                register.setLowData(0);
                SF = false;
                ZF = true;
                CF = count == register.getLsize() && register.getLowBits()[0] == 1;
            } else {
                bits = register.getLowBits();
                CF = bits[register.getLsize()-count-1] == 1;
                size = register.getLsize();
                ALUUtils.rightShift(bits, size, count);
                register.setLowBits(bits);
                Integer data = register.getLowData();
                ZF = data == 0;
                SF = data < 0;
            }
        }
    }

    public void AND(GeneralRegister register, Integer type, Integer num){
        if (num > 0x7FFF || num<-0x8000)
            throw new IllegalArgumentException("参数赋值超出范围");
        int result;
        Integer data;
        if (type.equals(RegisterConstants.X)){
            data = register.getData();
            result = data & num;
            result &= 0xFFFF;
            Integer[] bits = ALUUtils.numToB(result, register.getSize());
            register.setBits(bits);
            data = register.getData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }else if (type.equals(RegisterConstants.H)){
            if (num > 0x7F || num<-0x80)
                throw new IllegalArgumentException("参数赋值超出范围");
            data = register.getHighData();
            result = data & num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setHighBits(bits);
            data = register.getHighData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getHighBits());
        }else if(type.equals(RegisterConstants.L)){
            if (num > 0x7F || num<-0x80)
                throw new IllegalArgumentException("参数赋值超出范围");
            data = register.getLowData();
            result = data & num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setLowBits(bits);
            data = register.getLowData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }
        CF = false;
        OF = false;
    }

    public void AND(GeneralRegister register, Integer type,GeneralRegister register2, Integer type2){
        if (type == 0 && type2 == 1 || type == 0 && type2 == 2 ||
                type == 1 && type2 == 0 || type == 2 && type2 == 0){
            throw new IllegalArgumentException("8位寄存器不能与16位寄存器做AND运算");
        }
        int result;
        Integer num;
        Integer data;
        if (type.equals(RegisterConstants.X)){
            num = register2.getData();
            data = register.getData();
            result = data & num;
            result &= 0xFFFF;
            Integer[] bits = ALUUtils.numToB(result, register.getSize());
            register.setBits(bits);
            data = register.getData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }else if (type.equals(RegisterConstants.H)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            data = register.getHighData();
            result = data & num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setHighBits(bits);
            data = register.getHighData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            data = register.getLowData();
            result = data & num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setLowBits(bits);
            data = register.getLowData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }
        CF = false;
        OF = false;
    }

    public void OR(GeneralRegister register, Integer type, Integer num){
        if (num > 0x7FFF || num<-0x8000)
            throw new IllegalArgumentException("参数赋值超出范围");
        int result;
        Integer data;
        if (type.equals(RegisterConstants.X)){
            data = register.getData();
            result = data | num;
            result &= 0xFFFF;
            Integer[] bits = ALUUtils.numToB(result, register.getSize());
            register.setBits(bits);
            data = register.getData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }else if (type.equals(RegisterConstants.H)){
            if (num > 0x7F || num<-0x80)
                throw new IllegalArgumentException("参数赋值超出范围");
            data = register.getHighData();
            result = data | num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setHighBits(bits);
            data = register.getHighData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getHighBits());
        }else if(type.equals(RegisterConstants.L)){
            if (num > 0x7F || num<-0x80)
                throw new IllegalArgumentException("参数赋值超出范围");
            data = register.getLowData();
            result = data | num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setLowBits(bits);
            data = register.getLowData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }
        CF = false;
        OF = false;
    }

    public void OR(GeneralRegister register, Integer type,GeneralRegister register2, Integer type2){
        if (type == 0 && type2 == 1 || type == 0 && type2 == 2 ||
                type == 1 && type2 == 0 || type == 2 && type2 == 0){
            throw new IllegalArgumentException("8位寄存器不能与16位寄存器做AND运算");
        }
        int result;
        Integer num;
        Integer data;
        if (type.equals(RegisterConstants.X)){
            num = register2.getData();
            data = register.getData();
            result = data | num;
            result &= 0xFFFF;
            Integer[] bits = ALUUtils.numToB(result, register.getSize());
            register.setBits(bits);
            data = register.getData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }else if (type.equals(RegisterConstants.H)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            data = register.getHighData();
            result = data | num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setHighBits(bits);
            data = register.getHighData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            data = register.getLowData();
            result = data | num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setLowBits(bits);
            data = register.getLowData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }
        CF = false;
        OF = false;
    }

    public void XOR(GeneralRegister register, Integer type, Integer num){
        if (num > 0x7FFF || num<-0x8000)
            throw new IllegalArgumentException("参数赋值超出范围");
        int result;
        Integer data;
        if (type.equals(RegisterConstants.X)){
            data = register.getData();
            result = data ^ num;
            result &= 0xFFFF;
            Integer[] bits = ALUUtils.numToB(result, register.getSize());
            register.setBits(bits);
            data = register.getData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }else if (type.equals(RegisterConstants.H)){
            if (num > 0x7F || num<-0x80)
                throw new IllegalArgumentException("参数赋值超出范围");
            data = register.getHighData();
            result = data ^ num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setHighBits(bits);
            data = register.getHighData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getHighBits());
        }else if(type.equals(RegisterConstants.L)){
            if (num > 0x7F || num<-0x80)
                throw new IllegalArgumentException("参数赋值超出范围");
            data = register.getLowData();
            result = data ^ num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setLowBits(bits);
            data = register.getLowData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }
        CF = false;
        OF = false;
    }

    public void XOR(GeneralRegister register, Integer type,GeneralRegister register2, Integer type2){
        if (type == 0 && type2 == 1 || type == 0 && type2 == 2 ||
                type == 1 && type2 == 0 || type == 2 && type2 == 0){
            throw new IllegalArgumentException("8位寄存器不能与16位寄存器做AND运算");
        }
        int result;
        Integer num;
        Integer data;
        if (type.equals(RegisterConstants.X)){
            num = register2.getData();
            data = register.getData();
            result = data ^ num;
            result &= 0xFFFF;
            Integer[] bits = ALUUtils.numToB(result, register.getSize());
            register.setBits(bits);
            data = register.getData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }else if (type.equals(RegisterConstants.H)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            data = register.getHighData();
            result = data ^ num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setHighBits(bits);
            data = register.getHighData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getHighBits());
        }else if (type.equals(RegisterConstants.L)){
            num = register2.getLowData();
            if (type2.equals(RegisterConstants.H))
                num = register2.getHighData();
            data = register.getLowData();
            result = data ^ num;
            result &= 0xFF;
            Integer[] bits = ALUUtils.numToB(result, register.getLsize());
            register.setLowBits(bits);
            data = register.getLowData();
            ZF = data == 0;
            SF = data < 0;
            setPF(register.getLowBits());
        }
        CF = false;
        OF = false;
    }

    public void NOT(GeneralRegister register, Integer type){
        Integer[] bits;
        if (type.equals(RegisterConstants.X)){
            bits = register.getBits();
            for (int i = 0; i < register.getSize(); i++) {
                bits[i] = 1- bits[i];
            }
            register.setBits(bits);
        }else if(type.equals(RegisterConstants.H)){
            bits = register.getHighBits();
            for (int i = 0; i < register.getLsize(); i++) {
                bits[i] = 1- bits[i];
            }
            register.setHighBits(bits);
        }else if(type.equals(RegisterConstants.L)){
            bits = register.getLowBits();
            for (int i = 0; i < register.getLsize(); i++) {
                bits[i] = 1- bits[i];
            }
            register.setLowBits(bits);
        }
    }

    /**
     *  无符号的乘法
     * @param register BX或CX
     * @param type X或H或L
     * @param ax  必须为AX寄存器
     * @param dx  必须为DX寄存器
     */
    public void MUL(GeneralRegister register, Integer type,GeneralRegister ax,GeneralRegister dx){
        if (type.equals(RegisterConstants.X)){
            //进行无符号数转换
            Integer num1 = ALUUtils.BToNum(register.getBits());
            Integer num2 = ALUUtils.BToNum(ax.getBits());
            int result = num1 * num2;
            num1 = result & 0xFFFF;
            num2 = result & 0xFFFF0000;
            num2 >>= 16;
            ax.setBits(ALUUtils.numToB(num1,16));
            dx.setBits(ALUUtils.numToB(num2,16));
            ZF = result == 0;
        }else if(type.equals(RegisterConstants.H)){
            Integer num1 = ALUUtils.BToNum(register.getHighBits());
            Integer num2 = ALUUtils.BToNum(ax.getLowBits());
            int result = num1 * num2;
            ax.setBits(ALUUtils.numToB(result,16));
            ZF = result == 0;
        }else if(type.equals(RegisterConstants.L)){
            Integer num1 = ALUUtils.BToNum(register.getLowBits());
            Integer num2 = ALUUtils.BToNum(ax.getLowBits());
            int result = num1 * num2;
            ax.setBits(ALUUtils.numToB(result,16));
            ZF = result == 0;
        }
    }

    /**
     *  无符号的除法
     * @param register BX或CX
     * @param type X或H或L
     * @param ax  必须为AX寄存器
     * @param dx  必须为DX寄存器
     */
    public void DIV(GeneralRegister register, Integer type,GeneralRegister ax,GeneralRegister dx){
        if (type.equals(RegisterConstants.X)){
            //进行无符号数转换
            Integer num1 = ALUUtils.BToNum(dx.getBits())*(0x10000)+ALUUtils.BToNum(ax.getBits());
            Integer num2 = ALUUtils.BToNum(register.getBits());
            int quotient = num1/num2;
            int remainder = num1%num2;
            ax.setBits(ALUUtils.numToB(quotient,16));
            dx.setBits(ALUUtils.numToB(remainder,16));
            ZF = quotient == 0;
        }else if(type.equals(RegisterConstants.H)){
            Integer num1 = ALUUtils.BToNum(ax.getBits());
            Integer num2 = ALUUtils.BToNum(register.getHighBits());
            int quotient = num1/num2;
            int remainder = num1%num2;
            ax.setLowBits(ALUUtils.numToB(quotient,16));
            ax.setHighBits(ALUUtils.numToB(remainder,16));
            ZF = quotient == 0;
        }else if(type.equals(RegisterConstants.L)){
            Integer num1 = ALUUtils.BToNum(ax.getBits());
            Integer num2 = ALUUtils.BToNum(register.getLowBits());
            int quotient = num1/num2;
            int remainder = num1%num2;
            ax.setLowBits(ALUUtils.numToB(quotient,16));
            ax.setHighBits(ALUUtils.numToB(remainder,16));
            ZF = quotient == 0;
        }
    }


}
