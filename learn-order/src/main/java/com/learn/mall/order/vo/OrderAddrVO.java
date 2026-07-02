package com.learn.mall.order.vo;

public class OrderAddrVO {

    private Long orderAddrId;
    private String consignee;
    private String province;
    private String city;
    private String area;
    private String addr;
    private String mobile;

    public Long getOrderAddrId() { return orderAddrId; }
    public void setOrderAddrId(Long orderAddrId) { this.orderAddrId = orderAddrId; }
    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getAddr() { return addr; }
    public void setAddr(String addr) { this.addr = addr; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
