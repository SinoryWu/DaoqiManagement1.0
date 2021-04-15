package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.AreaListResponse;

import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.ViewHolder> {


    private Context context;
    private List<AreaListResponse.DataBean> dataBeans;
    private OnItemClickListener   mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int areaId,  String areaName,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    public AreaListAdapter(Context context, List<AreaListResponse.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.add_prepare_choice_recycler_item,null);


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
        private TextView mTvAreaName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAreaName = itemView.findViewById(R.id.product_list_productName);

        }

        public void setData(final AreaListResponse.DataBean dataBean, Context context, final int position){
            mTvAreaName.setText(dataBean.getAreaName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(dataBean.getAreaId(),dataBean.getAreaName(),position);
                }
            });
        }

    }




}
