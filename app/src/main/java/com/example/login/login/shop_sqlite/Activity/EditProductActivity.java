package com.example.login.login.shop_sqlite.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.R;
import com.example.login.login.shop_sqlite.ViewModel.CartItemViewModel;
import com.example.login.login.shop_sqlite.ViewModel.ProductViewModel;

import java.util.Objects;

public class EditProductActivity extends AppCompatActivity {
    private EditText etName, etPrice, etQuantity, etDescription, etImage;
    private Button btnEdit, btnView;
    private ProductViewModel productViewModel;

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        currentProduct=(Product) Objects.requireNonNull(getIntent().getSerializableExtra("product"));

        // Khởi tạo ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Ánh xạ view
        etName = findViewById(R.id.et_product_name);
        etName.setText(currentProduct.name);
        etPrice = findViewById(R.id.et_product_price);
        etPrice.setText(String.valueOf(currentProduct.price));
        etQuantity = findViewById(R.id.et_product_quantity);
        etQuantity.setText(String.valueOf(currentProduct.quantity));
        etDescription = findViewById(R.id.et_product_description);
        etDescription.setText(currentProduct.description);
        etImage = findViewById(R.id.et_product_image);
        etImage.setText(currentProduct.image);
        btnEdit = findViewById(R.id.btn_edit_product);
        btnView =findViewById(R.id.btn_products);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(v -> updateProduct());
    }

    private void updateProduct() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String image = etImage.getText().toString().trim();

        // Kiểm tra đầu vào
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tên, giá và số lượng", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            Product product = new Product(name, price, description, quantity, image);
            product.id = currentProduct.id;
            productViewModel.update(product);

            Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
            finish(); // Đóng activity sau khi thêm xong

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem(int productID, String username) {
        CartItemViewModel cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel .class);
        cartItemViewModel.getCartItem(username, productID).observe(this, new Observer<CartItem>() {
            @Override
            public void onChanged(CartItem cartItem) {
                if (cartItem == null) {
                    // Chưa có -> thêm mới
                    cartItemViewModel.insert(new CartItem(username, productID, 1));
                } else {
                    // Đã có -> tăng số lượng
                    int newQuantity = cartItem.quantity + 1;
                    cartItemViewModel.updateProductFields(productID, newQuantity, username);
                }

                // Ngừng observe sau khi xử lý xong để tránh gọi lại nhiều lần
                cartItemViewModel.getCartItem(username, productID).removeObserver(this);
            }
        });
    }
}
