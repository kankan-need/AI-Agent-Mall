package com.learn.mall.order.mapper;

import com.learn.mall.order.dto.OrderSearchDTO;
import com.learn.mall.order.model.Order;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper {

    Order getById(@Param("orderId") Long orderId);

    Order getByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    List<Order> page(@Param("search") OrderSearchDTO search, @Param("begin") int begin, @Param("size") int size);

    long count(@Param("search") OrderSearchDTO search);

    int save(Order order);

    int updatePaySuccess(@Param("orderId") Long orderId, @Param("payTime") LocalDateTime payTime);

    int updateClose(@Param("orderId") Long orderId, @Param("closeType") Integer closeType,
                    @Param("cancelTime") LocalDateTime cancelTime);

    List<Long> listExpiredUnpaidIds(@Param("expireBefore") LocalDateTime expireBefore);
}
