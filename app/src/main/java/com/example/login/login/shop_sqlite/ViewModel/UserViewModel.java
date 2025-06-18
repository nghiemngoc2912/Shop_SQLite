package com.example.login.login.shop_sqlite.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.login.login.shop_sqlite.DataHelper.AppDatabase;
import com.example.login.login.shop_sqlite.Dao.UserDao;
import com.example.login.login.shop_sqlite.Entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<User> getUserByUsernameAndPassword(String username, String password) {
        return userDao.getProductByName(username, password);
    }

    public void insert(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.update(user));
    }
}
