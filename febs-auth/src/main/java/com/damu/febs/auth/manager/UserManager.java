package com.damu.febs.auth.manager;

import com.damu.febs.auth.mapper.MenuMapper;
import com.damu.febs.auth.mapper.UserMapper;
import com.damu.febs.common.entity.system.Menu;
import com.damu.febs.common.entity.system.SystemUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManager {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    public SystemUser findByName(String username) {
        return userMapper.findByName(username);
    }

//    public String findUserPermissions(String username) {
//        List<Menu> userPermissions = menuMapper.findUserPermissions(username);
//
//        List<String> perms = new ArrayList<>();
//        for (Menu m: userPermissions){
//            perms.add(m.getPerms());
//        }
//        return StringUtils.join(perms, ",");
//    }

    public String findUserPermissions(String username) {
        List<Menu> userPermissions = menuMapper.findUserPermissions(username);
        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
    }
}
