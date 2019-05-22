package com.huonilaifu.service;

import com.huonilaifu.domain.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("spring-cloud-user")
public interface UserService {

    @RequestMapping("getUser")
    String getUserInfo();

    @RequestMapping(value = "userInfoById" ,method = RequestMethod.GET)
    User getUserById(@RequestParam(value = "id") Long id);

}
