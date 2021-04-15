package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.HospitalDetailResponse;

import java.util.List;

public class HospitalDetailCooperateAdapter extends RecyclerView.Adapter<HospitalDetailCooperateAdapter.ViewHolder> {
    private Context context;
    private List<HospitalDetailResponse.DataBean.CooperateBean>  cooperateBeans;

    public HospitalDetailCooperateAdapter(Context context,List<HospitalDetailResponse.DataBean.CooperateBean>  cooperateBeans){
        this.context = context;
        this.cooperateBeans= cooperateBeans;
    }

    @NonNull
    @Override
    public HospitalDetailCooperateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.hospital_detali_cooperate_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalDetailCooperateAdapter.ViewHolder holder, int position) {
        holder.setData(cooperateBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (cooperateBeans != null){
            return cooperateBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvContent = itemView.findViewById(R.id.cooperate_item_tv_content);
        }

        public void setData(HospitalDetailResponse.DataBean.CooperateBean cooperateBean , Context context,int position){
            if (cooperateBean.getStatus() == 0 || cooperateBean.getStatus() == 10 || cooperateBean.getStatus() == 90){
                mTvContent.setText(cooperateBean.getProductName()+"   "+"已锁定   "+cooperateBean.getTruename());
            }else if (cooperateBean.getStatus() == 1){
                mTvContent.setText(cooperateBean.getProductName()+"   "+"预报备   "+cooperateBean.getTruename());
            }else if (cooperateBean.getStatus() == 20 || cooperateBean.getStatus() == 30 || cooperateBean.getStatus() == 40 || cooperateBean.getStatus() == 50 || cooperateBean.getStatus() == 60 || cooperateBean.getStatus() == 80 ){
                mTvContent.setText(cooperateBean.getProductName()+"   "+"暂未开放   "+cooperateBean.getTruename());
            }else if (cooperateBean.getStatus() == 95){
                mTvContent.setText(cooperateBean.getProductName()+"   "+"已完成   "+cooperateBean.getTruename());
            }
        }
    }
}
