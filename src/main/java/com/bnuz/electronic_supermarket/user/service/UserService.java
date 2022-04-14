package com.bnuz.electronic_supermarket.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import com.bnuz.electronic_supermarket.user.dto.UserRegisterDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户模块   实现功能如下： 注册、登陆、修改密码、修改手机号码、修改用户信息（头像、昵称、身份证）
 */

public interface UserService extends IService<User> {
    /**
     * 检查用户名是否可用，是否重复了
     * @param account
     * @return 是否可用
     */
    boolean checkAccount(String account);

    /**
     *用户注册，注册前需要检查用户名是否可用
     * @param user
     */
    String registerUser(UserRegisterDTO user);

    /**
     * 登录接口 用户用账号和密码进行登陆，返回token
     * @param account
     * @param password
     * @return
     */
    Map<String,String> login(String account, String password);

    /**
     * 通过userId获取用户信息。
     * @param userId
     * @return
     */
    User getInfo(String userId, HttpServletRequest request);


}
