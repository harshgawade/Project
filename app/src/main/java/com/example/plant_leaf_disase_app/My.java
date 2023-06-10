package com.example.plant_leaf_disase_app;

public class My {

    String name,address,contactno,product,imageurl;

    public My() {
    }

    public My(String name, String address, String contactno, String product, String imageurl) {
        this.name = name;
        this.address = address;
        this.contactno = contactno;
        this.product = product;
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
