package com.mzvzm.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

public class Product {
    private String name;
    private Double price;
    private Integer saleTotal;

    public void setParam(JSONObject jsonObject) {
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            if ("name".equals(key)) {
                this.setName(jsonObject.getString(key));
            }
            if ("price".equals(key)) {
                this.setPrice(jsonObject.getDouble(key));
            }
            if ("saleTotal".equals(key)) {
                this.setSaleTotal(jsonObject.getInteger(key));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(Integer saleTotal) {
        this.saleTotal = saleTotal;
    }
}
