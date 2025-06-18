package com.example.login.login.shop_sqlite.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "price")
    public double price;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "quantity")
    public int quantity;
    @ColumnInfo(name = "image")
    public String image;

    public Product(String name, double price, String description, int quantity, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.quantity=quantity;
    }


    public Product() {
    }

}
