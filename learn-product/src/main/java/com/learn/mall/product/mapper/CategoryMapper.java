package com.learn.mall.product.mapper;

import com.learn.mall.product.model.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {

    Category getById(@Param("categoryId") Long categoryId);

    List<Category> listByShopId(@Param("shopId") Long shopId);

    List<Category> listByShopIdAndParentId(@Param("shopId") Long shopId, @Param("parentId") Long parentId);

    int save(Category category);

    int update(Category category);

    int deleteById(@Param("categoryId") Long categoryId);

    int countChildren(@Param("categoryId") Long categoryId);
}
