package com.learn.mall.order.model;

import java.time.LocalDateTime;

public class OrderAddr {

    private Long orderAddrId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long userId;
    private String consignee;
    private Long provinceId;
    private String province;
    private Long cityId;
    private String city;
    private Long areaId;
    private String area;
    private String addr;
    private String postCode;
    private String mobile;

    public Long getOrderAddrId() { return orderAddrId; }
    public void setOrderAddrId(Long orderAddrId) { this.orderAddrId = orderAddrId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }
    public Long getProvinceId() { return provinceId; }
    public void setProvinceId(Long provinceId) { this.provinceId = provinceId; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getAddr() { return addr; }
    public void setAddr(String addr) { this.addr = addr; }
    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
