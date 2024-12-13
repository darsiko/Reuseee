package com.example.reuse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.models.Product;

import java.util.List;
public class ProductBroughtAdapter extends RecyclerView.Adapter<ProductBroughtAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    public ProductBroughtAdapter(Context context, List<Product> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prod_brought_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //rimosso holder.productImage.setImageResource(product.getImageResId());
        holder.productName.setText(product.getNome());
        holder.productPrice.setText(""+product.getPrezzo());
        //rimosso holder.userAvatar.setImageResource(product.getUserAvatarResId());
        //rimosso holder.userName.setText(product.getUserName());
        //rimosso holder.userStatus.setText(product.getUserStatus());

        // Set click listeners
        holder.editProduct.setOnClickListener(view ->{
            Toast.makeText(view.getContext(), "Item: " + product.getNome(), Toast.LENGTH_SHORT);
            onItemClickListener.onEditProductClick(product);
            }
        );

        //holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(product));
    }

    public interface OnItemClickListener {
          void onEditProductClick(Product product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, userAvatar, editProduct;
        TextView productName, productPrice, userName, userStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            userAvatar = itemView.findViewById(R.id.sellerImage);
            userName = itemView.findViewById(R.id.sellerName);
            userStatus = itemView.findViewById(R.id.sellerStatus);
            editProduct = itemView.findViewById(R.id.editProduct);
        }
    }
}
