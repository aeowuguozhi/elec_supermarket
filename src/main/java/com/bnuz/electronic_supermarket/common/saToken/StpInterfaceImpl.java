/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserStpInterfaceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/18 15:29
 * Description: saToken权限码：User
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.saToken;

import cn.dev33.satoken.stp.StpInterface;
import com.bnuz.electronic_supermarket.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarket.common.javaBean.Administrator;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展 https://sa-token.dev33.cn/doc/index.html#/use/jur-auth
 */

@Component       // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    /**
     * 权限认证,返回一个账号所拥有的权限码集合       权限码集合，demo里先测试用户和商家,管理员两个权限。
     * 统一loginId格式为    prefix + "_" + UUID
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        if(loginId.toString().contains(User.myPrefix)){
            for (String permission:User.myPermission
                 ) {
                list.add(permission);
            }
        }else if(loginId.toString().contains(Businessman.myPrefix)){
            for (String permission:Businessman.myPermission
                 ) {
                list.add(permission);
            }
        }else if(loginId.toString().contains(Administrator.myPrefix)){
            list.add(Administrator.myPermission);
        }
        return list;
    }

    /**
     * 角色认证,返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)      角色集合
     * @param loginId
     * @param s
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String s) {
        List<String> list = new ArrayList<String>();
        if(loginId.toString().contains(User.myPrefix)){
            list.add(User.Role);
        }else if(loginId.toString().contains(Businessman.myPrefix)){
            list.add(Businessman.Role);
        }else if(loginId.toString().contains(Administrator.myPrefix)){
            list.add(Administrator.Role);
        }
        return list;
    }
}