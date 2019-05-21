package com.huonilaifu.service.Impl;

import com.huonilaifu.service.UserCenterService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
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

//    @CacheResult(cacheKeyMethod = "userInfoCacheKey")
    @HystrixCommand(fallbackMethod = "backMethod")
    @Override
    public String userInfo() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://SPRING-CLOUD-USER/getUser", String.class);
        String body = forEntity.getBody();
        return body;
    }

    public String backMethod(){
        return "error";
    }

    public String userInfoCacheKey() {
        return "userInfo";
    }
}
