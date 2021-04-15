package com.example.daoqimanagement.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.R;

import java.util.List;

public class UpDataProgressAddFileAdapter extends RecyclerView.Adapter<UpDataProgressAddFileAdapter.ViewHolder> {
    private Context context;
    List<String> list;


    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
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


        void onItemClickItem(int position, String uri);
    }

    public void setOnItemClickListener(OnClickItemListener listener){
        this.mClickListener = listener;
    }
    public UpDataProgressAddFileAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public UpDataProgressAddFileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.updata_progress_add_file_item,null);


        return new UpDataProgressAddFileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpDataProgressAddFileAdapter.ViewHolder holder, final int position) {
        holder.mRlDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClickItem(position);
            }
        });

        holder.mTvFileName.setText(list.get(position));

    }


    @Override
    public int getItemCount() {
        if (list.size() > 0){
            return list.size();
        }
        return 0;
    }


    // 添加数据
    public void addData(int position,String FileName) {
//   在list中添加数据，并通知条目加入一条
        list.add(position, FileName);
        //添加动画
        notifyItemInserted(position);
    }
    // 删除数据
    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout mRlDeleteFile;
        private TextView mTvFileName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mRlDeleteFile = itemView.findViewById(R.id.update_progress_add_file_rl_delete);
            mTvFileName = itemView.findViewById(R.id.update_progress_add_file_tv_name);

        }


    }



}
