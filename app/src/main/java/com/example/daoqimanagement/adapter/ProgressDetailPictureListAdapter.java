package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ProductListResponse;
import com.example.daoqimanagement.bean.ProgressDetailResponse;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ProgressDetailPictureListAdapter extends RecyclerView.Adapter<ProgressDetailPictureListAdapter.ViewHolder> {
    private Context context;

    private List<ProgressDetailResponse.DataBean.PicturesBean> picturesBeans;

    private ProgressDetailPictureListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String path,int position);
    }

    public void setOnItemClickListener(ProgressDetailPictureListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    public ProgressDetailPictureListAdapter(Context context, List<ProgressDetailResponse.DataBean.PicturesBean> picturesBeans) {
        this.context = context;
        this.picturesBeans = picturesBeans;
    }
    @NonNull
    @Override
    public ProgressDetailPictureListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.progress_detail_picture_list_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressDetailPictureListAdapter.ViewHolder holder, int position) {
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
        private ImageView mIvPicture;
        float scale;
        Bitmap bitmap;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvPicture= itemView.findViewById(R.id.progress_detail_item_iv_pic);
        }

        public void setData(final ProgressDetailResponse.DataBean.PicturesBean picturesBean, final Context context, final int position){
            Glide.with(context).asBitmap()
                    .load(Api.URL+picturesBean.getPath())
                    .error(R.drawable.progress_detail_picture_background)//异常时候显示的图片
                    .fallback(R.drawable.progress_detail_picture_background)//url为空的时候,显示的图片
                    .placeholder(R.drawable.progress_detail_picture_background)//加载成功前显示的图片
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            mIvPicture.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .skipMemoryCache(true).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    float width = resource.getWidth();
                    float height=  resource.getHeight();
                    scale = height/width;
                    int scaledW  = dip2px(context,346);
                    int scaledH  = (int) (scaledW*scale);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(scaledW,scaledH);
                    lp.gravity = Gravity.CENTER_HORIZONTAL;
                    mIvPicture.setLayoutParams(lp);
                    mIvPicture.setImageBitmap(resource);



                }
            });

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(picturesBean.getPath(),position);
                }
            });
        }
    }


    /**
     * dip转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
