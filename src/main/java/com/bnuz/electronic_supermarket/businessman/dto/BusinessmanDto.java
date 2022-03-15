/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: BusinessmanDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/15 21:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.businessman.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.RestController;

/**
 *    Mybatis-plus：注意对应表名和字段名，因为BaseMapper<JavaBean>，这里是dto所以就不用@TableName和@TableField
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "商人Dto")
public class BusinessmanDto {
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("头像")
    private String headIamge;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("手机号码")
    private String phoneNumber;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("身份证正面照片链接")
    private String idCardFontPict;
    @ApiModelProperty("身份证背面照片链接")
    private String idCardBackPict;
    @ApiModelProperty("微信ID")
    private String wechatId;
}