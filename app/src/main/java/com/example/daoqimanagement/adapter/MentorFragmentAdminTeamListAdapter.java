package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.MentorsFragmentAdminTeamListResponse;
import com.example.daoqimanagement.utils.OnMultiClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MentorFragmentAdminTeamListAdapter extends RecyclerView.Adapter<MentorFragmentAdminTeamListAdapter.ViewHolder> {
    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;
    private int TYPE_FOOTER2 = 0;
    private RecyclerView mRecyclerView;
    private View VIEW_FOOTER;
    private View VIEW_HEADER;
    private OnItemClickListener mOnItemClickListener;//声明自定义的接口
    private OnItem1ClickListener mOnItem1ClickListener;//声明自定义的接口

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItem1ClickListener  {
        void onItem1Click( String phoneNumber,int userType,String createTime,String headPic,String trueName,int uid, int position);

    }


    //定义方法并传给外面的使用者
    public void setOnItem1ClickListener(OnItem1ClickListener  listener) {
        this.mOnItem1ClickListener  = listener;
    }

    private Context context;
    private List<MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean> dataBeans;

    public MentorFragmentAdminTeamListAdapter(Context context, List<MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.mentors_fragment_team_list_item, null);

        if (viewType == TYPE_FOOTER) {
            return new MentorFragmentAdminTeamListAdapter.ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new MentorFragmentAdminTeamListAdapter.ViewHolder(VIEW_HEADER);
        } else {
            return new MentorFragmentAdminTeamListAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            holder.setData(dataBeans.get(position), context,position);
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
        } else if (isFooterView(position)  &&  TYPE_FOOTER2==1) {

            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTrueName, mTvPosition;
        private RelativeLayout mRlCallPhone;
        private CircleImageView mIvUserIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTrueName = itemView.findViewById(R.id.mentors_fragment_team_list_tv_user_true_name);
            mTvPosition = itemView.findViewById(R.id.mentors_fragment_team_list_tv_user_position);
            mRlCallPhone = itemView.findViewById(R.id.mentors_fragment_team_list_rl_call_phone);
            mIvUserIcon = itemView.findViewById(R.id.mentors_fragment_team_list_iv_user_list_icon);

        }

        public void setData(final MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean dataBean, final Context context, final int position) {

            mTvTrueName.setText(dataBean.getTruename());

            Glide.with(context).load("sadasd")
                    .error(R.mipmap.team_list_user_icon)//异常时候显示的图片
                    .fallback(R.mipmap.team_list_user_icon)//url为空的时候,显示的图片
                    .placeholder(R.mipmap.team_list_user_icon)//加载成功前显示的图片
                    .into(mIvUserIcon);
            mRlCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mOnItemClickListener.onItemClick(view,dataBean.getMobile(),position);
                }
            });

            if (dataBean.getLast_page() == dataBean.getCurrent_page()){
                TYPE_FOOTER2 = 1;
            }

            if (dataBean.getUserType() == 1){
                mTvPosition.setText("星火合伙人");
            }else if (dataBean.getUserType() == 2){
                mTvPosition.setText("直营团队");
            }else if (dataBean.getUserType() ==3 ){
                mTvPosition.setText("生态链合伙人");
            }else if (dataBean.getUserType() ==4 ){
                mTvPosition.setText("财务");
            }else if (dataBean.getUserType() ==5 ){
                mTvPosition.setText("初审");
            }else if (dataBean.getUserType() ==6 ){
                mTvPosition.setText("复审");
            }else if (dataBean.getUserType() ==7 ){
                mTvPosition.setText("终审");
            }else if (dataBean.getUserType() ==8 ){
                mTvPosition.setText("运维");
            }

            itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    mOnItem1ClickListener.onItem1Click(dataBean.getMobile(), dataBean.getUserType(),dataBean.getCreateTime(),dataBean.getHeadPic(),dataBean.getTruename(),dataBean.getUid(),position);
                }
            });
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

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, String phoneNumber, int position);
        void onItemLongClick(View v);
    }


    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }



}
