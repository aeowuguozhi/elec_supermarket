package com.bnuz.electronic_supermarker.user.dao;

import com.bnuz.electronic_supermarker.common.javaBean.User;
import com.bnuz.electronic_supermarker.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * userDao用来CURD user表
 */
@Repository
@Mapper
public interface UserDao {

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
