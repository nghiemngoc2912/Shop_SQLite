package com.example.login.login.shop_sqlite.ViewModel;



import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.login.shop_sqlite.Activity.ProductListActivity;
import com.example.login.login.shop_sqlite.DataHelper.AppDatabase;
import com.example.login.login.shop_sqlite.Dao.CartItemDao;
import com.example.login.login.shop_sqlite.DataHelper.Constanst;
import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.CartProduct;
import com.example.login.login.shop_sqlite.Entity.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartItemViewModel extends AndroidViewModel {

    private final CartItemDao cartItemDao;
    private final ExecutorService executorService;

    public CartItemViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        cartItemDao = db.cartItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return cartItemDao.getAllCartItem();
    }

    public LiveData<List<CartItem>> getCartItemByUser(String username) {
        return cartItemDao.getCartItemByName(username);
    }

    public LiveData<List<CartProduct>> getCartProduct(String username) {
        return cartItemDao.getCartProduct(username);
    }

    public void insert(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.insert(cartItem));
    }
    public LiveData<CartItem> getCartItem(String userName, int productID) {
       return cartItemDao.getCartItem(userName,productID);
    }

    public void delete(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.delete(cartItem));
    }

    public void deleteById(int productId, String user) {
        executorService.execute(() -> cartItemDao.deleteById(productId, user));
    }

    public void update(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.update(cartItem));
    }

    public void updateProductFields(int productId, int quantity, String user) {
        executorService.execute(() -> cartItemDao.updateProductFields(productId, quantity, user));
    }

    public void deleteByUser(String userName) {
        executorService.execute(() -> cartItemDao.deleteByUser(userName));
    }


}
