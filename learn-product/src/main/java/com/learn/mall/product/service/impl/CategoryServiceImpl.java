package com.learn.mall.product.service.impl;

import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.product.constant.ProductConstant;
import com.learn.mall.product.dto.CategoryDTO;
import com.learn.mall.product.mapper.CategoryMapper;
import com.learn.mall.product.mapper.SpuMapper;
import com.learn.mall.product.model.Category;
import com.learn.mall.product.service.CategoryService;
import com.learn.mall.product.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SpuMapper spuMapper;

    @Override
    public CategoryVO getById(Long categoryId) {
        Category category = categoryMapper.getById(categoryId);
        return BeanUtil.map(category, CategoryVO.class);
    }

    @Override
    public List<CategoryVO> list(Long shopId) {
        return toVoList(categoryMapper.listByShopId(shopId));
    }

    @Override
    public List<CategoryVO> listByParentId(Long shopId, Long parentId) {
        return toVoList(categoryMapper.listByShopIdAndParentId(shopId, parentId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CategoryDTO categoryDTO) {
        Category category = BeanUtil.map(categoryDTO, Category.class);
        category.setShopId(ProductConstant.DEFAULT_SHOP_ID);
        category.setStatus(categoryDTO.getStatus() == null ? ProductConstant.STATUS_ENABLE : categoryDTO.getStatus());
        category.setSeq(categoryDTO.getSeq() == null ? 0 : categoryDTO.getSeq());
        category.setLevel(buildLevel(categoryDTO.getParentId()));
        category.setPath(buildPath(categoryDTO.getParentId(), null));
        categoryMapper.save(category);
        category.setPath(buildPath(categoryDTO.getParentId(), category.getCategoryId()));
        categoryMapper.update(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CategoryDTO categoryDTO) {
        Category category = BeanUtil.map(categoryDTO, Category.class);
        categoryMapper.update(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long categoryId) {
        if (categoryMapper.countChildren(categoryId) > 0) {
            throw new LearnMallException("请先删除子分类");
        }
        if (spuMapper.countByCategoryId(categoryId) > 0) {
            throw new LearnMallException("分类下存在商品，无法删除");
        }
        categoryMapper.deleteById(categoryId);
    }

    private int buildLevel(Long parentId) {
        if (parentId == null || parentId == 0L) {
            return 0;
        }
        Category parent = categoryMapper.getById(parentId);
        if (parent == null) {
            return 0;
        }
        return parent.getLevel() + 1;
    }

    private String buildPath(Long parentId, Long categoryId) {
        if (parentId == null || parentId == 0L) {
            return categoryId == null ? "" : String.valueOf(categoryId);
        }
        Category parent = categoryMapper.getById(parentId);
        if (parent == null) {
            return categoryId == null ? "" : String.valueOf(categoryId);
        }
        if (categoryId == null) {
            return parent.getPath();
        }
        return parent.getPath() + "-" + categoryId;
    }

    private List<CategoryVO> toVoList(List<Category> categories) {
        List<CategoryVO> list = new ArrayList<>(categories.size());
        for (Category category : categories) {
            list.add(BeanUtil.map(category, CategoryVO.class));
        }
        return list;
    }
}
