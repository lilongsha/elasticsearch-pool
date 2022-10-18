package com.mzvzm.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzvzm.EsHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("es")
public class EsController {
    private final EsHelper helper;

    public EsController(EsHelper helper) {
        this.helper = helper;
    }

    @PutMapping("/index")
    public Boolean createIndex(@RequestBody JSONObject jsonObject) {
        return helper.createIndex(jsonObject.getString("index"));
    }

    @PostMapping("/batchAdd")
    public void batchAdd(@RequestBody JSONObject jsonObject) {
        Object o = helper.batchAdd(jsonObject);
        return;
    }

    @PostMapping("/add")
    public void add(@RequestBody JSONObject jsonObject) {
        Object o = helper.add(jsonObject);
        return;
    }
}
