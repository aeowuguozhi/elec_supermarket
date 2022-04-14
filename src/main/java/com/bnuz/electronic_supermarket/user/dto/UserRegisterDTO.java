/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserRegisDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/24 13:59
 * Description: 用户注册dto【
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.user.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("用户注册Dto")
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @ApiModelProperty(value = "微信ID")
    private String wechatId;
    @ApiModelProperty(value = "账号",required = true)
    private String account;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty("头像链接")
    private String headImage;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty(value = "手机号码",required = true)
    private String phoneNumber;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("邮箱")
    private String email;
}