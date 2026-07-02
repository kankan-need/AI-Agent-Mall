package com.learn.mall.order.service.impl;

import com.learn.mall.api.product.dto.StockLockItemDTO;
import com.learn.mall.api.product.feign.ProductFeignClient;
import com.learn.mall.api.product.vo.ShopCartItemFeignVO;
import com.learn.mall.api.user.feign.UserAddrFeignClient;
import com.learn.mall.api.user.vo.UserAddrFeignVO;
import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.order.constant.OrderConstant;
import com.learn.mall.order.dto.OrderSearchDTO;
import com.learn.mall.order.dto.OrderSubmitDTO;
import com.learn.mall.order.mapper.OrderAddrMapper;
import com.learn.mall.order.mapper.OrderItemMapper;
import com.learn.mall.order.mapper.OrderMapper;
import com.learn.mall.order.mapper.PayInfoMapper;
import com.learn.mall.order.model.Order;
import com.learn.mall.order.model.OrderAddr;
import com.learn.mall.order.model.OrderItem;
import com.learn.mall.order.model.PayInfo;
import com.learn.mall.order.service.OrderService;
import com.learn.mall.order.service.OrderTransactionService;
import com.learn.mall.order.vo.OrderAddrVO;
import com.learn.mall.order.vo.OrderItemVO;
import com.learn.mall.order.vo.OrderPayInfoVO;
import com.learn.mall.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${learn.mall.order.unpaid-timeout-minutes:30}")
    private int unpaidTimeoutMinutes;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderAddrMapper orderAddrMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private UserAddrFeignClient userAddrFeignClient;
    @Autowired
    private OrderTransactionService orderTransactionService;

    @Override
    public Long submit(Long userId, OrderSubmitDTO dto) {
        ServerResponseEntity<List<ShopCartItemFeignVO>> cartResp = productFeignClient.listCheckedCartItems(userId);
        if (!cartResp.isSuccess() || cartResp.getData() == null || cartResp.getData().isEmpty()) {
            throw new LearnMallException("请先勾选购物车商品");
        }
        List<ShopCartItemFeignVO> cartItems = cartResp.getData();

        ServerResponseEntity<UserAddrFeignVO> addrResp = userAddrFeignClient.getAddr(dto.getAddrId(), userId);
        if (!addrResp.isSuccess() || addrResp.getData() == null) {
            throw new LearnMallException("收货地址不存在");
        }
        UserAddrFeignVO userAddr = addrResp.getData();

        List<StockLockItemDTO> stockItems = buildStockItems(cartItems);
        ServerResponseEntity<Void> lockResp = productFeignClient.lockStock(stockItems);
        if (!lockResp.isSuccess()) {
            throw new LearnMallException(lockResp.getMsg());
        }

        try {
            return orderTransactionService.persistOrder(userId, cartItems, userAddr);
        } catch (Exception e) {
            productFeignClient.unlockStock(stockItems);
            if (e instanceof LearnMallException) {
                throw (LearnMallException) e;
            }
            throw new LearnMallException("下单失败");
        }
    }

    @Override
    public void cancel(Long userId, Long orderId, Integer closeType) {
        Order order = orderMapper.getByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new LearnMallException("订单不存在");
        }
        if (!Objects.equals(order.getStatus(), OrderConstant.STATUS_UNPAY)) {
            throw new LearnMallException("当前订单状态不可取消");
        }
        doCancel(orderId, closeType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pay(Long userId, Long orderId) {
        Order order = orderMapper.getByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new LearnMallException("订单不存在");
        }
        if (!Objects.equals(order.getStatus(), OrderConstant.STATUS_UNPAY)) {
            throw new LearnMallException("订单状态不可支付");
        }

        LocalDateTime now = LocalDateTime.now();
        int rows = orderMapper.updatePaySuccess(orderId, now);
        if (rows == 0) {
            throw new LearnMallException("支付失败，订单状态已变更");
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(userId);
        payInfo.setOrderIds(String.valueOf(orderId));
        payInfo.setBizPayNo("MOCK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        payInfo.setPayStatus(OrderConstant.PAY_PAID);
        payInfo.setPayAmount(order.getTotal());
        payInfo.setConfirmTime(now);
        payInfoMapper.save(payInfo);

        List<StockLockItemDTO> stockItems = buildStockItemsFromOrder(orderId);
        ServerResponseEntity<Void> confirmResp = productFeignClient.confirmStock(stockItems);
        if (!confirmResp.isSuccess()) {
            throw new LearnMallException(confirmResp.getMsg());
        }
    }

    @Override
    public OrderPayInfoVO payInfo(Long userId, Long orderId) {
        OrderVO detail = detail(orderId, userId);
        OrderPayInfoVO vo = new OrderPayInfoVO();
        vo.setOrderId(detail.getOrderId());
        vo.setTotal(detail.getTotal());
        vo.setCreateTime(detail.getCreateTime());
        vo.setExpireTime(detail.getCreateTime().plusMinutes(unpaidTimeoutMinutes));
        vo.setOrderAddr(detail.getOrderAddr());
        vo.setOrderItems(detail.getOrderItems());
        return vo;
    }

    @Override
    public PageVO<OrderVO> page(PageDTO pageDTO, OrderSearchDTO search) {
        int size = pageDTO.getPageSize() == null ? 10 : pageDTO.getPageSize();
        long total = orderMapper.count(search);
        List<Order> orders = orderMapper.page(search, pageDTO.getBegin(), size);
        PageVO<OrderVO> pageVO = new PageVO<>();
        List<OrderVO> list = new ArrayList<>();
        for (Order order : orders) {
            list.add(toOrderVO(order, true));
        }
        pageVO.setList(list);
        pageVO.setTotal(total);
        pageVO.setPages((int) Math.ceil(total * 1.0 / size));
        return pageVO;
    }

    @Override
    public OrderVO detail(Long orderId, Long userId) {
        Order order = userId == null ? orderMapper.getById(orderId) : orderMapper.getByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new LearnMallException("订单不存在");
        }
        return toOrderVO(order, true);
    }

    @Override
    public void cancelExpiredOrders() {
        LocalDateTime expireBefore = LocalDateTime.now().minusMinutes(unpaidTimeoutMinutes);
        List<Long> orderIds = orderMapper.listExpiredUnpaidIds(expireBefore);
        for (Long orderId : orderIds) {
            try {
                doCancel(orderId, OrderConstant.CLOSE_TIMEOUT);
            } catch (Exception ignored) {
            }
        }
    }

    private void doCancel(Long orderId, Integer closeType) {
        int rows = orderMapper.updateClose(orderId, closeType, LocalDateTime.now());
        if (rows == 0) {
            return;
        }
        List<StockLockItemDTO> stockItems = buildStockItemsFromOrder(orderId);
        productFeignClient.unlockStock(stockItems);
    }

    private OrderVO toOrderVO(Order order, boolean withItems) {
        OrderVO vo = BeanUtil.map(order, OrderVO.class);
        if (order.getOrderAddrId() != null) {
            OrderAddr addr = orderAddrMapper.getById(order.getOrderAddrId());
            vo.setOrderAddr(BeanUtil.map(addr, OrderAddrVO.class));
        }
        if (withItems) {
            List<OrderItem> items = orderItemMapper.listByOrderId(order.getOrderId());
            List<OrderItemVO> itemVos = new ArrayList<>();
            for (OrderItem item : items) {
                itemVos.add(BeanUtil.map(item, OrderItemVO.class));
            }
            vo.setOrderItems(itemVos);
        }
        return vo;
    }

    private List<StockLockItemDTO> buildStockItems(List<ShopCartItemFeignVO> cartItems) {
        List<StockLockItemDTO> list = new ArrayList<>();
        for (ShopCartItemFeignVO item : cartItems) {
            StockLockItemDTO dto = new StockLockItemDTO();
            dto.setSpuId(item.getSpuId());
            dto.setSkuId(item.getSkuId());
            dto.setCount(item.getCount());
            list.add(dto);
        }
        return list;
    }

    private List<StockLockItemDTO> buildStockItemsFromOrder(Long orderId) {
        List<OrderItem> items = orderItemMapper.listByOrderId(orderId);
        List<StockLockItemDTO> list = new ArrayList<>();
        for (OrderItem item : items) {
            StockLockItemDTO dto = new StockLockItemDTO();
            dto.setSpuId(item.getSpuId());
            dto.setSkuId(item.getSkuId());
            dto.setCount(item.getCount());
            list.add(dto);
        }
        return list;
    }
}
