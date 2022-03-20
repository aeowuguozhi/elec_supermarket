package com.bnuz.electronic_supermarket.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.javaBean.Orderr;
import com.bnuz.electronic_supermarket.order.dto.OrderDto;
import com.bnuz.electronic_supermarket.order.dto.PaymentDto;

import java.util.Map;

public interface OrderrService extends IService<Orderr> {

    Map<String,Object> confirmOrder(OrderDto orderDto);

    Map<String,Object> payOrder(PaymentDto paymentDto, String orderId);

    Map<String,Object> cancelOrder(String orderId);

    Map<String,Object> queryByUserAccount(Integer currPage,Integer size,String account);
}
