package com.learn.mall.product.service;

import com.learn.mall.api.product.dto.StockLockItemDTO;

import java.util.List;

public interface StockService {

    void lockStock(List<StockLockItemDTO> items);

    void unlockStock(List<StockLockItemDTO> items);

    void confirmStock(List<StockLockItemDTO> items);
}
