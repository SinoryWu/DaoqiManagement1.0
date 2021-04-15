package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ResourceDetailResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import org.w3c.dom.Text;

import java.util.List;

public class ResourceAppendixAdapter extends RecyclerView.Adapter<ResourceAppendixAdapter.ViewHolder> {
    private Context context;
    private List<ResourceDetailResponse.DataBean.AppendixBean> appendixBeans;

    private ResourceAppendixAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String path,View itemView);
    }

    public void setOnItemClickListener(ResourceAppendixAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    public ResourceAppendixAdapter(Context context,List<ResourceDetailResponse.DataBean.AppendixBean> appendixBeans){
        this.context =context;
        this.appendixBeans = appendixBeans;
    }
    @NonNull
    @Override
    public ResourceAppendixAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.resource_detail_appendix_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResourceAppendixAdapter.ViewHolder holder, final int position) {
       holder.setDate(appendixBeans.get(position),context);

    }

    @Override
    public int getItemCount() {
        if (appendixBeans.size()>0){
            return appendixBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvFileName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvFileName= itemView.findViewById(R.id.resource_detail_appendix_item_tv_fileName);
        }
        public void setDate(final ResourceDetailResponse.DataBean.AppendixBean appendixBean,Context context){
            mTvFileName.setText(appendixBean.getFilename());

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(appendixBean.getPath(),itemView);
                }
            });
        }


    }
}
