package com.saigou.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.entity.User;
import com.saigou.mapper.IUserMapper;
import com.saigou.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author saigou
 * @since 2024-04-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<IUserMapper, User> implements IUserService {
    @Resource
    IUserMapper userMapper;
    @Override
    public int add(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int delete(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int update(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }
    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }
    public User getByAccount(String account) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);
        return userMapper.selectOne(wrapper);
    }
}
