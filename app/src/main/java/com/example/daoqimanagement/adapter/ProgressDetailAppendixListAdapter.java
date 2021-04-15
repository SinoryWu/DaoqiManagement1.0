package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ProgressDetailResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ProgressDetailAppendixListAdapter extends RecyclerView.Adapter<ProgressDetailAppendixListAdapter.ViewHolder> {

    private Context context;

    private List<ProgressDetailResponse.DataBean.AppendixsBean> appendixsBeans;

    private ProgressDetailAppendixListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(String path,int position,View view);
    }

    public void setOnItemClickListener(ProgressDetailAppendixListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    public ProgressDetailAppendixListAdapter(Context context, List<ProgressDetailResponse.DataBean.AppendixsBean> appendixsBeans) {
        this.context = context;
        this.appendixsBeans = appendixsBeans;
    }


    @NonNull
    @Override
    public ProgressDetailAppendixListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.progress_detail_appendix_list_item,null);
        return new ProgressDetailAppendixListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressDetailAppendixListAdapter.ViewHolder holder, int position) {
        holder.setData(appendixsBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (appendixsBeans != null){
            return appendixsBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mTvName = itemView.findViewById(R.id.progress_detail_appendix_list_name);
        }

        public void setData(final ProgressDetailResponse.DataBean.AppendixsBean appendixsBean, final Context context, final int position){

            mTvName.setText(appendixsBean.getFilename());

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(appendixsBean.getPath(),position,itemView);
                }
            });
        }
    }

}
