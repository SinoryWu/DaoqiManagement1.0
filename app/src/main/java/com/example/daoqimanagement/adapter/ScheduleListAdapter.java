package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.AreaListResponse;
import com.example.daoqimanagement.bean.PrepareDetailForUserResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {


    private Context context;
    private List<PrepareDetailForUserResponse.DataBean.ScheduleListBean> dataBeans;
    private ScheduleListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int scheduleId,int position);
    }

    public void setOnItemClickListener(ScheduleListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }

    public ScheduleListAdapter(Context context, List<PrepareDetailForUserResponse.DataBean.ScheduleListBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.schedule_list_item,null);


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
        private TextView mTvCreateTime,mTvTrueName,mTvDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCreateTime = itemView.findViewById(R.id.schedule_list_item_tv_createdTime);
            mTvTrueName = itemView.findViewById(R.id.schedule_list_item_tv_truename);
            mTvDesc = itemView.findViewById(R.id.schedule_list_item_tv_desc);

        }

        public void setData(final PrepareDetailForUserResponse.DataBean.ScheduleListBean dataBean, Context context, final int position){

            if (dataBean.getCreatedTime() == null){
                mTvCreateTime.setText("");
            }else {
                String createAt1 = dataBean.getCreatedTime();
                String createAt= createAt1.substring(0,16);
                StringBuffer buffer = new StringBuffer(createAt);
                buffer.replace(4,5,"年");
                buffer.replace(7,8,"月");
                buffer.replace(10,11,"日");
                buffer.insert(11," ");
                mTvCreateTime.setText(buffer);
            }

            mTvTrueName.setText(dataBean.getTruename());
            mTvDesc.setText(dataBean.getDesc());

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getScheduleId(),position);
                }
            });
        }

    }




}
