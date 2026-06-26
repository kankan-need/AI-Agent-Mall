package com.learn.mall.product.vo;

import java.util.List;

public class SpuVO {

    private Long spuId;
    private Long categoryId;
    private Long shopId;
    private String name;
    private String sellingPoint;
    private String mainImgUrl;
    private String imgUrls;
    private Long priceFee;
    private Long marketPriceFee;
    private Integer status;
    private Integer stock;
    private Integer saleNum;
    private Integer seq;
    private String detail;
    private List<SkuVO> skus;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellingPoint() {
        return sellingPoint;
    }

    public void setSellingPoint(String sellingPoint) {
        this.sellingPoint = sellingPoint;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Long getPriceFee() {
        return priceFee;
    }

    public void setPriceFee(Long priceFee) {
        this.priceFee = priceFee;
    }

    public Long getMarketPriceFee() {
        return marketPriceFee;
    }

    public void setMarketPriceFee(Long marketPriceFee) {
        this.marketPriceFee = marketPriceFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<SkuVO> getSkus() {
        return skus;
    }

    public void setSkus(List<SkuVO> skus) {
        this.skus = skus;
    }
}
