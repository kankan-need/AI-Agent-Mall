package com.learn.mall.product.service.impl;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.product.constant.ProductConstant;
import com.learn.mall.product.dto.SpuDTO;
import com.learn.mall.product.dto.SpuPageSearchDTO;
import com.learn.mall.product.mapper.SkuMapper;
import com.learn.mall.product.mapper.SpuDetailMapper;
import com.learn.mall.product.mapper.SpuMapper;
import com.learn.mall.product.model.Sku;
import com.learn.mall.product.model.Spu;
import com.learn.mall.product.model.SpuDetail;
import com.learn.mall.product.service.SpuService;
import com.learn.mall.product.vo.SkuVO;
import com.learn.mall.product.vo.SpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Override
    public PageVO<SpuVO> page(PageDTO pageDTO, SpuPageSearchDTO searchDTO) {
        if (searchDTO == null) {
            searchDTO = new SpuPageSearchDTO();
        }
        int size = pageDTO.getPageSize() == null ? 10 : pageDTO.getPageSize();
        long total = spuMapper.count(ProductConstant.DEFAULT_SHOP_ID, searchDTO);
        List<Spu> spuList = spuMapper.page(ProductConstant.DEFAULT_SHOP_ID, searchDTO,
                pageDTO.getBegin(), size);
        return buildPage(spuList, total, size);
    }

    @Override
    public PageVO<SpuVO> appPage(PageDTO pageDTO, Long categoryId) {
        int size = pageDTO.getPageSize() == null ? 10 : pageDTO.getPageSize();
        long total = spuMapper.countAppPage(ProductConstant.DEFAULT_SHOP_ID, categoryId);
        List<Spu> spuList = spuMapper.listAppPage(ProductConstant.DEFAULT_SHOP_ID, categoryId,
                pageDTO.getBegin(), size);
        return buildPage(spuList, total, size);
    }

    @Override
    public SpuVO getBySpuId(Long spuId) {
        Spu spu = spuMapper.getById(spuId);
        if (spu == null) {
            return null;
        }
        SpuVO spuVO = BeanUtil.map(spu, SpuVO.class);
        SpuDetail detail = spuDetailMapper.getBySpuId(spuId);
        if (detail != null) {
            spuVO.setDetail(detail.getDetail());
        }
        spuVO.setSkus(toSkuVoList(skuMapper.listBySpuId(spuId)));
        return spuVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpuDTO spuDTO) {
        Spu spu = BeanUtil.map(spuDTO, Spu.class);
        spu.setShopId(ProductConstant.DEFAULT_SHOP_ID);
        spu.setStatus(spuDTO.getStatus() == null ? ProductConstant.STATUS_DISABLE : spuDTO.getStatus());
        spu.setMarketPriceFee(spuDTO.getMarketPriceFee() == null ? 0L : spuDTO.getMarketPriceFee());
        spu.setSaleNum(0);
        spu.setSeq(spuDTO.getSeq() == null ? 0 : spuDTO.getSeq());
        spuMapper.save(spu);

        SpuDetail detail = new SpuDetail();
        detail.setSpuId(spu.getSpuId());
        detail.setDetail(spuDTO.getDetail());
        spuDetailMapper.save(detail);

        Sku sku = new Sku();
        sku.setSpuId(spu.getSpuId());
        sku.setPriceFee(spu.getPriceFee());
        sku.setStock(spu.getStock());
        sku.setStatus(ProductConstant.STATUS_ENABLE);
        skuMapper.save(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SpuDTO spuDTO) {
        Spu exist = spuMapper.getById(spuDTO.getSpuId());
        if (exist == null) {
            throw new LearnMallException("商品不存在");
        }
        Spu spu = BeanUtil.map(spuDTO, Spu.class);
        spu.setShopId(ProductConstant.DEFAULT_SHOP_ID);
        spu.setMarketPriceFee(spuDTO.getMarketPriceFee() == null ? 0L : spuDTO.getMarketPriceFee());
        spu.setSeq(spuDTO.getSeq() == null ? 0 : spuDTO.getSeq());
        spuMapper.update(spu);

        SpuDetail detail = spuDetailMapper.getBySpuId(spu.getSpuId());
        if (detail == null) {
            detail = new SpuDetail();
            detail.setSpuId(spu.getSpuId());
            detail.setDetail(spuDTO.getDetail());
            spuDetailMapper.save(detail);
        } else {
            detail.setDetail(spuDTO.getDetail());
            spuDetailMapper.update(detail);
        }

        Sku sku = skuMapper.getBySpuId(spu.getSpuId());
        if (sku == null) {
            sku = new Sku();
            sku.setSpuId(spu.getSpuId());
            sku.setPriceFee(spu.getPriceFee());
            sku.setStock(spu.getStock());
            sku.setStatus(ProductConstant.STATUS_ENABLE);
            skuMapper.save(sku);
        } else {
            sku.setPriceFee(spu.getPriceFee());
            sku.setStock(spu.getStock());
            skuMapper.update(sku);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long spuId, Integer status) {
        spuMapper.updateStatus(spuId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long spuId) {
        spuMapper.updateStatus(spuId, ProductConstant.STATUS_DELETE);
    }

    private PageVO<SpuVO> buildPage(List<Spu> spuList, long total, int size) {
        PageVO<SpuVO> pageVO = new PageVO<>();
        List<SpuVO> list = new ArrayList<>(spuList.size());
        for (Spu spu : spuList) {
            list.add(BeanUtil.map(spu, SpuVO.class));
        }
        pageVO.setList(list);
        pageVO.setTotal(total);
        pageVO.setPages((int) Math.ceil(total * 1.0 / size));
        return pageVO;
    }

    private List<SkuVO> toSkuVoList(List<Sku> skus) {
        List<SkuVO> list = new ArrayList<>(skus.size());
        for (Sku sku : skus) {
            list.add(BeanUtil.map(sku, SkuVO.class));
        }
        return list;
    }
}
