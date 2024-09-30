package com.jojo.Register;

    public class GeneralRegister extends Register{

    private Integer[] highBits;
    private Integer[] lowBits;
    private Integer highData;
    private Integer lowData;
    private final Integer lsize;

    public GeneralRegister(String name, Integer size) {
        super(name, size);
        super.setData(0);
        this.lsize = super.getSize()/2;
        this.highData = 0;
        this.lowData = 0;
        this.highBits = new Integer[lsize];
        for (int i = 0; i < lsize; i++) {
            highBits[i] = 0;
        }
        this.lowBits = new Integer[lsize];
        for (int i = 0; i < lsize; i++) {
            lowBits[i] = 0;
        }
    }

    public void setHighBits(Integer[] highBits) {
        if(highBits.length != lsize || RegisterUtils.checkedArray(highBits)){
            throw new IllegalArgumentException("数组不符合要求");
        }
        this.highBits = highBits;
        this.highData = RegisterUtils.bitsToData(lsize,highBits);
        Integer[] bits = super.getBits();
        System.arraycopy(highBits, 0, bits, 0, lsize);
        super.setBits(bits);
    }

    public void setLowBits(Integer[] lowBits) {
        if(highBits.length != lsize || RegisterUtils.checkedArray(lowBits)){
            throw new IllegalArgumentException("数组不符合要求");
        }
        this.lowBits = lowBits;
        this.lowData = RegisterUtils.bitsToData(lsize,lowBits);
        Integer[] bits = super.getBits();
        for (int i = lsize; i < lsize*2; i++) {
            bits[i] = lowBits[i-lsize];
        }
        super.setBits(bits);
    }

    public void setHighData(Integer highData) {
        if (highData >= -Math.pow(2, lsize - 1) && highData < Math.pow(2, lsize - 1)) {
            this.highData = highData;
            this.highBits = RegisterUtils.dataToBits(lsize,highData);
            Integer[] bits = super.getBits();
            if (lsize >= 0) System.arraycopy(highBits, 0, bits, 0, lsize);
            super.setBits(bits);
        }else{
            throw new IllegalArgumentException("数据超出范围");
        }
    }

    public void setLowData(Integer lowData) {
        if (lowData >= -Math.pow(2, lsize - 1) && lowData < Math.pow(2, lsize - 1)) {
            this.lowData = lowData;
            this.lowBits = RegisterUtils.dataToBits(lsize,lowData);
            Integer[] bits = super.getBits();
            for (int i = lsize; i < lsize*2; i++) {
                bits[i] = lowBits[i-lsize];
            }
            super.setBits(bits);
        }else{
            throw new IllegalArgumentException("数据超出范围");
        }
    }

    @Override
    public void setData(Integer data) {
        super.setData(data);
        Integer[] bits = super.getBits();
        if (lsize >= 0) System.arraycopy(bits, 0, this.highBits, 0, lsize);
        for (int i = lsize; i < lsize*2; i++) {
            lowBits[i-lsize] = bits[i];
        }
        this.highData = RegisterUtils.bitsToData(lsize,highBits);
        this.lowData = RegisterUtils.bitsToData(lsize,lowBits);
    }

    @Override
    public void setBits(Integer[] bits) {
        super.setBits(bits);
        if (lsize >= 0) System.arraycopy(bits, 0, this.highBits, 0, lsize);
        for (int i = lsize; i < lsize*2; i++) {
            lowBits[i-lsize] = bits[i];
        }
        this.highData = RegisterUtils.bitsToData(lsize,highBits);
        this.lowData = RegisterUtils.bitsToData(lsize,lowBits);
    }

    public Integer[] getHighBits() {
        return this.highBits;
    }
    public Integer getHighData(){
        return this.highData;
    }

    public Integer[] getLowBits() {
        return lowBits;
    }

    public Integer getLowData() {
        return lowData;
    }

    public Integer getLsize() {
        return lsize;
    }
}
