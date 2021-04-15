package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ProgressDetailCommentResponse;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ProgressDetailCommentAppendixAdapter extends RecyclerView.Adapter<ProgressDetailCommentAppendixAdapter.ViewHolder> {
    private Context context;
    private List<ProgressDetailCommentResponse.DataBean.CommentBean.AppendixsBeans> appendixsBeans;

    public ProgressDetailCommentAppendixAdapter(Context context,List<ProgressDetailCommentResponse.DataBean.CommentBean.AppendixsBeans> appendixsBeans){
        this.context = context;
        this.appendixsBeans = appendixsBeans;
    }

    private ProgressDetailCommentAppendixAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String path,int position,View view);
    }

    public void setOnItemClickListener(ProgressDetailCommentAppendixAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public ProgressDetailCommentAppendixAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.progress_detail_comment_appendix_item,null);
        return new ProgressDetailCommentAppendixAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressDetailCommentAppendixAdapter.ViewHolder holder, int position) {
        holder.setData(appendixsBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (appendixsBeans != null){
            return appendixsBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvFileName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvFileName = itemView.findViewById(R.id.progress_detail_comment_appendix_item_tv_fileName);
        }

        public void setData(final ProgressDetailCommentResponse.DataBean.CommentBean.AppendixsBeans appendixsBean, final Context context, final int position){
            mTvFileName.setText(appendixsBean.getFilename());

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(appendixsBean.getPath(),position,itemView);
                }
            });

        }
    }
}
