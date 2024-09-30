package com.jojo.BIU;


import com.jojo.ALU.ALUUtils;
import com.jojo.CPU;
import com.jojo.Memory.Segment;
import com.jojo.Register.Register;
import lombok.SneakyThrows;

public class BIU {

    private final Register IP = new Register("IP",16);
    private Integer busData;
    private Integer[] busBits;

    public BIU(){
        setBusData(0);
    }

    @SneakyThrows
    public void fetch(Segment<String> CodeSegment, CPU cpu){
        String instruct;
        Integer ip = IP.getData();
        while((instruct = CodeSegment.read(ip))!=null) {
            //放入译码队列
            cpu.getDecodeQueue().put(instruct);
            System.out.println("取出指令: "+instruct+"成功,"+"ip为: "+ip);
            IP.setData(ip + 1);
            setBusData(IP.getData());
            ip = IP.getData();
            //模拟取指令周期
            Thread.sleep(200);
        }
    }

    public Register getIP() {
        return IP;
    }

    public Integer getBusData() {
        return busData;
    }

    public void setBusData(Integer busData) {
        this.busData = busData;
        busBits = ALUUtils.numToB(busData, 16);
    }

    public Integer[] getBusBits() {
        return busBits;
    }

    public void setBusBits(Integer[] busBits) {
        this.busBits = busBits;
        busData = ALUUtils.BToNum(busBits);
    }
}
