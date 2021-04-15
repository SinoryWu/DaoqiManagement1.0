package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ProductListResponse;
import com.example.daoqimanagement.bean.SearchHospitalListResponse;

import java.util.List;

public class SearchHospitalListAdapter extends RecyclerView.Adapter<SearchHospitalListAdapter.ViewHolder> {


    private Context context;
    private List<SearchHospitalListResponse.DataBeanX.DataBean> dataBeans;
    private OnItemClickListener   mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String hospitalName,  String detail,String level,String nature,String areaName,int status,String headPic, int  hospitalid,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    public SearchHospitalListAdapter(Context context, List<SearchHospitalListResponse.DataBeanX.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.search_hospital_list_item,null);


            return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setData(dataBeans.get(position),context,position);


    }

    @Override
    public int getItemCount() {

           if (dataBeans != null){
               return dataBeans.size();
           }
            return 0;



        //        return dataBeans.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvHospitalName,mTvHospitalStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvHospitalName = itemView.findViewById(R.id.search_hospital_item_tv_hospital_name);
            mTvHospitalStatus = itemView.findViewById(R.id.search_hospital_item_tv_hospital_status);

        }

        public void setData(final SearchHospitalListResponse.DataBeanX.DataBean dataBean, Context context, final int position){
            mTvHospitalName.setText(dataBean.getHospitalName());
            if (dataBean.getStatus() == 1){
                mTvHospitalStatus.setText("");
            }else if (dataBean.getStatus() == 2){
                mTvHospitalStatus.setText("已锁定");
            }else if (dataBean.getStatus() == 3){
                mTvHospitalStatus.setText("暂未开放");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(dataBean.getHospitalName(),dataBean.getDetail(),dataBean.getLevel(),dataBean.getNature(),dataBean.getAreaName(),dataBean.getStatus(),dataBean.getHeadPic(),dataBean.getHospitalid(),position);
                }
            });


        }

    }




}
