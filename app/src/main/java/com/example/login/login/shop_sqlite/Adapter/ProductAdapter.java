package com.example.login.login.shop_sqlite.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.login.shop_sqlite.Activity.EditProductActivity;
import com.example.login.login.shop_sqlite.Activity.ProductDetailActivity;
import com.example.login.login.shop_sqlite.Entity.Product;
import com.example.login.login.shop_sqlite.R;
import com.example.login.login.shop_sqlite.ViewModel.CartItemViewModel;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private CartItemViewModel cartItemViewModel;
    onCartChange listener;

    OnProductChangeListener productChangeListener;
    public interface onCartChange {
        public void addToCard(Product product);
    }

    public interface OnProductChangeListener {
        void onProductChange(Product product);
        void onDeleteProduct(Product product);
    }
    public void setProductList(List<Product> list) {
        this.productList = list;
        notifyDataSetChanged();
    }

    public ProductAdapter(onCartChange listener, OnProductChangeListener productChangeListener){
       // this.cartItemViewModel=cartItemViewModel;
        this.listener = listener;
        this.productChangeListener = productChangeListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.price*1000);
        holder.tvId.setText("ID: " + product.id);
        holder.tvName.setText(product.name);
        holder.tvPrice.setText("Giá: " + formattedPrice + " đ");
       // Dùng Glide để load ảnh từ URL hoặc đường dẫn file
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Glide.with(holder.itemView.getContext())
                    .load(product.image)
                    .placeholder(R.drawable.ic_launcher_background) // ảnh mặc định khi loading
                    .error(R.drawable.ic_err_image_layy) // ảnh khi lỗi
                    .into(holder.imgProduct);
       // }
        // Xử lys khi user click addTocart
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc chắn muốn xoá sản phẩm này không?")
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productChangeListener.onDeleteProduct(productList.get(holder.getAdapterPosition()));
                                productList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }
        });
        holder.btViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                // Tạo Intent để mở ProductDetailActivity
                Intent intent = new Intent(context, ProductDetailActivity.class);

                // Gửi thêm dữ liệu sản phẩm (ví dụ: product ID)
                intent.putExtra("product_id", product.id);
                context.startActivity(intent);

            }
        });
        holder.btnCart.setOnClickListener((v)->{
            this.listener.addToCard(product);
        });
        holder.btnEdit.setOnClickListener((v)->{
            Context context = v.getContext();

            // Tạo Intent để mở ProductDetailActivity
            Intent intent = new Intent(context, EditProductActivity.class);

            // Gửi thêm dữ liệu sản phẩm (ví dụ: product ID)
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvPrice;//, tvDescription;
        ImageView imgProduct;
        ImageButton btViewDetail, btnDelete, btnEdit, btnCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_product_id);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
           // tvDescription = itemView.findViewById(R.id.tv_product_description);
            imgProduct = itemView.findViewById(R.id.img_product);
            btnDelete=itemView.findViewById(R.id.btn_delete);
            btViewDetail=itemView.findViewById(R.id.btn_view_detail);
            btnCart=itemView.findViewById(R.id.btn_cart);
            btnEdit=itemView.findViewById(R.id.btn_edit);
        }
    }
}
