    package com.jojo.Register;

    public class Register {
        private final String name;
        private Integer data;
        private final Integer size;
        private Integer[] bits;

        public Register(String name, Integer size) {
            this.name = name;
            this.size = size;
            this.bits = new Integer[size];
            this.data = 0;
        }

        public void setData(Integer data) {
            if (data >= -Math.pow(2, size - 1) && data < Math.pow(2, size - 1)) {
                this.data = data;
                this.bits = RegisterUtils.dataToBits(size,data);
            } else {
                throw new IllegalArgumentException("数值超出范围");
            }
        }

        public void setBits(Integer[] bits){
            if(bits.length != size || RegisterUtils.checkedArray(bits)){
                throw new IllegalArgumentException("数组不符合要求");
            }
            this.bits = bits;
            this.data = RegisterUtils.bitsToData(size,bits);
        }

        public Integer getData() {
            return this.data;
        }

        public String getName() {
            return name;
        }

        public Integer getSize() {
            return size;
        }

        public Integer[] getBits() {
            return bits;
        }

    }

