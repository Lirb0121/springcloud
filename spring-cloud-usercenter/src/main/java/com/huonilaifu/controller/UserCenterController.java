package com.huonilaifu.controller;

import com.huonilaifu.domain.User;
import com.huonilaifu.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: lirb
 * @date: 2019/5/16
 * @description:
 */
@RestController
public class UserCenterController {

    @Autowired
    private UserCenterService userCenterService;

    @RequestMapping("/userInfo")
    public String userInfo() {
        return userCenterService.userInfo();
    }
    @RequestMapping("/userInfoById")
    public User userInfoById(@RequestParam Long id) {
        User s = userCenterService.userInfoById(id);
        return s;
    }

}
