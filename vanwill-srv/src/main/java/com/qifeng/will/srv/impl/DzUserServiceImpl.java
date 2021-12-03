package com.qifeng.will.srv.impl;

import com.qifeng.will.base.warrior.entity.DzUser;
import com.qifeng.will.srv.service.DzUserService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DzUserServiceImpl implements DzUserService {

    @Override
    public Set<String> selectByExample(String id) {
        return null;
    }

    @Override
    public boolean insertSelective(DzUser dzUser) {
        return false;
    }
}
