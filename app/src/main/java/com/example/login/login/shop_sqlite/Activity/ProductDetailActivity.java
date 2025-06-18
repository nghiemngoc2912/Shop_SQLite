package com.example.login.login.shop_sqlite.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.R;
import com.example.login.login.shop_sqlite.ViewModel.ProductViewModel;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView idText, nameText, descriptionText, priceText, quantityText,imageText;
    Button buttonBuyNow, buttonAddToCart;
    private ProductViewModel productViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        imageView = findViewById(R.id.imgMain);
        imageText=findViewById(R.id.imgText);
        nameText = findViewById(R.id.txtProductName);
        descriptionText = findViewById(R.id.txtProductDescription);
        priceText = findViewById(R.id.txtProductPrice);
        quantityText=findViewById(R.id.txtProductQuantity);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            finish();
            return;
        }
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductById(productId).observe(this, product -> {

        if (product != null) {
            Glide.with(this)
                    .load(product.image)
                    .into(imageView);
            nameText.setText("Name: " + product.name);
            descriptionText.setText("Description: " + product.description);
            priceText.setText("Price: $" + product.price);
            quantityText.setText("Quantity: "+product.quantity);
            imageText.setText(product.image);
        }});
    }
}

