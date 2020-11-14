package com.example.infs3605team3.wishlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.infs3605team3.R;
import com.example.infs3605team3.model.Booking;
import com.example.infs3605team3.model.Wish;

import java.util.ArrayList;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
    ArrayList<Wish> mLists;
    Context context;
    public WishAdapter(Context context, ArrayList<Wish> mLists) {
        this.context = context;
        this.mLists = mLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_wish,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Wish item = mLists.get(position);
        RoundedCorners roundedCorners=new RoundedCorners(30);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners);
        requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context).load(item.image).apply(requestOptions).into(holder.iv_logo);

        holder.tvTitle.setText(item.officeId);
        holder.tv_price.setText(mLists.get(position).price+" $");
        holder.tv_info.setText(mLists.get(position).location);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemLongClick(position);
                }
                return true;
            }


        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tv_info;
        TextView tv_price;
        ImageView iv_logo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name);
            tv_info = itemView.findViewById(R.id.tv_info);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv_logo = itemView.findViewById(R.id.iv_logo);
        }
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
