package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.example.daoqimanagement.bean.ProgressDetailCommentResponse2;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ProgressDetailCommentPictureAdapter extends RecyclerView.Adapter<ProgressDetailCommentPictureAdapter.ViewHolder> {
    private Context context;
    private List<ProgressDetailCommentResponse.DataBean.CommentBean.PicturesBeans>  picturesBeans;

    public ProgressDetailCommentPictureAdapter(Context context,List<ProgressDetailCommentResponse.DataBean.CommentBean.PicturesBeans>  picturesBeans){
        this.context = context;
        this.picturesBeans = picturesBeans;
    }

    private ProgressDetailCommentPictureAdapter.OnItemClickListener mClickListener;


    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String path,int position);
    }

    public void setOnItemClickListener(ProgressDetailCommentPictureAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public ProgressDetailCommentPictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.progress_detail_comment_picture_item,null);
        return new ProgressDetailCommentPictureAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressDetailCommentPictureAdapter.ViewHolder holder, int position) {
        holder.setData(picturesBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (picturesBeans != null){
            return picturesBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mIVPicture;
        private RelativeLayout mRlPictureFailed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIVPicture= itemView.findViewById(R.id.progress_detail_picture_item_iv_pic);
            mRlPictureFailed= itemView.findViewById(R.id.progress_detail_picture_item_rl_pic_failed);
        }

        public void setData(final ProgressDetailCommentResponse.DataBean.CommentBean.PicturesBeans picturesBeans, final Context context, final int position){
            mRlPictureFailed.setVisibility(View.GONE);

            /**
             * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
             */
            Glide.with(context).load(Api.URL+picturesBeans.getPath())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mRlPictureFailed.setVisibility(View.VISIBLE);
                            mIVPicture.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })

                    .into(mIVPicture);

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(picturesBeans.getPath(),position);
                }
            });
        }
    }
}
