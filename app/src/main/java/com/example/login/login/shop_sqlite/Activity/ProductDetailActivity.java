package com.example.login.login.shop_sqlite.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.login.login.shop_sqlite.Adapter.ProductAdapter;
import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.R;
import com.example.login.login.shop_sqlite.ViewModel.CartItemViewModel;
import com.example.login.login.shop_sqlite.ViewModel.ProductViewModel;

import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView idText, nameText, descriptionText, priceText, quantityText,imageText;
    Button buttonBuyNow, buttonAddToCart;
    private ProductViewModel productViewModel;

    private CartItemViewModel cartItemViewModel;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        buttonAddToCart = findViewById(R.id.btnAddToCart);

        imageView = findViewById(R.id.imgMain);
        imageText = findViewById(R.id.imgText);
        nameText = findViewById(R.id.txtProductName);
        descriptionText = findViewById(R.id.txtProductDescription);
        priceText = findViewById(R.id.txtProductPrice);
        quantityText = findViewById(R.id.txtProductQuantity);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            finish();
            return;
        }
        username = LoginActivity.username;
        ViewModelProvider provider = new ViewModelProvider(this);
        cartItemViewModel = provider.get(CartItemViewModel .class);
        productViewModel = provider.get(ProductViewModel.class);
        productViewModel.getProductById(productId).observe(this, product -> {

            if (product != null) {
                Glide.with(this)
                        .load(product.image)
                        .into(imageView);
                nameText.setText("Name: " + product.name);
                descriptionText.setText("Description: " + product.description);
                priceText.setText("Price: $" + product.price);
                quantityText.setText("Quantity: " + product.quantity);
                imageText.setText(product.image);

                buttonAddToCart.setOnClickListener((v)->{
                    addItem(productId);
                    Toast.makeText(ProductDetailActivity.this, "Added to Cart.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    public void addItem(int productID) {
        LiveData<CartItem> liveData = cartItemViewModel.getCartItem(username, productID);

        Observer<CartItem> observer = new Observer<CartItem>() {
            @Override
            public void onChanged(CartItem cartItem) {
                if (cartItem == null) {
                    // Sản phẩm chưa có trong giỏ
                    cartItemViewModel.insert(new CartItem(username, productID, 1));
                } else {
                    // Tăng số lượng nếu đã tồn tại
                    int newQuantity = cartItem.quantity + 1;
                    cartItemViewModel.updateProductFields(productID, newQuantity, username);
                }

                // Ngừng observe sau khi xử lý
                liveData.removeObserver(this);
            }
        };

        liveData.observe(this, observer);// this là Activity hoặc Fragment (LifecycleOwner)
    }
}

