package com.learn.mall.product.service.impl;

import com.learn.mall.api.product.dto.StockLockItemDTO;
import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.product.mapper.SkuMapper;
import com.learn.mall.product.mapper.SpuMapper;
import com.learn.mall.product.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuMapper spuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(List<StockLockItemDTO> items) {
        for (StockLockItemDTO item : items) {
            int skuRows = skuMapper.reduceStock(item.getSkuId(), item.getCount());
            if (skuRows == 0) {
                throw new LearnMallException("商品库存不足");
            }
            int spuRows = spuMapper.reduceStock(item.getSpuId(), item.getCount());
            if (spuRows == 0) {
                throw new LearnMallException("商品库存不足");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockStock(List<StockLockItemDTO> items) {
        for (StockLockItemDTO item : items) {
            skuMapper.addStock(item.getSkuId(), item.getCount());
            spuMapper.addStock(item.getSpuId(), item.getCount());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmStock(List<StockLockItemDTO> items) {
        for (StockLockItemDTO item : items) {
            spuMapper.addSaleNum(item.getSpuId(), item.getCount());
        }
    }
}
