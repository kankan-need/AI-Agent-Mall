package com.learn.mall.product.controller.app;

import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.product.constant.ProductConstant;
import com.learn.mall.product.service.CategoryService;
import com.learn.mall.product.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ua/category")
public class AppCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category_list")
    public ServerResponseEntity<List<CategoryVO>> categoryList(
            @RequestParam(value = "parentId", defaultValue = "0") Long parentId,
            @RequestParam(value = "shopId", defaultValue = "1") Long shopId) {
        return ServerResponseEntity.success(categoryService.listByParentId(shopId, parentId));
    }

    @GetMapping("/shop_category_list")
    public ServerResponseEntity<List<CategoryVO>> shopCategoryList(
            @RequestParam(value = "shopId", defaultValue = "1") Long shopId) {
        return ServerResponseEntity.success(categoryService.list(shopId));
    }
}
