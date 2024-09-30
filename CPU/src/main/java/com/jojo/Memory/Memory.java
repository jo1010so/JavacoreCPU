package com.jojo.Memory;

public class Memory {

    private final Segment<String> CodeSegment = new Segment<>("CodeSegment");
    private final Segment<Integer> DataSegment = new Segment<>("DataSegment");
    private final Segment<Integer> ExtraSegment = new Segment<>("ExtraSegment");
    private final Segment<Integer> StackSegment = new Segment<>("StackSegment");

    public Segment<String> getCodeSegment() {
        return CodeSegment;
    }

    public Segment<Integer> getDataSegment() {
        return DataSegment;
    }

    public Segment<Integer> getExtraSegment() {
        return ExtraSegment;
    }

    public Segment<Integer> getStackSegment() {
        return StackSegment;
    }
}
