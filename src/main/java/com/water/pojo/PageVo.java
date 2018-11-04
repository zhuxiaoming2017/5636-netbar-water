package com.water.pojo;

import java.io.Serializable;

public class PageVo implements Serializable {

    private Integer num=1;

    private Integer size=10;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
