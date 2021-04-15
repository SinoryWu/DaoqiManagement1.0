package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;

import java.util.List;

public class PrepareAddContactAdapter extends RecyclerView.Adapter<PrepareAddContactAdapter.ViewHolder> {
    private Context context;
    List<String> nameList;
    List<String> mobileList;
    List<String> departmentList;
    List<String> positionList;




    //设置回调接口

    private OnItemClickItemListener mItemClickListener;
    public interface OnItemClickItemListener {


        void onItemClickItem(int position);
    }

    public void setOnItemClickItemListener(OnItemClickItemListener listener){
        this.mItemClickListener = listener;
    }


    private OnClickItemListener mClickListener;
    public interface OnClickItemListener {


        void onItemClickItem(int position, Bitmap bitmap);
    }

    public void setOnItemClickListener(OnClickItemListener listener){
        this.mClickListener = listener;
    }
    public PrepareAddContactAdapter(Context context, List<String> nameList,List<String> mobileList,List<String> departmentList, List<String> positionList){
        this.context = context;
        this.nameList = nameList;
        this.mobileList = mobileList;
        this.departmentList = departmentList;
        this.positionList = positionList;
    }
    @NonNull
    @Override
    public PrepareAddContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.preapre_add_contact_item,null);


        return new PrepareAddContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrepareAddContactAdapter.ViewHolder holder, final int position) {
        holder.mRlDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClickItem(position);
            }
        });
        holder.mTvName.setText(nameList.get(position));
        holder.mTvMobile.setText(mobileList.get(position));
        holder.mTvDepartment.setText(departmentList.get(position));
        holder.mTvPosition.setText(positionList.get(position));


//        if (isAndroidQ){
//
//        }else {
//            holder.mIvAddPic.setImageBitmap(BitmapFactory.decodeFile(list.get(position)));
//        }




    }

    @Override
    public int getItemCount() {
        if (mobileList.size() > 0){
            return mobileList.size();
        }
        return 0;
    }


    // 添加数据
    public void addData(int position,String name, String mobile,String department,String position1) {
//   在list中添加数据，并通知条目加入一条
        nameList.add(position, name);
        mobileList.add(position,mobile);
        departmentList.add(position,department);
        positionList.add(position1);

        //添加动画
        notifyItemInserted(position);
    }
    // 删除数据
    public void removeData(int position) {
        nameList.remove(position);
        mobileList.remove(position);
        departmentList.remove(position);
        positionList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvName,mTvMobile,mTvDepartment,mTvPosition;
        private RelativeLayout mRlDeleteContact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mRlDeleteContact = itemView.findViewById(R.id.prepare_contact_item_delete);
            mTvName = itemView.findViewById(R.id.prepare_contact_item_name);
            mTvMobile = itemView.findViewById(R.id.prepare_contact_item_mobile);
            mTvDepartment = itemView.findViewById(R.id.prepare_contact_item_department);
            mTvPosition = itemView.findViewById(R.id.prepare_contact_item_position);

        }


    }



}
