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

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.HomeFragmentProgressListResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class HomeFragmentProgressListAdapter extends RecyclerView.Adapter<HomeFragmentProgressListAdapter.ViewHolder> {
    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;
    private int TYPE_FOOTER2 = 0;
    private RecyclerView mRecyclerView;
    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    private Context context;
    private List<HomeFragmentProgressListResponse.DataBeanX.DataBean> dataBeans;
    private HomeFragmentProgressListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int scheduleId,int position);
    }

    public void setOnItemClickListener(HomeFragmentProgressListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    public HomeFragmentProgressListAdapter(Context context, List<HomeFragmentProgressListResponse.DataBeanX.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.home_fragment_progress_list_item,null);

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
            holder.setData(dataBeans.get(position),context,position);

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
        private TextView mTvCreateTime,mTvTrueName,mTvHospitalName,mTvDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCreateTime= itemView.findViewById(R.id.home_fragment_tv_progress_list_create_time);
            mTvTrueName= itemView.findViewById(R.id.home_fragment_tv_progress_list_truename);
            mTvHospitalName= itemView.findViewById(R.id.home_fragment_tv_progress_list_hospitalName);
            mTvDesc= itemView.findViewById(R.id.home_fragment_tv_progress_list_desc);



        }

        public void setData(final HomeFragmentProgressListResponse.DataBeanX.DataBean dataBean, Context context, final int position){

            Log.d("progresslist", String.valueOf(dataBean));

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getScheduleId(),position);
                }
            });

            if (dataBean.getCreated_at() == null){
                mTvCreateTime.setText("");
            }else {
                String createAt1 = dataBean.getCreated_at();
                String createAt= createAt1.substring(0,16);
                StringBuffer buffer = new StringBuffer(createAt);
                buffer.replace(4,5,"年");
                buffer.replace(7,8,"月");
                buffer.replace(10,11,"日");
                buffer.insert(11," ");
                mTvCreateTime.setText(buffer);
            }
            mTvTrueName.setText(dataBean.getTruename());
            mTvHospitalName.setText(dataBean.getHospitalName());
            mTvDesc.setText(dataBean.getDesc());


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
