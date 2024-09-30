package com.jojo.Memory;

public class Segment<T> {
    private final String name;
    private T[] memory = (T[]) new Object[1024];
    private Integer offset = 0;
    public Segment(String name){
        this.name = name;
    }
    public T read(Integer offset){
        return memory[offset];
    }
    public T read(){
        return memory[offset];
    }
    public void write(Integer offset,T data){
        memory[offset] = data;
        System.out.println("向"+name+"写入"+(T) data+"成功");
    }
    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setMemory(T[] memory){
        this.memory  = memory;
    }

}
