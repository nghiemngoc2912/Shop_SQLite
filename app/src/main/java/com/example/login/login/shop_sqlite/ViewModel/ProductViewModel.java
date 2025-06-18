package com.example.login.login.shop_sqlite.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.login.login.shop_sqlite.Dao.ProductDao;
import com.example.login.login.shop_sqlite.DataHelper.AppDatabase;
import com.example.login.login.shop_sqlite.Entity.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductViewModel extends AndroidViewModel {

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;
    private final ExecutorService executorService;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application); // Assuming singleton DB
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> getProductByName(String keyword) {
        return productDao.getProductByName("%" + keyword + "%");
    }

    public LiveData<Product> getProductById(int id) {
        return productDao.getProductById(id);
    }

    public void insert(Product product) {
        executorService.execute(() -> productDao.insert(product));
    }

    public void delete(Product product) {
        executorService.execute(() -> productDao.delete(product));
    }

    public void deleteById(int id) {
        executorService.execute(() -> productDao.deleteById(id));
    }

    public void update(Product product) {
        executorService.execute(() -> productDao.update(product));
    }

    public void updateProductFields(int id, String name, double price) {
        executorService.execute(() -> productDao.updateProductFields(id, name, price));
    }
}
