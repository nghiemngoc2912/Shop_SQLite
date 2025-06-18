package com.example.login.login.shop_sqlite.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.login.login.shop_sqlite.Entity.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM Product ORDER BY id DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM Product WHERE name like :name")
    LiveData<List<Product>> getProductByName(String name);
    @Query("SELECT * FROM Product where Id =:id")
    LiveData<Product> getProductById(int id);
    @Delete
    void delete(Product product);
    @Query("DELETE FROM Product WHERE id = :productId")
    void deleteById(int productId);
    @Update
    void update(Product product);

    @Query("UPDATE Product SET name = :name, price = :price WHERE id = :id")
    void updateProductFields(int id, String name, double price);



}



