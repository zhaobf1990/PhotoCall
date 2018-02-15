package com.zhaobf.phonecall.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by zhaobf on 2018/2/13.
 */

public class Person implements Serializable, Comparable {
    /**
     * 生成的唯一编号
     */
    public String uuid;
    /**
     * 序号
     */
    public Integer number;
    /**
     * 手机号
     */
    public String tel;
    /**
     * 姓名
     */
    public String name;
    /**
     * 头像
     */
    public String headImage;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Person aaa = (Person) o;
        return this.getNumber() - aaa.getNumber();
//        return 0;
    }
}
