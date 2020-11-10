package com.example.infs3605team3.experience;

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
import com.example.infs3605team3.model.Experience;
import com.example.infs3605team3.model.Wish;

import java.util.ArrayList;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {
    ArrayList<Experience> mLists;
    Context context;
    public ExperienceAdapter(Context context, ArrayList<Experience> mLists) {
        this.context = context;
        this.mLists = mLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_experience,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Experience item = mLists.get(position);
        RoundedCorners roundedCorners=new RoundedCorners(30);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners);
        requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context).load(item.image).apply(requestOptions).into(holder.iv_logo);
        holder.tvTitle.setText(item.officeId);
        holder.tv_info.setText(mLists.get(position).location);
        holder.tv_time.setText(mLists.get(position).startTime+"-"+mLists.get(position).endTime);
        holder.btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemPreClick(position);
                }
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemDeleteClick(position);
                }
            }
        });

        holder.btn_bookagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemAgainClick(position);
                }
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
        TextView tv_time;
        ImageView iv_logo;
        TextView btn_delete;
        TextView btn_bookagain;
        TextView btn_order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name);
            tv_info = itemView.findViewById(R.id.tv_info);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_logo = itemView.findViewById(R.id.iv_logo);
            btn_order = itemView.findViewById(R.id.btn_order);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_bookagain = itemView.findViewById(R.id.btn_bookagain);
        }
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemAgainClick(int position);
        void onItemDeleteClick(int position);
        void onItemPreClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
