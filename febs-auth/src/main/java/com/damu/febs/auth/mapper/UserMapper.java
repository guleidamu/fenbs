package com.damu.febs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damu.febs.common.entity.system.SystemUser;

public interface UserMapper extends BaseMapper<SystemUser> {
    SystemUser findByName(String username);
}