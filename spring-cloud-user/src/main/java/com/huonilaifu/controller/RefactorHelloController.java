package com.huonilaifu.controller;

import com.huonilaifu.domain.User;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lirb
 * @date: 2019/5/22
 * @description:
 */
@RestController
//public class RefactorHelloController implements FeignService {
public class RefactorHelloController{
//    @Override
    public String hello1() {
        return "lirb is a good man ";
    }

//    @Override
    public User hello2(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("lirb");
        user.setPwd("df");
        user.setAge(10);
        user.setDesc("lirb is a good man");
        user.setPhone("17736186055");
        return user;
    }
}
