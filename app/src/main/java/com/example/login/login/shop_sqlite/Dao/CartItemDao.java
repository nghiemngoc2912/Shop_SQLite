package com.example.login.login.shop_sqlite.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.CartProduct;
import com.example.login.login.shop_sqlite.Entity.Product;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    void insert(CartItem product);

    @Query("SELECT * FROM CartItem")
    LiveData<List<CartItem>> getAllCartItem();

    @Query("SELECT * FROM CartItem WHERE username = :uname")
    LiveData<List<CartItem>> getCartItemByName(String uname);

    @Query("SELECT * FROM CartItem WHERE username = :uname and productId =:productId")
    LiveData<CartItem> getCartItem(String uname, int productId);

    @Query("SELECT Product.id as id, username, name, price, CartItem.quantity as quantity, description, image" +
            " FROM Product inner join CartItem " +
            "on Product.id = CartItem.productId where username =:user")
    LiveData<List<CartProduct>> getCartProduct(String user);
    @Delete
    void delete(CartItem cartItem);
    @Query("DELETE FROM CartItem WHERE  productId= :productId and username =:user")
    void deleteById(int productId, String user);
    @Update
    void update(CartItem cartItem);

    @Query("UPDATE CartItem SET quantity =:quantity WHERE productId = :id " +
            "and username =:user")
    void updateProductFields(int id, int quantity, String user);

    @Query("DELETE FROM CartItem WHERE  username =:userName")
    void deleteByUser(String userName);
}



