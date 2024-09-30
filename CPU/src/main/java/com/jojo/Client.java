package com.jojo;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CPU cpu = new CPU();
        System.out.println("请输入执行文件地址：");
        String filePath = scanner.nextLine();
        cpu.run(filePath);
    }
}
