package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.UpDateProgressActivity;
import com.example.daoqimanagement.bean.PartnerPrepareListResponse;
import com.example.daoqimanagement.utils.Api;

import java.util.List;

public class PartnerPrepareAdapter extends RecyclerView.Adapter<PartnerPrepareAdapter.ViewHolder> {
    private Context context;
    private List<PartnerPrepareListResponse.DataBeanX.DataBean> dataBeans;

    public PartnerPrepareAdapter( Context context,List<PartnerPrepareListResponse.DataBeanX.DataBean> dataBeans){
        this.context = context;
        this.dataBeans = dataBeans;
    }

    private PartnerPrepareAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick( int prepareId,int position);
    }

    public void setOnItemClickListener(PartnerPrepareAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public PartnerPrepareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = View.inflate(parent.getContext(), R.layout.partner_prepare_list_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerPrepareAdapter.ViewHolder holder, int position) {
        holder.setData(dataBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (dataBeans != null){
            return dataBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvHospitalName,mTvProtectTime,mTvScheduleNum;
        private ImageView mIvHeadPic;
        private CardView mCvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvHospitalName= itemView.findViewById(R.id.partner_prepare_item_tv_hospitalName);
            mTvProtectTime= itemView.findViewById(R.id.partner_prepare_item_tv_protectTime);
            mTvScheduleNum= itemView.findViewById(R.id.partner_prepare_item_tv_scheduleNum);
            mIvHeadPic= itemView.findViewById(R.id.partner_prepare_item_iv_headPic);
            mCvItem= itemView.findViewById(R.id.partner_prepare_item_cv_item);

        }

        public void setData(final PartnerPrepareListResponse.DataBeanX.DataBean dataBean, Context context, final int position){
            mTvHospitalName.setText(dataBean.getHospitalName());
            if (dataBean.getProtectTime() != null){
                String createAt= dataBean.getProtectTime().substring(0,10);
                StringBuffer buffer = new StringBuffer(createAt);
                buffer.replace(4,5,"-");
                buffer.replace(7,8,"-");
                mTvProtectTime.setText("保护期截止至："+buffer);
            }else {
                mTvProtectTime.setText("保护期截止至：");
            }
            mTvScheduleNum.setText("上报进度："+dataBean.getScheduleNum());

            /**
             * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
             */
            Glide.with(context).load(Api.URL+dataBean.getHospitalPic())
                    .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                    .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                    .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                    .into(mIvHeadPic);

            mCvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(dataBean.getPrepareId(),position);
                }
            });

        }
    }
}
