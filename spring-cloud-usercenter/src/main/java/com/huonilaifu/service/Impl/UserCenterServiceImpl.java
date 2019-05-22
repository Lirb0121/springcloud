package com.huonilaifu.service.Impl;

import com.huonilaifu.domain.User;
import com.huonilaifu.service.UserCenterService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author: lirb
 * @date: 2019/5/17
 * @description:
 */
@Service
public class UserCenterServiceImpl implements UserCenterService {

    @Autowired
    private RestTemplate restTemplate;


    @HystrixCommand(fallbackMethod = "backMethod")
    @Override
    public String userInfo() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://SPRING-CLOUD-USER/getUser", String.class);
        String body = forEntity.getBody();
        return body;
    }

//    @CacheResult
    @HystrixCommand(fallbackMethod = "backMethod2")
    @Override
    public User userInfoById( Long id) {
        User user = restTemplate.getForObject("http://SPRING-CLOUD-USER/userInfoById?id={1}", User.class, id);
        return user;
    }

    public String backMethod(){
        return "error";
    }
    public User backMethod2(Long id){
        User user = new User();
        return user;
    }

    private Long userByIdKey(Long id){
        return id;
    }
}
