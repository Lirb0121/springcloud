package com.huonilaifu.service;

import com.huonilaifu.domain.User;

/**
 * @author: lirb
 * @date: 2019/5/17
 * @description:
 */
public interface UserCenterService {

    String userInfo();

    User userInfoById(Long id);
}
