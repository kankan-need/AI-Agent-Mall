package com.learn.mall.product.controller.app;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.product.service.SpuService;
import com.learn.mall.product.vo.SpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ua/spu")
public class AppSpuController {

    @Autowired
    private SpuService spuService;

    @GetMapping("/prod_info")
    public ServerResponseEntity<SpuVO> prodInfo(@RequestParam("spuId") Long spuId) {
        return ServerResponseEntity.success(spuService.getBySpuId(spuId));
    }

    @GetMapping("/page")
    public ServerResponseEntity<PageVO<SpuVO>> page(PageDTO pageDTO,
                                                    @RequestParam(value = "categoryId", required = false) Long categoryId) {
        return ServerResponseEntity.success(spuService.appPage(pageDTO, categoryId));
    }
}
