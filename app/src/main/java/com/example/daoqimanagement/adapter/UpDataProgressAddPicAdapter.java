package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class UpDataProgressAddPicAdapter extends RecyclerView.Adapter<UpDataProgressAddPicAdapter.ViewHolder> {
    private Context context;
    List<Bitmap> list;


    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    //设置回调接口

    private OnItemClickItemListener mItemClickListener;
    public interface OnItemClickItemListener {


        void onItemClickItem(int position);
    }

    public void setOnItemClickItemListener(OnItemClickItemListener listener){
        this.mItemClickListener = listener;
    }


    private OnClickItemListener mClickListener;
    public interface OnClickItemListener {


        void onItemClickItem(int position,Bitmap bitmap);
    }

    public void setOnItemClickListener(OnClickItemListener listener){
        this.mClickListener = listener;
    }
    public UpDataProgressAddPicAdapter(Context context, List<Bitmap> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public UpDataProgressAddPicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.updata_progress_add_pic_item,null);


        return new UpDataProgressAddPicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpDataProgressAddPicAdapter.ViewHolder holder, final int position) {
        holder.mIvDeletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClickItem(position);
            }
        });

        holder.mIvAddPic.setImageBitmap(list.get(position));
//        if (isAndroidQ){
//
//        }else {
//            holder.mIvAddPic.setImageBitmap(BitmapFactory.decodeFile(list.get(position)));
//        }

        holder.itemView.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
               mClickListener.onItemClickItem(position,list.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list.size() > 0){
            return list.size();
        }
        return 0;
    }


    // 添加数据
    public void addData(int position,Bitmap bitmap) {
//   在list中添加数据，并通知条目加入一条
        list.add(position, bitmap);

        //添加动画
        notifyItemInserted(position);
    }
    // 删除数据
    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mIvDeletePic,mIvAddPic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mIvDeletePic = itemView.findViewById(R.id.update_progress_iv_delete_pic);
            mIvAddPic = itemView.findViewById(R.id.update_progress_iv_add_pic);

        }


    }



}
