package com.jojo.stringToMethod;

import java.lang.reflect.Method;

public class LI {

    // 示例类，包含我们要调用的方法
    public static class MyClass {
        public void sayHello(String name) {
            System.out.println("Hello, " + name + "!");
        }

        public void addNumbers(int a, int b) {
            System.out.println("Sum: " + (a + b));
        }
    }

    public static void main(String[] argss) {
        try {
            // 目标对象
            MyClass obj = new MyClass();

            // 方法名和参数字符串
            String methodName = "sayHello"; // 或 "addNumbers"
            String parameters = "John"; // 或 "5,10" 对于 addNumbers

            // 分割参数
            String[] paramsArray = parameters.split(",");

            // 获取方法的参数类型
            Class<?>[] paramTypes = getParameterTypes(methodName, paramsArray);

            // 获取 Method 对象
            Method method = MyClass.class.getMethod(methodName, paramTypes);

            // 将参数转换为正确的类型
            Object[] args = convertParameters(paramTypes, paramsArray);

            // 调用方法
            method.invoke(obj, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?>[] getParameterTypes(String methodName, String[] paramsArray) throws NoSuchMethodException {
        // 通过参数类型推断方法
        Method[] methods = MyClass.class.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method.getParameterTypes();
            }
        }
        throw new NoSuchMethodException("No such method: " + methodName);
    }

    private static Object[] convertParameters(Class<?>[] paramTypes, String[] paramsArray) {
        Object[] convertedParams = new Object[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            if (paramTypes[i] == int.class) {
                convertedParams[i] = Integer.parseInt(paramsArray[i]);
            } else if (paramTypes[i] == double.class) {
                convertedParams[i] = Double.parseDouble(paramsArray[i]);
            } else {
                convertedParams[i] = paramsArray[i];
            }
        }
        return convertedParams;
    }
}
