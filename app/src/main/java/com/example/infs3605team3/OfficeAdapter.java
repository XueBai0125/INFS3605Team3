package com.example.infs3605team3;

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
import com.example.infs3605team3.model.Office;

import java.util.ArrayList;

public class OfficeAdapter extends RecyclerView.Adapter<OfficeAdapter.ViewHolder> {
    ArrayList<Office> mLists;
    Context context;
    public OfficeAdapter(Context context, ArrayList<Office> mLists) {
        this.context = context;
        this.mLists = mLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_alloffice,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Office item = mLists.get(position);
        RoundedCorners roundedCorners=new RoundedCorners(30);
        RequestOptions requestOptions =    RequestOptions.bitmapTransform(roundedCorners);
        requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context).load(item.img).apply(requestOptions).into(holder.iv_logo);

        holder.tv_name.setText(item.name);
        holder.tv_des.setText(item.des);
        holder.tv_info.setText(item.country+item.state+item.surburb+item.street);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_logo;
        TextView tv_name;
        TextView tv_info;
        TextView tv_des;
        TextView btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_logo = itemView.findViewById(R.id.iv_logo);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_info = itemView.findViewById(R.id.tv_info);
            tv_des = itemView.findViewById(R.id.tv_des);
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
