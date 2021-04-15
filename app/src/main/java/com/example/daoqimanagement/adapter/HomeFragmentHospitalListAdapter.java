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
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class HomeFragmentHospitalListAdapter extends RecyclerView.Adapter<HomeFragmentHospitalListAdapter.ViewHolder> {

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

    public HomeFragmentHospitalListAdapter(Context context, List<HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }
    private HomeFragmentHospitalListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int prepareId,String hospitalName,int position);
    }

    public void setOnItemClickListener(HomeFragmentHospitalListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.home_fragment_hospital_list_item,null);
        if (viewType == TYPE_FOOTER) {
            return new HomeFragmentHospitalListAdapter.ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new HomeFragmentHospitalListAdapter.ViewHolder(VIEW_HEADER);
        }else {
            return new HomeFragmentHospitalListAdapter.ViewHolder(view);
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
        private ImageView mIvHospitalIcon;
        private TextView mTvHospitalName,mTvTrueName,mTvProtectTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvHospitalIcon = itemView.findViewById(R.id.home_fragment_iv_hospital_list_icon);
            mTvHospitalName = itemView.findViewById(R.id.home_fragment_tv_hospital_list_name);
            mTvTrueName = itemView.findViewById(R.id.home_fragment_tv_hospital_list_truename);
            mTvProtectTime = itemView.findViewById(R.id.home_fragment_tv_hospital_list_protect_time);
        }

        public void setData(final HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean dataBean, Context context, final int position){

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getPrepareId(),dataBean.getHospitalName(),position);
                }
            });
            Log.d("TAG", String.valueOf(dataBean));
            /**
             * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
             */
            Glide.with(context).load(Api.URL+dataBean.getHospitalHeadPic())
                    .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                    .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                    .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                    .into(mIvHospitalIcon);
            mTvHospitalName.setText(dataBean.getHospitalName());
            mTvTrueName.setText("报备人："+dataBean.getTruename());
//            Log.d("TAG", dataBean.getProtectTime());
            mTvProtectTime.setText(String.valueOf(dataBean.getStatus()));
            if (dataBean.getStatus() == 0){
                mTvProtectTime.setText("已关闭");
            }else if (dataBean.getStatus() == 1){
                mTvProtectTime.setText("预报备");
            }else if (dataBean.getStatus() == 10){
                mTvProtectTime.setText("待上传支付凭证");
            }else if (dataBean.getStatus() == 20){
                mTvProtectTime.setText("待财务审核");
            }else if (dataBean.getStatus() == 30 || dataBean.getStatus() == 40 || dataBean.getStatus() == 50){
                mTvProtectTime.setText("待审核");
            }else if (dataBean.getStatus() == 60){
                mTvProtectTime.setText("待退款");
            }else if (dataBean.getStatus() == 70){
                mTvProtectTime.setText("已退款");
            }else if (dataBean.getStatus() == 80){
                mTvProtectTime.setText("已取消");
            }else if (dataBean.getStatus() == 90){
               if (dataBean.getDelay() == 0){
                   mTvProtectTime.setText("保护截止期："+dataBean.getProtectTime().substring(0,10));
               }else if (dataBean.getDelay() == 1){
                   mTvProtectTime.setText("待上传支付凭证");
               }else if (dataBean.getDelay() == 2){
                   mTvProtectTime.setText("待财务审核");
               }else if (dataBean.getDelay() == 3){
                   mTvProtectTime.setText("待审核");
               }
            }else if (dataBean.getStatus() == 95){
                mTvProtectTime.setText("开发完成");
            }
//            if (dataBean.getProtectTime().equals("null") || dataBean.getProtectTime() == null){
//                mTvProtectTime.setText("保护截止期：");
//            }else {
////
//                mTvProtectTime.setText("保护截止期："+dataBean.getProtectTime().substring(0,10));
//            }
            Log.d("mTvProtectTime", dataBean.getProtectTime());
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
