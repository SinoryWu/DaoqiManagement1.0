package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.HospitalDetailContactResponse;

import java.util.List;

public class HospitalDetailContactAdapter extends RecyclerView.Adapter<HospitalDetailContactAdapter.ViewHolder> {
    private Context context;
    private List<HospitalDetailContactResponse.DataBean.ContactBean> contactBeans;

    public HospitalDetailContactAdapter(Context context,List<HospitalDetailContactResponse.DataBean.ContactBean> contactBeans){
        this.context =context;
        this.contactBeans = contactBeans;
    }

    //设置回调接口

    private HospitalDetailContactAdapter.OnItemClickItemListener mItemClickListener;
    public interface OnItemClickItemListener {


        void onItemClickItem(String mobile);
    }

    public void setOnItemClickItemListener(HospitalDetailContactAdapter.OnItemClickItemListener listener){
        this.mItemClickListener = listener;
    }



    @NonNull
    @Override
    public HospitalDetailContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.hospital_detail_contact_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalDetailContactAdapter.ViewHolder holder, int position) {
        holder.setData(contactBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (contactBeans != null){
            return contactBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName,mTvMobile,mTvDepartment,mTvPosition;
        private RelativeLayout mRlCallPhone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.contact_item_tv_name);
            mTvMobile = itemView.findViewById(R.id.contact_item_tv_mobile);
            mTvDepartment = itemView.findViewById(R.id.contact_item_tv_department);
            mTvPosition = itemView.findViewById(R.id.contact_item_tv_position);
            mRlCallPhone = itemView.findViewById(R.id.contact_item_rl_callPhone);
        }

        public void setData(final HospitalDetailContactResponse.DataBean.ContactBean contactBean, Context context, int position){
            mTvName.setText(contactBean.getName());
            mTvMobile.setText(contactBean.getMobile());
            mTvDepartment.setText(contactBean.getDepartment());
            mTvPosition.setText(contactBean.getPosition());

            mRlCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClickItem(contactBean.getMobile());
                }
            });

        }
    }
}
