package com.saigou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saigou.entity.user.User;


import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author saigou
 * @since 2024-04-19
 */
public interface UserService extends IService<User> {
    int add(User user);
    int delete(Integer id);
    int update(User user);

    User getById(Integer id);
    List<User> getAll();

}
