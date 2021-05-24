package com.baizhi.service;


import com.baizhi.entity.Perms;
import com.baizhi.entity.User;

import java.util.List;

public interface UserService {
    //注册用户
    void register(User user) ;
    User findByUserName(String username) ;
    User findRolesByUserName(String username) ;
    //    根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id) ;
}
