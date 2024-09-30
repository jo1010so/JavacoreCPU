package com.jojo;

import com.jojo.ALU.ALU;
import com.jojo.BIU.BIU;
import com.jojo.EU.EU;
import com.jojo.EU.Instruction;
import com.jojo.Memory.Memory;
import com.jojo.Register.GeneralRegister;
import com.jojo.Register.SegmentRegister;
import lombok.SneakyThrows;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.*;

public class CPU {
    private final GeneralRegister AX = new GeneralRegister("AX",16);
    private final GeneralRegister BX = new GeneralRegister("BX",16);
    private final GeneralRegister CX = new GeneralRegister("CX",16);
    private final GeneralRegister DX = new GeneralRegister("DX",16);
    private final SegmentRegister CS = new SegmentRegister("CS",16);
    private final SegmentRegister DS = new SegmentRegister("DS",16);
    private final SegmentRegister ES = new SegmentRegister("ES",16);
    private final SegmentRegister SS = new SegmentRegister("SS",16);
    private final ALU ALU = new ALU();
    private final Memory memory = new Memory();
    private final BlockingQueue<String> decodeQueue = new LinkedBlockingQueue<>(100);
    private final BlockingQueue<Instruction> executeQueue = new LinkedBlockingQueue<>(100);
    private final BIU biu = new BIU();
    private final EU eu = new EU();

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            6,
            6,
            20,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(6),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    public void run(String filePath){
        this.getEu().loadCodes(filePath,this.getMemory().getCodeSegment());
        threadPoolExecutor.execute(()-> this.getBiu().fetch(this.getMemory().getCodeSegment(), this));
        threadPoolExecutor.execute(()-> this.getEu().decode(this));
        threadPoolExecutor.execute(this::execute);
        threadPoolExecutor.shutdown();
        try {
            // 等待直到所有任务完成或超时
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPoolExecutor.shutdownNow(); // 超时后强制关闭
            }
        } catch (InterruptedException e) {
            threadPoolExecutor.shutdownNow(); // 当前线程被中断时强制关闭
        }
    }

    @SneakyThrows
    public void execute() {
        while(true)
            while (!executeQueue.isEmpty()) {
                Instruction instruction = executeQueue.take();
                String methodName = instruction.getOpcode();
                if (methodName.equals("HLT")) {
                    System.out.println();
                    System.out.println("程序执行结束");
                    System.out.println("各寄存器值为：");
                    show();
                    return;
                }
                String parameters = instruction.getOperands();
                String[] paramsArray = parameters.split(",");
                Class<?>[] paramTypes = getParameterTypes(methodName, paramsArray);
                Method method = ALU.class.getMethod(methodName, paramTypes);
                Object[] argsArray = convertParameters(paramTypes, paramsArray);
                method.invoke(ALU, argsArray);
                System.out.println("该指令执行结束");
                show();
            }
    }

    private  Class<?>[] getParameterTypes(String methodName, String[] paramsArray) {
        Method[] methods = ALU.class.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), getParameterClasses(paramsArray))) {
                return method.getParameterTypes();
            }
        }
        return null;
    }

    private  Class<?>[] getParameterClasses(String[] paramsArray) {
        Class<?>[] paramClasses = new Class<?>[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            try {
                Integer.parseInt(paramsArray[i]);
                paramClasses[i] = Integer.class;
            } catch (NumberFormatException e1) {
                paramClasses[i] = GeneralRegister.class;
            }
        }
        //System.out.println(Arrays.toString(paramClasses));
        return paramClasses;
    }

    private  Object[] convertParameters(Class<?>[] paramTypes, String[] paramsArray) {
        Object[] convertedParams = new Object[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            if (paramTypes[i] == Integer.class) {
                convertedParams[i] = Integer.parseInt(paramsArray[i]);
            } else if (paramTypes[i] == GeneralRegister.class) {
                switch (paramsArray[i]) {
                    case "AX":
                        convertedParams[i] = AX;
                        break;
                    case "BX":
                        convertedParams[i] = BX;
                        break;
                    case "CX":
                        convertedParams[i] = CX;
                        break;
                    case "DX":
                        convertedParams[i] = DX;
                        break;
                }
            }
        }
        return convertedParams;
    }

    public void show(){
        System.out.println(
                "AX:"+"\t"+AX.getData()+"\t"+"\t"+"\t"+Arrays.toString(AX.getBits())+"\n" +
                "BX:"+"\t"+BX.getData()+"\t"+"\t"+"\t"+Arrays.toString(BX.getBits())+"\n" +
                "CX:"+"\t"+CX.getData()+"\t"+"\t"+"\t"+Arrays.toString(CX.getBits())+"\n" +
                "DX:"+"\t"+DX.getData()+"\t"+"\t"+"\t"+Arrays.toString(DX.getBits())+"\n" +
                "Flags:["+ALU.showFlags()+"]"+"\n"
        );
    }

    public GeneralRegister getAX() {
        return AX;
    }

    public GeneralRegister getBX() {
        return BX;
    }

    public GeneralRegister getCX() {
        return CX;
    }

    public GeneralRegister getDX() {
        return DX;
    }


    public com.jojo.ALU.ALU getALU() {
        return ALU;
    }

    public Memory getMemory() {
        return memory;
    }

    public BlockingQueue<String> getDecodeQueue() {
        return decodeQueue;
    }

    public BlockingQueue<Instruction> getExecuteQueue() {
        return executeQueue;
    }

    public BIU getBiu() {
        return biu;
    }

    public EU getEu() {
        return eu;
    }
}
