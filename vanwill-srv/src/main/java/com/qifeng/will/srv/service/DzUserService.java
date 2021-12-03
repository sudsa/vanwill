package com.qifeng.will.srv.service;

import com.qifeng.will.base.warrior.entity.DzUser;

import java.util.Set;

public interface DzUserService {

    Set<String> selectByExample(String id);

    boolean insertSelective(DzUser dzUser);
}
