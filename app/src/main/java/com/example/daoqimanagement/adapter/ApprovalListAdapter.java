package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ApprovalListResponse;
import com.example.daoqimanagement.bean.HomeFragmentProgressListResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ApprovalListAdapter extends RecyclerView.Adapter<ApprovalListAdapter.ViewHolder> {
    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;
    private int TYPE_FOOTER2 = 0;
    private RecyclerView mRecyclerView;
    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    private Context context;
    private List<ApprovalListResponse.DataBeanX.DataBean> dataBeans;

    public ApprovalListAdapter(Context context, List<ApprovalListResponse.DataBeanX.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    private ApprovalListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int approvalId);
    }

    public void setOnItemClickListener(ApprovalListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.approval_fragment_approval_list_item,null);

        if (viewType == TYPE_FOOTER) {
            return new ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new ViewHolder(VIEW_HEADER);
        }else {
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            holder.setData(dataBeans.get(position),context);
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


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvCreateTime,mTvStatus,mTvTitle,mTvTrueName,mTvReason;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCreateTime= itemView.findViewById(R.id.approval_fragment_tv_progress_list_create_time);
            mTvStatus= itemView.findViewById(R.id.approval_fragment_tv_approval_list_status);
            mTvTitle= itemView.findViewById(R.id.approval_fragment_tv_approval_list_title);
            mTvTrueName= itemView.findViewById(R.id.approval_fragment_tv_approval_list_truename);
            mTvReason= itemView.findViewById(R.id.approval_fragment_tv_approval_list_reason);

        }

        public void setData(final ApprovalListResponse.DataBeanX.DataBean dataBean, Context context){


            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getApprovalId());
                }
            });
            Log.d("CreatedAt", String.valueOf(dataBean));

            if (dataBean.getCreatedAt().length() < 1){
                mTvCreateTime.setText("");
            }else {
                String createAt1 = dataBean.getCreatedAt();
                String createAt= createAt1.substring(0,16);
                StringBuffer buffer = new StringBuffer(createAt);
                buffer.replace(4,5,"年");
                buffer.replace(7,8,"月");
                mTvCreateTime.setText(buffer);
            }


            mTvTrueName.setText("发起人："+dataBean.getTruename());
            mTvTitle.setText(dataBean.getTitle());
            mTvReason.setText(dataBean.getReason());
            if (dataBean.getStatus() == 1){
                mTvStatus.setText("待处理");
                mTvStatus.setTextColor(Color.parseColor("#F45C50"));
            }else if (dataBean.getStatus() == 2){
                mTvStatus.setText("已通过");
                mTvStatus.setTextColor(Color.parseColor("#6CC291"));
            }else if (dataBean.getStatus() == 3){
                mTvStatus.setText("不通过");
                mTvStatus.setTextColor(Color.parseColor("#BCC5D3"));
            }


            if (dataBean.getLast_page() == dataBean.getCurrent_page()){
                TYPE_FOOTER2 = 1;
            }



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
