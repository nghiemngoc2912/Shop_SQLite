package com.example.login.login.shop_sqlite.Activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.login.shop_sqlite.Adapter.ProductAdapter;
import com.example.login.login.shop_sqlite.DataHelper.Constanst;
import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.CartProduct;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.R;
import com.example.login.login.shop_sqlite.ViewModel.CartItemViewModel;
import com.example.login.login.shop_sqlite.ViewModel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductListActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
     CartItemViewModel cartItemViewModel;// = new ViewModelProvider(this).get(CartItemViewModel .class);
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Danh sách sản phẩm");

        // RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel .class);
        productAdapter = new ProductAdapter(new ProductAdapter.onCartChange() {
            @Override
            public void addToCard(Product product) {
                addItem(product.id, Constanst.userName);
            }
        });
        recyclerView.setAdapter(productAdapter);

        // ViewModel setup
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        productViewModel.getAllProducts().observe(this, productAdapter::setProductList);



        // FAB thêm sản phẩm
        fabAddProduct = findViewById(R.id.fab_add_product);
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }
    public void addItem(int productID, String username) {
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

        liveData.observe(this, observer); // this là Activity hoặc Fragment (LifecycleOwner)
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);

        // Tìm item SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Bắt sự kiện khi người dùng nhấn enter (submit)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.trim().equalsIgnoreCase("all"))
                    //list LIst of Product
                    productViewModel.getAllProducts().observe(ProductListActivity.this, productAdapter::setProductList);
                else
                {
                    query = "%"+query.replaceAll(" ","%")+"%";
                    productViewModel.getProductByName(query).observe(ProductListActivity.this, productAdapter::setProductList);

                }
                searchView.clearFocus();
                // Thu gọn lại SearchView
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng đang gõ (nếu cần)
                return false;
            }
        });
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.products)
            productViewModel.getAllProducts().observe(ProductListActivity.this, productAdapter::setProductList);
        if (id == R.id.action_cart) {
            // Xử lý khi người dùng nhấn nút 🛒
            Toast.makeText(this, "Bạn đã chọn Giỏ hàng", Toast.LENGTH_SHORT).show();

            // (Tùy chọn) Chuyển sang màn hình giỏ hàng
            Intent intent = new Intent(this, CardActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}

