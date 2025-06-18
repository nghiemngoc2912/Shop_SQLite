package com.example.login.login.shop_sqlite.Entity;

public class CartProduct {
        public String username;
        public String name;
        public double price;
        public String image;
        public int id;
    public int quantity; // from CartItem
    public String description;

    public CartProduct(int id, String username,String name, double price, int quantity, String description, String image) {
        this.id =id;
        this.username = username;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image =image;
    }

    public CartProduct() {
    }



}
