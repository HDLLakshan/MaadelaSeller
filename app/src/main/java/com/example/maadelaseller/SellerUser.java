package com.example.maadelaseller;

public class SellerUser {
    private String id;
    private String shopname;
    private String phonenum;
    private String Address;
    private String AccNo;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAccNo() {
        return AccNo;
    }

    public void setAccNo(String accNo) {
        AccNo = accNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
}
