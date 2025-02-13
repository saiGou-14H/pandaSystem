package com.saigou.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.entity.user.User;
import com.saigou.mapper.UserMapper;
import com.saigou.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public int add(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int delete(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int update(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }
}
