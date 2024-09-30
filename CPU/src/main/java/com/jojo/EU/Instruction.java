package com.jojo.EU;

public class Instruction {

    private String opcode;
    private String operands;

    public Instruction(){
    }

    public Instruction(String opcode, String operands) {
        this.opcode = opcode;
        this.operands = operands;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getOperands() {
        return operands;
    }

    public void setOperands(String operands) {
        this.operands = operands;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "opcode='" + opcode + '\'' +
                ", operands='" + operands + '\'' +
                '}';
    }
}
