package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.HomeFragmentHospitalPrepareListResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class SearPrepareListAdapter extends RecyclerView.Adapter<SearPrepareListAdapter.ViewHolder> {

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;
    private int TYPE_FOOTER2 = 0;
    private RecyclerView mRecyclerView;
    private View VIEW_FOOTER;
    private View VIEW_HEADER;


    private Context context;
    private List<HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean> dataBeans;

    public SearPrepareListAdapter(Context context, List<HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }
    private SearPrepareListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int prepareid, String hospitalname,int statu,String hospitalheadpic,int positions);
    }

    public void setOnItemClickListener(SearPrepareListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.search_prepare_list_item,null);
        if (viewType == TYPE_FOOTER) {
            return new SearPrepareListAdapter.ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new SearPrepareListAdapter.ViewHolder(VIEW_HEADER);
        }else {
            return new SearPrepareListAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            holder.setData(dataBeans.get(position),context,position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position) && TYPE_FOOTER2==1) {

            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        int count = (dataBeans == null ? 0 : dataBeans.size());
        if (VIEW_FOOTER != null) {
            if (dataBeans.size() > 0){
                count++;
            }else {
                count = 0;
            }
        }

        if (VIEW_HEADER != null) {
            if (dataBeans.size() > 0){
                count++;
            }else {
                count = 0;
            }
        }
        return count;
        //        return dataBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvHospitalName,mTvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvHospitalName = itemView.findViewById(R.id.search_prepare_list_item_tv_hospital_name);
            mTvStatus = itemView.findViewById(R.id.search_prepare_list_item_tv_hospital_status);

        }

        public void setData(final HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean dataBean, Context context, final int position){


            mTvHospitalName.setText(dataBean.getHospitalName());
            if ( dataBean.getStatus() == 30 || dataBean.getStatus() == 40 || dataBean.getStatus() == 50){
                mTvStatus.setText("审核中");
            }else if (dataBean.getStatus() == 0){
                mTvStatus.setText("已锁定");
            }else if (dataBean.getStatus() == 1){
                mTvStatus.setText("预报备");
            }else if (dataBean.getStatus() == 10){
                mTvStatus.setText("待上传支付凭证");
            }else if (dataBean.getStatus() == 20){
                mTvStatus.setText("待财务审核");
            }else if (dataBean.getStatus() == 60){
                mTvStatus.setText("待退款");
            }else if (dataBean.getStatus() == 70){
                mTvStatus.setText("已退款");
            }else if (dataBean.getStatus() == 80){
                mTvStatus.setText("已取消");
            }else if (dataBean.getStatus() == 90){
                mTvStatus.setText("报备已完成");
            }else if (dataBean.getStatus() == 95){
                mTvStatus.setText("开发完成");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(dataBean.getPrepareId(),dataBean.getHospitalName(),dataBean.getStatus(),dataBean.getHospitalHeadPic(),position);
                }
            });
            Log.d("TAG", String.valueOf(dataBean));


        }

    }

    private View getLayout(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }


    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);


        }
    }

    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }
}
