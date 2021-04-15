package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ApprovalLogResponse;

import java.util.List;

public class ApprovalLogAdapter extends RecyclerView.Adapter<ApprovalLogAdapter.ViewHolder> {
    private Context context;
    private List<ApprovalLogResponse.DataBean> dataBeans;

    public ApprovalLogAdapter(Context context,List<ApprovalLogResponse.DataBean> dataBeans){
        this.context = context;
        this.dataBeans = dataBeans;
    }
    @NonNull
    @Override
    public ApprovalLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.approval_log_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalLogAdapter.ViewHolder holder, int position) {
        holder.setData(dataBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (dataBeans != null){
            return dataBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View headView;
        private ImageView mIvStatus;
        private TextView mTvStatus,mTvName,mTvOpinion,mTvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headView = itemView.findViewById(R.id.approval_log_item_head_view);
            mIvStatus = itemView.findViewById(R.id.approval_log_item_iv_status);
            mTvStatus = itemView.findViewById(R.id.approval_log_item_tv_status);
            mTvName = itemView.findViewById(R.id.approval_log_item_tv_name);
            mTvOpinion = itemView.findViewById(R.id.approval_log_item_tv_opinion);
            mTvTime = itemView.findViewById(R.id.approval_log_item_tv_time);

        }

        public void setData(final ApprovalLogResponse.DataBean dataBean,Context context ,int position){

            if (position == 0){
                headView.setVisibility(View.GONE);
            }else {
                headView.setVisibility(View.VISIBLE);
            }

            if (dataBean.getStatus() == 0){
                mTvStatus.setText("提交");
                mTvStatus.setTextColor(Color.parseColor("#6CC291"));
                mIvStatus.setImageResource(R.mipmap.approval_log_pass_icon);
            }else if (dataBean.getStatus() == 1){
                mTvStatus.setText("待审核");
                mTvStatus.setTextColor(Color.parseColor("#5EB1FC"));
                mIvStatus.setImageResource(R.mipmap.approval_log_wait_icon);
            }else if (dataBean.getStatus() == 2){
                mTvStatus.setText("通过");
                mTvStatus.setTextColor(Color.parseColor("#6CC291"));
                mIvStatus.setImageResource(R.mipmap.approval_log_pass_icon);
            }else if (dataBean.getStatus() == 3){
                mTvStatus.setText("不通过");
                mTvStatus.setTextColor(Color.parseColor("#F45C50"));
                mIvStatus.setImageResource(R.mipmap.approval_log_no_pass_icon);
            }

            if (dataBean.getUserType() == 1){
                mTvName.setText(dataBean.getTruename()+" "+"星火合伙人");
            }else if (dataBean.getUserType() == 2){
                mTvName.setText(dataBean.getTruename()+" "+"直营团队");
            }else if (dataBean.getUserType() == 3){
                mTvName.setText(dataBean.getTruename()+" "+"生态链合伙人");
            }else if (dataBean.getUserType() == 4){
                mTvName.setText(dataBean.getTruename()+" "+"财务");
            }else if (dataBean.getUserType() == 5){
                mTvName.setText(dataBean.getTruename()+" "+"初审");
            }else if (dataBean.getUserType() == 6){
                mTvName.setText(dataBean.getTruename()+" "+"复审");
            }else if (dataBean.getUserType() == 7){
                mTvName.setText(dataBean.getTruename()+" "+"终审");
            }else if (dataBean.getUserType() == 8){
                mTvName.setText(dataBean.getTruename()+" "+"运维");
            }

            if (!TextUtils.isEmpty(dataBean.getOpinion())){
                mTvOpinion.setText(dataBean.getOpinion());
            }

            mTvTime.setText(dataBean.getUpdatedAt());

        }
    }
}
