package com.example.reuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    // Constructor with custom OnItemClickListener
    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // Get the product from the list at the given position
        Product product = productList.get(position);

        // Bind the product data to the UI elements in the ViewHolder
        holder.productName.setText(product.getNome());
        //rimosso holder.productPrice.setText(product.getPrezzo());
        //rimosso holder.productImage.setImageResource(product.getImageResId());
        //rimosso holder.userAvatar.setImageResource(product.getUserAvatarResId());
        //rimosso holder.userName.setText(product.getUserName());
        //rimosso holder.userStatus.setText(product.getUserStatus());

        // Set the click listener for the item view
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Define the custom interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        // Declare the views inside each item
        ImageView productImage, userAvatar;
        TextView productName, productPrice, userName, userStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            userStatus = itemView.findViewById(R.id.user_status);
        }
    }
}
