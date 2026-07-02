package com.learn.mall.order.vo;

public class OrderItemVO {

    private Long orderItemId;
    private Long spuId;
    private Long skuId;
    private Integer count;
    private String spuName;
    private String pic;
    private Long price;
    private Long spuTotalAmount;

    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Long getSpuId() { return spuId; }
    public void setSpuId(Long spuId) { this.spuId = spuId; }
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getSpuName() { return spuName; }
    public void setSpuName(String spuName) { this.spuName = spuName; }
    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public Long getSpuTotalAmount() { return spuTotalAmount; }
    public void setSpuTotalAmount(Long spuTotalAmount) { this.spuTotalAmount = spuTotalAmount; }
}
