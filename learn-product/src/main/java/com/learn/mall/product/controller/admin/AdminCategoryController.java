package com.learn.mall.product.controller.admin;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.product.constant.ProductConstant;
import com.learn.mall.product.dto.CategoryDTO;
import com.learn.mall.product.dto.SpuDTO;
import com.learn.mall.product.dto.SpuPageSearchDTO;
import com.learn.mall.product.service.CategoryService;
import com.learn.mall.product.service.SpuService;
import com.learn.mall.product.vo.CategoryVO;
import com.learn.mall.product.vo.SpuVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ServerResponseEntity<CategoryVO> getByCategoryId(@RequestParam Long categoryId) {
        return ServerResponseEntity.success(categoryService.getById(categoryId));
    }

    @GetMapping("/shop_categories")
    public ServerResponseEntity<List<CategoryVO>> shopCategories() {
        return ServerResponseEntity.success(categoryService.list(ProductConstant.DEFAULT_SHOP_ID));
    }

    @GetMapping("/get_list_by_parent_id")
    public ServerResponseEntity<List<CategoryVO>> getListByParentId(@RequestParam Long parentId) {
        return ServerResponseEntity.success(
                categoryService.listByParentId(ProductConstant.DEFAULT_SHOP_ID, parentId));
    }

    @PostMapping
    public ServerResponseEntity<Void> save(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    public ServerResponseEntity<Void> update(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    public ServerResponseEntity<Void> delete(@RequestParam Long categoryId) {
        categoryService.deleteById(categoryId);
        return ServerResponseEntity.success();
    }
}
