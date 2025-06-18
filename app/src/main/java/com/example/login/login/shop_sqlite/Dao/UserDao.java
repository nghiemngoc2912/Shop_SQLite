package com.example.login.login.shop_sqlite.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.Entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);
    @Query("SELECT * FROM User WHERE username = :name and password =:pass")
    LiveData<User> getProductByName(String name, String pass);

    @Update
    void update(User user);
}



