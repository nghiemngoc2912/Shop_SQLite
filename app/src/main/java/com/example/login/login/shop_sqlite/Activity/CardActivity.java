package com.example.login.login.shop_sqlite.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.login.shop_sqlite.Adapter.CardAdapter;
import com.example.login.login.shop_sqlite.Entity.CartProduct;
import com.example.login.login.shop_sqlite.ViewModel.CartItemViewModel;
import com.example.login.login.shop_sqlite.R;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CardActivity extends AppCompatActivity {
    private RecyclerView recyclerCart;
    private TextView textTotal;
    private Button btnCheckout;
   private CartItemViewModel cartItemViewModel;
    final int REQUEST_CALL_PHONE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
// gắn toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);

        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);

        recyclerCart = findViewById(R.id.recyclerCart);
        textTotal = findViewById(R.id.textTotal);
        btnCheckout = findViewById(R.id.btnCheckout);


        CardAdapter adapter = new CardAdapter(CardActivity.this,  new CardAdapter.OnProductChangeListener() {
            @Override
            public void onProductChange(CartProduct cartProduct) {

               cartItemViewModel.updateProductFields(cartProduct.id, cartProduct.quantity, cartProduct.username);
                calculateTotal();
            }

            @Override
            public void onDeleteProduct(CartProduct cartProduct) {
                calculateTotal();
                cartItemViewModel.deleteById(cartProduct.id,  cartProduct.username);

            }
        });
        cartItemViewModel.getCartProduct(LoginActivity.username).observe(this, adapter::setProductList);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);

        calculateTotal();


        btnCheckout.setOnClickListener(v -> {
            List<CartProduct> currentList = cartItemViewModel.getCartProduct(LoginActivity.username).getValue();
            int count = (currentList != null) ? currentList.size() : 0;
            if( count==0)
            Toast.makeText(this, "Bạn chưa có sản phẩm nào, Pls chọn sản phẩm...", Toast.LENGTH_SHORT).show();
            else {
                cartItemViewModel.deleteByUser(LoginActivity.username);
            }
            startActivity(new Intent(this, ProductListActivity.class));
        });
    }



    private void calculateTotal() {

        cartItemViewModel.getCartProduct(LoginActivity.username).observe(this, new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> list) {
                int sum=0;
                // Lúc này list đã có dữ liệu
                if (list != null) {
                    for (CartProduct p : list)
                        if(p!=null)
                            sum += p.price*p.quantity;
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedPrice = formatter.format(sum*1000);
                    textTotal.setText("Tổng: "+formattedPrice + "₫");
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart_product, menu);
        MenuItem card = menu.findItem(R.id.action_cart);

//        card.setVisible(false);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.products) {
            // Xử lý khi người dùng nhấn nút 🛒
            Toast.makeText(this, "Bạn đã chọn về trang Products", Toast.LENGTH_SHORT).show();

            // (Tùy chọn) Chuyển sang màn hình products List
            Intent intent = new Intent(this, ProductListActivity.class);
            // intent.putExtra("card", (ArrayList) card.getProductList());  // cartList là ArrayList<Product>
            startActivity(intent);

            return true;
        }
       return super.onOptionsItemSelected(item);
    }


}
