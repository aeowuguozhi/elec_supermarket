/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: OrderrController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 14:14
 * Description: 订单控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.order.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.*;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.order.dto.OrderDto;
import com.bnuz.electronic_supermarket.order.dto.PaymentDto;
import com.bnuz.electronic_supermarket.order.enums.OrderrEnum;
import com.bnuz.electronic_supermarket.order.service.Impl.OrderrServiceImpl;
import com.bnuz.electronic_supermarket.order.service.OrderrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "订单API")
@RestController
@RequestMapping("/order")
public class OrderrController {

    @Autowired
    private OrderrService orderrService;

    /**
     * 确认订单，订单状态是 确认订单为支付状态
     * @param orderDto
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("orderr-add")
    @ApiOperation("确认订单信息并提交订单（未支付）,订单状态设为2")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true)
    @PostMapping("/confirmOrder")
    public SysResult confirmOrder(@RequestBody OrderDto orderDto){
        try{
            Map<String, Object> map = orderrService.confirmOrder(orderDto);
            return new SysResult(SysResultEnum.Created.getIndex(), SysResultEnum.Created.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    /**
     * 订单需要从 确认未支付改为确认已经支付状态
     * @param paymentDto
     * @param orderId
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("orderr-update")
    @ApiOperation("未支付的订单进行支付，订单状态设为1")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true)
    })
    @PutMapping("/payOrder")
    public SysResult payOrder(@RequestBody PaymentDto paymentDto, @RequestParam("orderId") String orderId){
        try{
            Map<String, Object> map = orderrService.payOrder(paymentDto,orderId);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    /**
     * 取消订单，订单状态改为取消0
     * @param orderId
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("orderr-update")
    @ApiOperation("取消订单，订单状态设为0")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true)
    })
    @PutMapping("/cancelOrder")
    public SysResult cancelOrder(@RequestParam("orderId") String orderId){
        try{
            Map<String, Object> map = orderrService.cancelOrder(orderId);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    /**
     * 删除订单，真的删除
     * @param orderId
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("orderr-delete")
    @ApiOperation("删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true)
    })
    @DeleteMapping("/deleteOrder")
    public SysResult deleteOrder(@RequestParam("orderId") String orderId){
        try{
            boolean b = orderrService.removeById(orderId);
            if(!b) throw new MsgException("删除订单失败");
            Map<String, Object> map = new HashMap<>();
            map.put("orderId",orderId);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    /**
     * 用户或者商家可以根据订单ID查看订单
     * @param orderId
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("orderr-query")
    @ApiOperation("商家或者用户查看某个订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token/商家token", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true)
    })
    @GetMapping("/queryById")
    public SysResult queryOrderById(@RequestParam("orderId") String orderId){
        try{
            Orderr orderr = orderrService.getById(orderId);
            if(orderr == null) throw new MsgException("查询订单失败,检查订单ID");
            Map<String, Object> map = new HashMap<>();
            map.put("order",orderr);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    /**
     * 用户分页查找自己的订单信息
     * @param currPage
     * @param size
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("orderr-query")
    @ApiOperation("用户查看自己的订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "一页多少条记录", defaultValue = "10")
    })
    @GetMapping("/queryByUserToken")
    public SysResult queryOrderByUserId(@RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size){
        try{
            StpUtil.checkRoleOr(Administrator.Role,User.Role);
            //从StpUtil中取出用户个人信息
            User user = (User)StpUtil.getSession().get(UserTypeEnum.USER.getName());
            Map<String, Object> map = orderrService.queryByUserAccount(currPage, size, user.getAccount());
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

}