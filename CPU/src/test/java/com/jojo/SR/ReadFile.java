package com.jojo.SR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    public static void main(String[] args) {
        String filePath = "src/main/java/com/jojo/code.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                //进行译码
                String[] split = line.split(" ");
                String opcode = split[0];
                String operands = split[1];

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
