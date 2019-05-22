package com.huonilaifu.controller;

import com.huonilaifu.domain.User;
import com.huonilaifu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lirb
 * @date: 2019/5/22
 * @description:
 */
@RestController
public class FeignController {

    @Autowired
    private UserService userService;


    @RequestMapping("/getUser")
    public String  userInfo(){
        return userService.getUserInfo();
    }

    @RequestMapping("/getUserById")
    public User  getUserById(@RequestParam Long id){
        return userService.getUserById(id);
    }
}
