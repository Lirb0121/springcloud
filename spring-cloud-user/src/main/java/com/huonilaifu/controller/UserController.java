package com.huonilaifu.controller;

import com.huonilaifu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author: lirb
 * @date: 2019/5/16
 * @description:
 */
@RestController
public class UserController {

    @Autowired
    DiscoveryClient discoveryClient;
    @RequestMapping("/getUser")
    public String getUser() throws InterruptedException {
//        int i = new Random().nextInt(3000);
//        Thread.sleep(i);
        ServiceInstance localServiceInstance = discoveryClient.getLocalServiceInstance();
        System.out.println("host:"+localServiceInstance.getHost()+",serviceId:"+
                localServiceInstance.getServiceId());
        return "user name is lirb";
    }


    @RequestMapping("/userInfoById")
    public User userInfoById(@RequestParam Long id) throws InterruptedException {
        ServiceInstance localServiceInstance = discoveryClient.getLocalServiceInstance();
        System.out.println("host:"+localServiceInstance.getHost()+",serviceId:"+
                localServiceInstance.getServiceId());
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
