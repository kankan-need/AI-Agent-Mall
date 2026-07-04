package com.learn.mall.product.mapper;

import com.learn.mall.product.dto.SpuPageSearchDTO;
import com.learn.mall.product.model.Spu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpuMapper {

    Spu getById(@Param("spuId") Long spuId);

    List<Spu> page(@Param("shopId") Long shopId, @Param("search") SpuPageSearchDTO search,
                   @Param("begin") int begin, @Param("size") int size);

    long count(@Param("shopId") Long shopId, @Param("search") SpuPageSearchDTO search);

    List<Spu> listAppPage(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId,
                          @Param("name") String name, @Param("begin") int begin, @Param("size") int size);

    long countAppPage(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId,
                      @Param("name") String name);

    int save(Spu spu);

    int update(Spu spu);

    int updateStatus(@Param("spuId") Long spuId, @Param("status") Integer status);

    int countByCategoryId(@Param("categoryId") Long categoryId);

    int reduceStock(@Param("spuId") Long spuId, @Param("count") Integer count);

    int addStock(@Param("spuId") Long spuId, @Param("count") Integer count);

    int addSaleNum(@Param("spuId") Long spuId, @Param("count") Integer count);

    List<Spu> searchForAgent(@Param("shopId") Long shopId, @Param("keyword") String keyword,
                               @Param("limit") int limit);

    List<Spu> listByIds(@Param("spuIds") List<Long> spuIds);
}
