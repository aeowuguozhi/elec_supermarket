/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: specification
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 12:15
 * Description: 规格名字
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * lombok mybatis-plus api
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("规格")
@TableName("specification")
public class Specification {
    private String id;
    @ApiModelProperty("规格名,eg:内存,颜色,外存,CPU型号")
    private String name;
}