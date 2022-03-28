/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: OrderrServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 11:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.order.service.Impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Orderr;
import com.bnuz.electronic_supermarket.common.javaBean.Product;
import com.bnuz.electronic_supermarket.common.javaBean.Store;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.order.dao.OrderMapper;
import com.bnuz.electronic_supermarket.order.dto.OrderDto;
import com.bnuz.electronic_supermarket.order.dto.PaymentDto;
import com.bnuz.electronic_supermarket.order.enums.OrderrEnum;
import com.bnuz.electronic_supermarket.order.service.OrderrService;
import com.bnuz.electronic_supermarket.product.service.ProductService;
import com.bnuz.electronic_supermarket.store.service.StoreService;
import com.bnuz.electronic_supermarket.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderrServiceImpl extends ServiceImpl<OrderMapper, Orderr> implements OrderrService {

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderrServiceImpl.class);

    @Autowired
    private OrderMapper orderDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;


    /**
     * 购买商品，确认订单但未完成支付，(这里的sql语句展现的业务是：确认订单，该订单里面的商品都只能是某一个商店里的商品)
     * 默认前端支付成功返回支付编号，then生成订单
     * @param orderDto
     * @return
     */
    @Transactional
    @Override
    public Map<String,Object> confirmOrder(OrderDto orderDto) {
        try {
            //验证products<productId,number>  select count(*) from product where id in keySet AND storeId = storeId;
            HashMap<String, String> products = orderDto.getProducts();
            QueryWrapper<Product>queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",products.keySet()).eq("storeId",orderDto.getStoreId());
            //传进来购买这么多个商品ID which are all in the same store，如果都找到了，那么map的长度是跟count相等的
            if(products.size() != productService.count(queryWrapper)){
                throw new MsgException("某个商品ID或店铺ID有误");
            }
            //从StpUtil的Session中获取个人信息(登录的时候就已经存进去了，不用解析json直接拿就好了)
            User user1 = (User) StpUtil.getSession().get(UserTypeEnum.USER.getName());
            //校验完毕
            Orderr orderr = new Orderr();
            //雪花算法snowFlake生成分布式系统下订单ID   1505528456231038976
            orderr.setId(IdUtil.getSnowflakeNextIdStr());
            orderr.setAccount(user1.getAccount());
            orderr.setDiliveryMethod(orderDto.getDiliveryMethod());
            orderr.setOrderTime(LocalDateTimeUtils.getLocalDateTime());
            orderr.setPackageId(null);
            orderr.setPayId(null);
            orderr.setPayMethod(null);
            orderr.setPayNumber(null);
            //将HashMap products转化为json字符串存到数据库
            orderr.setProductIds(GsonUtil.getGson().toJson(products));
            orderr.setState(OrderrEnum.NotPay.getIndex());
            orderr.setStoreId(orderDto.getStoreId());
            orderr.setTotalPrice(orderDto.getTotalPrice());
            int save = orderDao.insert(orderr);
            if(save <= 0) throw new MsgException("确认订单失败");
            HashMap<String,Object> map = new HashMap<>();
            map.put("order",orderr);
            return map;
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw e;
        } finally {

        }
    }

    /**
     * 订单已经确认，现在付钱了，修改订单的状态为确认订单且已支付。然后定时任务通知骑手接单。
     * 事务：减SKU stock keeping unit 的库存,修改订单     注意并发，多个线程减库存
     * @param paymentDto
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> payOrder(PaymentDto paymentDto, String orderId) {
        try{
            Orderr orderr = orderDao.selectById(orderId);
            if(orderr == null) throw new MsgException("订单ID有误");
            if(orderr.getState() != OrderrEnum.NotPay.getIndex()) throw new MsgException("订单状态异常，不能正常支付");
            //sku减库存 json_HashMap<String,String>
            String json_productIds = orderr.getProductIds();
            HashMap<String, String>products = GsonUtil.getGson().fromJson(json_productIds,HashMap.class);
            //并发更新商品的库存，需要java.util.conCurrent并发包的某些并发技术支持,ReentrantLock使用非公平锁
            if(Product.getReentrantLock().tryLock()){   //只有在调用时它不被另一个线程占用才能获取锁。
                //获得锁后
                try{                  //解决Gson解析数据用map接收时int自动转化为double问题
                    for(String productId : products.keySet()){
                        int number = Convert.toInt(products.get(productId));       //"1" to int 1
                        //获取该product的库存，然后减库存
                        Product updateProduct = productService.getById(productId);
                        if(updateProduct == null) throw new MsgException("下单的商品不存在: "+productId);
                        System.out.println("当前下单:"+orderId+",下单商品ID:"+productId+",该商品库存:"+updateProduct.getStock());
                        if(updateProduct.getStock()-number < 0) throw new MsgException("商品库存不足，下单失败");
                        updateProduct.setStock(updateProduct.getStock()-number);
                        UpdateWrapper<Product>updateWrapper = new UpdateWrapper<>();
                        updateWrapper.set("stock",updateProduct.getStock()).eq("id",productId);
                        boolean update = productService.update(null, updateWrapper);
                        if (update == false) throw new MsgException("商品库存不足，购买失败");
                        System.out.println("当前下单:"+orderId+" 已完成,下单商品ID:"+productId+",下单后商品库存:"+updateProduct.getStock());
                    }
                }catch (MsgException e){
                    throw e;
                }catch (Exception e){
                    throw e;
                } finally{
                    Product.getReentrantLock().unlock();  //尝试释放此锁。
                }
            }else{
                //被其他线程占用了
                throw new MsgException("下单用户过多，请稍后重试");
            }
            //todo 消息队列publish信息，外卖骑手收到消息 不做这个
            //update order回去数据库  标准的UpdateWrapper写法
            UpdateWrapper<Orderr>updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("payNumber",paymentDto.getPayNumber()).set("payMethod",paymentDto.getPayMethod());
            updateWrapper.set("payId",paymentDto.getPayId()).set("state",OrderrEnum.Payed.getIndex()).eq("id",orderId);
            int update = orderDao.update(orderr, updateWrapper);
            if(update <= 0) throw new MsgException("支付订单失败");
            HashMap<String,Object> map = new HashMap<>();
            map.put("orderId",orderr.getId());
            return map;
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw e;
        } finally {

        }
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Override
    public Map<String, Object> cancelOrder(String orderId) {
        try{
            Orderr orderr = orderDao.selectById(orderId);
            if(orderr == null) throw new MsgException("订单ID有误");
            orderr.setState(OrderrEnum.Cancel.getIndex());
            //update 回去数据库
            UpdateWrapper<Orderr>updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",orderr.getId());
            int update = orderDao.update(orderr, updateWrapper);
            if(update <= 0) throw new MsgException("取消订单失败");
            HashMap<String,Object> map = new HashMap<>();
            map.put("orderId",orderr.getId());
            return map;
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw e;
        } finally {

        }
    }

    @Override
    public Map<String, Object> queryByUserAccount(Integer currPage,Integer size,String account) {
        try{
            QueryWrapper<Orderr>queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account",account);
            Page<Orderr>page = new Page<>(currPage,size);
            Page<Orderr> page1 = orderDao.selectPage(page, queryWrapper);
            HashMap<String,Object> map = new HashMap<>();
            map.put("page",page1);
            return map;
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw e;
        } finally {

        }
    }
}