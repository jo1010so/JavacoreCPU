package com.jojo.SR;

import com.jojo.CPU;
import com.jojo.Memory.Segment;
import com.jojo.Register.SegmentRegister;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SRTest {

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            6,
            6,
            20,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(6),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Test
    public void test01(){
        Segment<String> CodeSegment = new Segment<>("CodeSegment");
        CodeSegment.setOffset(0);
        CodeSegment.write(1,"MOV AX,1");
        String s = CodeSegment.read();
        String s1 = CodeSegment.read(1);
        System.out.println(s);
        System.out.println(s1);
    }

    @Test
    public void test02(){
        Segment<Integer> CodeSegment = new Segment<>("DataSegment");
        CodeSegment.setOffset(0);
        CodeSegment.write(1,7);
        Integer s = CodeSegment.read();
        Integer s1 = CodeSegment.read(1);
        System.out.println(s);
        System.out.println(s1);
    }

    @Test
    public void test03(){
        SegmentRegister register = new SegmentRegister("CS",16);
        register.setData(3);
        System.out.println(Arrays.toString(register.getBits()));
    }

    @Test
    public void test04(){
        CPU cpu = new CPU();
        cpu.getEu().loadCodes("src/main/java/com/jojo/code.txt",cpu.getMemory().getCodeSegment());
        /*cpu.run();*/
        while(true);
    }

    @Test
    public void test05(){
        CPU cpu = new CPU();
        cpu.getEu().loadCodes("src/main/java/com/jojo/code.txt",cpu.getMemory().getCodeSegment());
        /*cpu.run();*/
        while(true);
    }

    @Test
    public void fetchTest(){
        CPU cpu = new CPU();
        cpu.getEu().loadCodes("src/main/java/com/jojo/code.txt",cpu.getMemory().getCodeSegment());
        new Thread(new Runnable() {
            @Override
            public void run() {
                cpu.getBiu().fetch(cpu.getMemory().getCodeSegment(), cpu);
            }
        }).start();
        new Thread(()->{
            cpu.getEu().decode(cpu);
        }).start();
        new Thread(cpu::execute).start();
        while (true);
    }
    @Test
    public void fetchTest2(){
        CPU cpu = new CPU();
        cpu.getEu().loadCodes("src/main/java/com/jojo/code.txt",cpu.getMemory().getCodeSegment());
        threadPoolExecutor.execute(()-> cpu.getBiu().fetch(cpu.getMemory().getCodeSegment(), cpu));
        threadPoolExecutor.execute(()-> cpu.getEu().decode(cpu));
        threadPoolExecutor.execute(cpu::execute);
        while (true);
    }

    @Test
    public void decodeTest(){
        boolean b = Pattern.matches("^-?\\d+$","-3");
        System.out.println(b);
        String s = "AX,0,134,";
        String[] split = s.split(",");
        System.out.println(split.length);
    }

    @Test
    public void executeTest(){
        CPU cpu = new CPU();
        cpu.run("src/main/java/com/jojo/code.txt");
    }

    @Test
    public void setHigh(){

    }

}
