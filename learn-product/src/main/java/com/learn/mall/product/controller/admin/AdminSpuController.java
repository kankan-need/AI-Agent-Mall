package com.learn.mall.product.controller.admin;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.product.dto.SpuDTO;
import com.learn.mall.product.dto.SpuPageSearchDTO;
import com.learn.mall.product.service.SpuService;
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

@RestController
@RequestMapping("/admin/spu")
public class AdminSpuController {

    @Autowired
    private SpuService spuService;

    @GetMapping("/page")
    public ServerResponseEntity<PageVO<SpuVO>> page(PageDTO pageDTO, SpuPageSearchDTO spuDTO) {
        return ServerResponseEntity.success(spuService.page(pageDTO, spuDTO));
    }

    @GetMapping
    public ServerResponseEntity<SpuVO> getBySpuId(@RequestParam Long spuId) {
        return ServerResponseEntity.success(spuService.getBySpuId(spuId));
    }

    @PostMapping
    public ServerResponseEntity<Void> save(@Valid @RequestBody SpuDTO spuDTO) {
        spuService.save(spuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    public ServerResponseEntity<Void> update(@Valid @RequestBody SpuDTO spuDTO) {
        spuService.update(spuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/prod_status")
    public ServerResponseEntity<Void> updateStatus(@RequestParam Long spuId, @RequestParam Integer status) {
        spuService.updateStatus(spuId, status);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    public ServerResponseEntity<Void> delete(@RequestParam Long spuId) {
        spuService.deleteById(spuId);
        return ServerResponseEntity.success();
    }
}
