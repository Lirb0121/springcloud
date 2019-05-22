package com.huonilaifu.service;

import com.huonilaifu.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/refactor")
public interface FeignService {

    @RequestMapping(value = "/hello1",method = RequestMethod.GET)
    String hello1();

    @RequestMapping(value = "/hello2",method = RequestMethod.GET)
    User hello2(@RequestParam("id") Long id);

}
