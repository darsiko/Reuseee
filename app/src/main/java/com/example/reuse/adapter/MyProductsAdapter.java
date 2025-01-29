package com.example.reuse.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ProductViewHolder> {
    private StorageReference storageRef;
    private Context context;
    private List<Product> productList = new ArrayList<>();
    private MyProductsAdapter.OnItemClickListener onItemClickListener;
    public MyProductsAdapter(Context context, List<Product> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList.addAll(productList);
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public MyProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prod_my_products, parent, false);
        return new MyProductsAdapter.ProductViewHolder(view);
    }
    // Define the custom interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
    @Override
    public void onBindViewHolder(@NonNull MyProductsAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        String sellerId = product.getIdVenditore();
        new User(sellerId, new User.UserCallback() {
            List<String> idProd = new ArrayList<>();
            @Override
            public void onUserLoaded(User user) {
                idProd = user.getProductsForSale();
                holder.userName.setText(user.getUsername());
                String profilePictureUrl = user.getImageUrl();
                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                    Glide.with(context)
                            .load(profilePictureUrl)// Error image in case of failure
                            .into(holder.userAvatar);
                }
                else {
                    // Set a default image if profilePictureUrl is unavailable
                    holder.userAvatar.setImageResource(R.drawable.user);
                }
            }
            @Override
            public void onError(Exception e) {}
        });

        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(product.getImageUrl());

        storageRef.getDownloadUrl().addOnCompleteListener(uri -> {
            String imageUrl = uri.toString(); // This is the HTTP URL
            //rimosso il println di debug
            Glide.with(context).load(imageUrl).into(holder.productImage);
        });
        holder.productName.setText(product.getNome());
        if(product.getPrezzo()>=0) holder.productPrice.setText(String.format("$%.2f", product.getPrezzo()));
        else holder.productPrice.setText("Price Unavailable");
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(product));
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, userAvatar;
        TextView productName, productPrice, userName;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            userAvatar = itemView.findViewById(R.id.sellerImage);
            userName = itemView.findViewById(R.id.sellerName);
        }
    }
}