package com.example.login.login.shop_sqlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.login.shop_sqlite.Entity.CartItem;
import com.example.login.login.shop_sqlite.Entity.CartProduct;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.R;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CartViewHolder> {
    private List<CartProduct> cartList;
    OnProductChangeListener listener;
    Context context;


    public void setProductList(List<CartProduct> list) {

        this.cartList = list;
        notifyDataSetChanged();
    }

    public CardAdapter(Context context,  OnProductChangeListener listener) {
        cartList = new ArrayList<>();
        this.listener=listener;
        this.context=context;
    }
    public interface OnProductChangeListener {
        void onProductChange(CartProduct cartProduct);
        void onDeleteProduct(CartProduct cartProduct);
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartProduct product = cartList.get(position);
        holder.textName.setText(product.name);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.price*1000);
        holder.textPrice.setText(formattedPrice + "₫");
        holder.textQuantity.setText(product.quantity+"");

        Glide.with(holder.imageView.getContext())
                .load(product.image)
                .placeholder(R.drawable.ic_launcher_background) // ảnh mặc định khi loading
                .error(R.drawable.ic_err_image_layy) // ảnh khi lỗi
                .into(holder.imageView);
        holder.btdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")

                        .setPositiveButton("Xóa", (dialog, which) -> {
                           listener.onDeleteProduct(cartList.get(position));
                            Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        holder.btdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt ("0"+holder.textQuantity.getText().toString());
                if(quantity >0) {
                    quantity = quantity - 1;
                    holder.textQuantity.setText(quantity+"");
                    CartProduct product = cartList.get(position);
                    product.quantity=(quantity);
                    listener.onProductChange(product);
                }
            }
        });
        holder.btinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartProduct product = cartList.get(position);
                int quantity = Integer.parseInt ("0"+holder.textQuantity.getText().toString());

                    quantity = quantity + 1;
                    holder.textQuantity.setText(quantity+"");

                product.quantity=(quantity);
                listener.onProductChange(product);


            }
        });
    }

    @Override
    public int getItemCount() {
        if(cartList==null) return 0;
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textQuantity;
        ImageView imageView;
        Button btinc, btdec;
        ImageButton btdel;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textProductName);
            textPrice = itemView.findViewById(R.id.textProductPrice);
            imageView =itemView.findViewById(R.id.imageProduct);
            btinc=itemView.findViewById(R.id.btnIncrease);
            btdec=itemView.findViewById(R.id.btnDecrease);
            textQuantity =itemView.findViewById(R.id.textQuantity);
            btdel=itemView.findViewById(R.id.btnRemove);

        }
    }
}
