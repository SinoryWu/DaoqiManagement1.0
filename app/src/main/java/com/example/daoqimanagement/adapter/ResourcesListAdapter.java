package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ResourceListResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ResourcesListAdapter extends RecyclerView.Adapter<ResourcesListAdapter.ViewHolder> {

    private List<ResourceListResponse.DataBeanX.DataBean> dataBeans;
    private Context context;

    private ResourcesListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String url,int type,int titleId);
    }

    public void setOnItemClickListener(ResourcesListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    public ResourcesListAdapter(Context context,List<ResourceListResponse.DataBeanX.DataBean> dataBeans){
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ResourcesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view  = View.inflate(parent.getContext(), R.layout.resource_list_item,null);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesListAdapter.ViewHolder holder, int position) {
        holder.setData(dataBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (dataBeans.size()>0){
            return dataBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTime,mTvTitle,mTvDesc,mTvReadNum;
        String updateTime = "";
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTime= itemView.findViewById(R.id.resource_item_time);
            mTvTitle= itemView.findViewById(R.id.resource_item_title);
            mTvDesc= itemView.findViewById(R.id.resource_item_desc);
            mTvReadNum= itemView.findViewById(R.id.resource_item_readNum);
        }
        public void setData(final ResourceListResponse.DataBeanX.DataBean dataBean ,Context context,int position){


            if (dataBean.getUpdatedAt() == null){
                mTvTime.setText("");
            }else {
                String createAt1 = dataBean.getUpdatedAt();
                String createAt= createAt1.substring(0,16);
                StringBuffer buffer = new StringBuffer(createAt);
                buffer.replace(4,5,"年");
                buffer.replace(7,8,"月");
                updateTime= String.valueOf(buffer);
                mTvTime.setText(buffer);

            }

            mTvTitle.setText(dataBean.getTitle());
            mTvReadNum.setText("阅读 "+dataBean.getReadNum());
            mTvDesc.setText(dataBean.getDesc());

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getUrl(),dataBean.getType(),dataBean.getTitleId());
                }
            });

        }


    }
}
