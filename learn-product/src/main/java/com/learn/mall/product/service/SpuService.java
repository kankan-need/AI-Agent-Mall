package com.learn.mall.product.service;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.product.dto.SpuDTO;
import com.learn.mall.product.dto.SpuPageSearchDTO;
import com.learn.mall.product.vo.SpuVO;

import java.util.List;

public interface SpuService {

    PageVO<SpuVO> page(PageDTO pageDTO, SpuPageSearchDTO searchDTO);

    PageVO<SpuVO> appPage(PageDTO pageDTO, Long categoryId, String name);

    SpuVO getBySpuId(Long spuId);

    void save(SpuDTO spuDTO);

    void update(SpuDTO spuDTO);

    void updateStatus(Long spuId, Integer status);

    void deleteById(Long spuId);

    List<SpuVO> searchForAgent(String keyword, int limit);

    List<SpuVO> listBriefByIds(List<Long> spuIds);
}
