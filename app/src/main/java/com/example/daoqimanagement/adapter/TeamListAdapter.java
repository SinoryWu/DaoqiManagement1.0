package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.TeamListResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {
    private Context context;
    private List<TeamListResponse.DataBeanX.DataBean> dataBeans;

    public TeamListAdapter(Context context,List<TeamListResponse.DataBeanX.DataBean> dataBeans){
        this.context = context;
        this.dataBeans = dataBeans;
    }

    private TeamListAdapter.OnItemClickListener mClickListener;
    //设置回调接口
    public interface OnItemClickListener {


        void onItemClick(int uid,boolean inTeam);
    }

    public void setOnItemClickListener(TeamListAdapter.OnItemClickListener listener){
        this.mClickListener  = listener;
    }
    @NonNull
    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.search_team_list_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListAdapter.ViewHolder holder, int position) {
        holder.setData(dataBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (dataBeans != null){
            return dataBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName,mTvInTeam;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.search_team_item_tv_truename);
            mTvInTeam = itemView.findViewById(R.id.search_team_item_tv_inTeam);
        }
        public void setData(final TeamListResponse.DataBeanX.DataBean dataBean,Context context,int position){
            if (dataBean.isInTeam() == true){
                mTvInTeam.setText("已选择");
            }else {
                mTvInTeam.setText("");
            }

            if (dataBean.getUser_type() == 1){
                mTvName.setText(dataBean.getTruename()+" "+"星火合伙人");
            }else if (dataBean.getUser_type() == 2){
                mTvName.setText(dataBean.getTruename()+" "+"直营团队");
            }else if (dataBean.getUser_type() == 3){
                mTvName.setText(dataBean.getTruename()+" "+"生态链合伙人");
            }else if (dataBean.getUser_type() == 4){
                mTvName.setText(dataBean.getTruename()+" "+"财务");
            }else if (dataBean.getUser_type() == 5){
                mTvName.setText(dataBean.getTruename()+" "+"初审");
            }else if (dataBean.getUser_type() == 6){
                mTvName.setText(dataBean.getTruename()+" "+"复审");
            }else if (dataBean.getUser_type() == 7){
                mTvName.setText(dataBean.getTruename()+" "+"终审");
            }else if (dataBean.getUser_type() == 8){
                mTvName.setText(dataBean.getTruename()+" "+"运维");
            }

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mClickListener.onItemClick(dataBean.getUid(),dataBean.isInTeam());
                }
            });


        }
    }
}
