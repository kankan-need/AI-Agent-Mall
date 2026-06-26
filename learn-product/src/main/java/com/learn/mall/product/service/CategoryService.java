package com.learn.mall.product.service;

import com.learn.mall.product.dto.CategoryDTO;
import com.learn.mall.product.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    CategoryVO getById(Long categoryId);

    List<CategoryVO> list(Long shopId);

    List<CategoryVO> listByParentId(Long shopId, Long parentId);

    void save(CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO);

    void deleteById(Long categoryId);
}
