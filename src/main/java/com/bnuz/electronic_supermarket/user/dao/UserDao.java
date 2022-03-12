package com.bnuz.electronic_supermarket.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * userDao用来CURD user表      Mapper层
 */
@Repository
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 用户注册
     * @param user
     * @return User
     */
    void insertUser(User user);

    /**
     *登录
     * @Param User
     * @return
     */
    User selectUsersByAccountPassword(String account,String password);

    /**
     * 根据用户手机号码查找用户资料
     * @param phone
     * @return
     */
    User selectUsersByPhone(String phone);

    /**
     * 检查用户账号是否可用
     * @param account
     * @return
     */
    User checkAccount(String account);

}
