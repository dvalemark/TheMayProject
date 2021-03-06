package com.bimobject.themayproject.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimobject.themayproject.R;
import com.bimobject.themayproject.dto.Product;
import com.bimobject.themayproject.helpers.OnBottomReachedListener;
import com.bimobject.themayproject.helpers.OnNewRequestLoadListener;
import com.bimobject.themayproject.helpers.OnNewRequestRecievedListener;
import com.bimobject.themayproject.helpers.OnRecycleViewItemClickListener;
import com.bimobject.themayproject.helpers.RVAHelper;
import com.bimobject.themayproject.helpers.Request;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ProductViewHolder> {

    private List<Product> data;
    private OnRecycleViewItemClickListener onRecycleViewItemClickListener;
    private OnBottomReachedListener onBottomReachedListener;
    private OnNewRequestRecievedListener onNewRequestRecievedListener;
    private OnNewRequestLoadListener onNewRequestLoadListener;
    public boolean isLoading;

    private RVAHelper helper;

    public RecycleViewAdapter() {
        data = new ArrayList<>();
        helper = new RVAHelper(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = data.get(position);
        holder.setProductId(product.getId());

        //TODO: Find better solution to find context
        Picasso.with(holder.image.getContext())
                .load(product.getImageUrl()).fit().centerInside()
                .placeholder(R.drawable.progress_animation)
                .into(holder.image);

        Picasso.with(holder.brand_logo.getContext())
                .load(product.getBrand().getImageUrl()).fit().centerInside()
                .placeholder(R.drawable.progress_animation)
                .into(holder.brand_logo);

        holder.product_title.setText(product.getName());
        holder.brand_name.setText(product.getBrand().getName());

        //TODO: Also weird way of getting context here
        //If bottom is reached
        if ((position == data.size() - 1)){
            onBottomReachedListener.onBottomReached(position);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ProductViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        Picasso.with(holder.image.getContext())
                .cancelRequest(holder.image);
        Picasso.with(holder.brand_logo.getContext())
                .cancelRequest(holder.brand_logo);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public RVAHelper getHelper() {
        return helper;
    }

    public void addAll(List<Product> objects){
        data.addAll(objects);
        this.notifyDataSetChanged();
    }

    public void clear(){
        data.clear();
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public void setOnNewRequestRecievedListener(OnNewRequestRecievedListener onNewRequestRecievedListener) {
        this.onNewRequestRecievedListener = onNewRequestRecievedListener;
    }

    public void setOnNewRequestLoadListener(OnNewRequestLoadListener onNewRequestLoadListener) {
        this.onNewRequestLoadListener = onNewRequestLoadListener;
    }

    public OnNewRequestLoadListener getOnNewRequestLoadListener() {
        return onNewRequestLoadListener;
    }

    public void onNewRequest(Request request){
        onNewRequestRecievedListener.onNewRequest(request);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView product_title;
        private TextView brand_name;
        private ImageView image;
        private ImageView brand_logo;

        private String productId;

        private ProductViewHolder(View itemView) {
            super(itemView);
            product_title = itemView.findViewById(R.id.layout_list_item_tv_product_name);
            brand_name = itemView.findViewById(R.id.layout_list_item_tv_product_brand);
            image = itemView.findViewById(R.id.layout_list_item_img_product);
            brand_logo = itemView.findViewById(R.id.layout_list_item_img_product_logo);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRecycleViewItemClickListener.onItemClick(v, this.productId);
        }



        private void setProductId(String productId) {
            this.productId = productId;
        }
    }


}