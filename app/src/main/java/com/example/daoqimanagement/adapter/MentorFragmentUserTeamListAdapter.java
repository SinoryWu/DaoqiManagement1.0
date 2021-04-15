package com.example.daoqimanagement.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.HomeFragmentProgressListResponse;
import com.example.daoqimanagement.bean.MentorsFragmentUserTeamListResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MentorFragmentUserTeamListAdapter extends RecyclerView.Adapter<MentorFragmentUserTeamListAdapter.ViewHolder> {
    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;
    private RecyclerView mRecyclerView;
    private View VIEW_FOOTER;
    private View VIEW_HEADER;
    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    private Context context;
    private List<MentorsFragmentUserTeamListResponse.DataBean> dataBeans;

    public MentorFragmentUserTeamListAdapter(Context context, List<MentorsFragmentUserTeamListResponse.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.mentors_fragment_team_list_item, null);

        if (viewType == TYPE_FOOTER) {
            return new MentorFragmentUserTeamListAdapter.ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new MentorFragmentUserTeamListAdapter.ViewHolder(VIEW_HEADER);
        } else {
            return new MentorFragmentUserTeamListAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            holder.setData(dataBeans.get(position), context,position);
        }
    }

    @Override
    public int getItemCount() {
        int count = (dataBeans == null ? 0 : dataBeans.size());
        if (VIEW_FOOTER != null) {
            if (dataBeans.size() > 0){
                count++;
            }else {
                count = 0;
            }
        }

        if (VIEW_HEADER != null) {
            if (dataBeans.size() > 0){
                count++;
            }else {
                count = 0;
            }
        }
        return count;
        //        return dataBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {

            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTrueName, mTvPosition;
        private RelativeLayout mRlCallPhone;
        private CircleImageView mIvUserIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTrueName = itemView.findViewById(R.id.mentors_fragment_team_list_tv_user_true_name);
            mTvPosition = itemView.findViewById(R.id.mentors_fragment_team_list_tv_user_position);
            mRlCallPhone = itemView.findViewById(R.id.mentors_fragment_team_list_rl_call_phone);
            mIvUserIcon = itemView.findViewById(R.id.mentors_fragment_team_list_iv_user_list_icon);

        }

        public void setData(final MentorsFragmentUserTeamListResponse.DataBean dataBean, final Context context, final int position) {

            mTvTrueName.setText(dataBean.getTruename());
            mTvPosition.setText(dataBean.getPosition());
            Glide.with(context).load("sadasd")
                    .error(R.mipmap.team_list_user_icon)//异常时候显示的图片
                    .fallback(R.mipmap.team_list_user_icon)//url为空的时候,显示的图片
                    .placeholder(R.mipmap.team_list_user_icon)//加载成功前显示的图片
                    .into(mIvUserIcon);
            mRlCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mOnItemClickListener.onItemClick(view,dataBean.getMobile(),position);
                }
            });

        }

    }


    private View getLayout(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);


        }
    }

    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, String phoneNumber, int position);
        void onItemLongClick(View v);
    }


    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }



}
