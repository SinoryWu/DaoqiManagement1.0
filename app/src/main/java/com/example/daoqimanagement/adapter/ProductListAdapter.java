package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ProductListResponse;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {


    private Context context;
    private List<ProductListResponse.DataBean> dataBeans;
    private OnItemClickListener   mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String productName ,int productId, String introduce,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    public ProductListAdapter(Context context, List<ProductListResponse.DataBean> dataBeans) {
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
        private TextView mTvProductName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvProductName = itemView.findViewById(R.id.product_list_productName);

        }

        public void setData(final ProductListResponse.DataBean dataBean, Context context, final int position){
            mTvProductName.setText(dataBean.getProductName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(dataBean.getProductName(),dataBean.getProductid(),dataBean.getIntroduce(),position);
                }
            });
        }

    }




}
