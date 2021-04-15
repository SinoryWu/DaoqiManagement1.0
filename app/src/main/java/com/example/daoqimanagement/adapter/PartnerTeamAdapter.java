package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.PartnerDetailActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.PartnerTeamResponse;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartnerTeamAdapter extends RecyclerView.Adapter<PartnerTeamAdapter.ViewHolder> {
    private Context context;
    private List<PartnerTeamResponse.DataBean> dataBeans;

    public PartnerTeamAdapter(Context context, List<PartnerTeamResponse.DataBean> dataBeans){
        this.context = context;
        this.dataBeans=dataBeans;
    }

    private PartnerTeamAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int uid);
    }

    public void setOnItemClickListener(PartnerTeamAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public PartnerTeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = View.inflate(parent.getContext(), R.layout.partner_team_list_item,null);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerTeamAdapter.ViewHolder holder, int position) {
        holder.setData(dataBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (dataBeans !=null){
            return dataBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mIvHeadPic;
        private TextView mTvName,mTvPosition;
        private RelativeLayout mRlDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvHeadPic= itemView.findViewById(R.id.partner_team_list_item_iv_headPic);
            mTvName= itemView.findViewById(R.id.partner_team_list_item_tv_truename);
            mTvPosition= itemView.findViewById(R.id.partner_team_list_item_tv_position);
            mRlDelete= itemView.findViewById(R.id.partner_team_list_item_rl_delete);
        }

        public void setData(final PartnerTeamResponse.DataBean dataBean,Context context,int position){
            /**
             * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
             */
            Glide.with(context).load(Api.URL+dataBean.getHeadPic())
                    .error(R.mipmap.team_list_user_icon)//异常时候显示的图片
                    .fallback(R.mipmap.team_list_user_icon)//url为空的时候,显示的图片
                    .placeholder(R.mipmap.team_list_user_icon)//加载成功前显示的图片
                    .into(mIvHeadPic);
            mTvName.setText(dataBean.getTruename());
            mTvPosition.setText(dataBean.getPosition());


            mRlDelete.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getUid());
                }
            });

        }
    }
}
