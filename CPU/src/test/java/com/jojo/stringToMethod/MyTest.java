package com.jojo.stringToMethod;

import com.jojo.ALU.ALU;
import com.jojo.Register.GeneralRegister;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MyTest {

    private static final GeneralRegister ax = new GeneralRegister("AX",16);
    private static final GeneralRegister bx = new GeneralRegister("BX",16);
    private static final GeneralRegister cx = new GeneralRegister("CX",16);
    private static final GeneralRegister dx = new GeneralRegister("DX",16);


    public static void main(String[] args) throws Exception {
        ALU alu = new ALU();
        dx.setBits(new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0});
        ax.setBits(new Integer[]{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1});
        bx.setData(16);
        System.out.println(ax.getData());
        String methodName = "AND";
        String parameters = "AX,0,15";
        String[] paramsArray = parameters.split(",");
        System.out.println(Arrays.toString(paramsArray));
        Class<?>[] paramTypes = getParameterTypes(methodName, paramsArray);
        System.out.println(Arrays.toString(paramTypes));
        Method method = ALU.class.getMethod(methodName, paramTypes);
        Object[] argsArray = convertParameters(paramTypes, paramsArray);
        method.invoke(alu, argsArray);
        System.out.println(Arrays.toString(ax.getBits()));
        System.out.println(Arrays.toString(dx.getBits()));
    }

    private static void exc(){

    }

    private static Class<?>[] getParameterTypes(String methodName, String[] paramsArray) {
        Method[] methods = ALU.class.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), getParameterClasses(paramsArray))) {
                return method.getParameterTypes();
            }
        }
        return null;
    }

    private static Class<?>[] getParameterClasses(String[] paramsArray) {
        Class<?>[] paramClasses = new Class<?>[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            try {
                Integer.parseInt(paramsArray[i]);
                paramClasses[i] = Integer.class;
            } catch (NumberFormatException e1) {
                paramClasses[i] = GeneralRegister.class;
            }
        }
        System.out.println(Arrays.toString(paramClasses));
        return paramClasses;
    }

    private static Object[] convertParameters(Class<?>[] paramTypes, String[] paramsArray) {
        Object[] convertedParams = new Object[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            if (paramTypes[i] == Integer.class) {
                convertedParams[i] = Integer.parseInt(paramsArray[i]);
            } else if (paramTypes[i] == GeneralRegister.class) {
                switch (paramsArray[i]) {
                    case "AX":
                        convertedParams[i] = ax;
                        break;
                    case "BX":
                        convertedParams[i] = bx;
                        break;
                    case "CX":
                        convertedParams[i] = cx;
                        break;
                    case "DX":
                        convertedParams[i] = dx;
                        break;
                }
            }
        }
        return convertedParams;
    }

}
