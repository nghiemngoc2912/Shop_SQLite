package com.example.login.login.shop_sqlite.DataHelper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.login.login.shop_sqlite.Dao.CartItemDao;
import com.example.login.login.shop_sqlite.Dao.ProductDao;
import com.example.login.login.shop_sqlite.Dao.UserDao;
import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.Entity.User;


    @Database(entities = {Product.class, CartItem.class, User.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {
        private static volatile AppDatabase INSTANCE;

        public abstract ProductDao productDao();
        public abstract UserDao userDao();
        public abstract CartItemDao cartItemDao();

        public static AppDatabase getInstance(Context context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "shopping.sqlite")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }


