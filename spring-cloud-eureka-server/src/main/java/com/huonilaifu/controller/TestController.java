package com.huonilaifu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lirb
 * @date: 2019/5/16
 * @description:
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "eureka server start";
    }
}
