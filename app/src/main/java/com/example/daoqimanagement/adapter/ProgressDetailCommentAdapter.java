package com.example.daoqimanagement.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoqimanagement.ProgressDetailActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.bean.ProgressDetailCommentResponse;
import com.example.daoqimanagement.dialog.CheckPhotoURLDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.FileUtils;
import com.example.daoqimanagement.utils.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;

public class ProgressDetailCommentAdapter extends RecyclerView.Adapter<ProgressDetailCommentAdapter.ViewHolder> {
    AlertDialog spotDialog;
    private Context context;
    ProgressDetailActivity progressDetailActivity;
    DialogTokenIntent dialogTokenIntent = null;
    private List<ProgressDetailCommentResponse.DataBean.CommentBean>  commentBeans;
    int scheduleId;
    private PopupWindow popupWindow;
    private Animation animation;
    String filePath;
    public ProgressDetailCommentAdapter (Context context , List<ProgressDetailCommentResponse.DataBean.CommentBean>  commentBeans,ProgressDetailActivity progressDetailActivity){
        this.commentBeans =commentBeans;
        this.progressDetailActivity = progressDetailActivity;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgressDetailCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.progress_detail_comment_item,null);
        return new ProgressDetailCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressDetailCommentAdapter.ViewHolder holder, int position) {
        holder.setData(commentBeans.get(position),context,position);
    }

    @Override
    public int getItemCount() {
        if (commentBeans != null){
            return commentBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvCreateTime,mTvTrueName,mTvComment;
        private RecyclerView mRcPictureList,mRcAppendixList;
        ProgressDetailCommentPictureAdapter pictureAdapter;
        ProgressDetailCommentAppendixAdapter appendixAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCreateTime= itemView.findViewById(R.id.progress_detail_comment_item_tv_createTime);
            mTvTrueName= itemView.findViewById(R.id.progress_detail_comment_item_tv_truename);
            mTvComment= itemView.findViewById(R.id.progress_detail_comment_item_tv_comment);
            mRcPictureList= itemView.findViewById(R.id.progress_detail_comment_item_rc_picture);
            mRcAppendixList= itemView.findViewById(R.id.progress_detail_comment_item_rc_appendix);


        }

        public void setData(final ProgressDetailCommentResponse.DataBean.CommentBean commentBean, final Context context, final int position){
            if (commentBean.getCreated_at() == null){
                mTvCreateTime.setText("");
            }else {
                String createAt1 = commentBean.getCreated_at();
                String createAt= createAt1.substring(0,16);
                StringBuffer buffer = new StringBuffer(createAt);
                buffer.replace(4,5,"年");
                buffer.replace(7,8,"月");
                buffer.replace(10,11,"日");
                buffer.insert(11," ");
                mTvCreateTime.setText(buffer);
            }
            mTvComment.setText(commentBean.getDesc());
            mTvTrueName.setText(commentBean.getTruename());


            pictureAdapter = new ProgressDetailCommentPictureAdapter(context,commentBean.getPictures());
            LinearLayoutManager linearLayoutManagerPic = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false){
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            };
            mRcPictureList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mRcPictureList.setAdapter(pictureAdapter);
            pictureAdapter.setOnItemClickListener(new ProgressDetailCommentPictureAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String path, int position) {
//                    Intent intent = new Intent(context, CheckPhotoUrlActivity.class);
//                    intent.putExtra("path", Api.URL+path);
//                    context.startActivity(intent);
                    CheckPhotoURLDialog checkPhotoURLDialog = new CheckPhotoURLDialog(context,R.style.CustomDialogPhoto,Api.URL+path);
                    checkPhotoURLDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    checkPhotoURLDialog.show();
                    if (checkPhotoURLDialog.isShowing()){
                        final Window window=((Activity)context).getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setStatusBarColor(Color.parseColor("#000000"));
                        }
                    }
                    checkPhotoURLDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                            final Window window=((Activity)context).getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.setStatusBarColor(Color.parseColor("#ffffff"));
                            }

                        }
                    });


                    Window dialogWindow = checkPhotoURLDialog.getWindow();
                    dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.y = 0;
                    dialogWindow.setAttributes(lp);
                }
            });

            appendixAdapter = new ProgressDetailCommentAppendixAdapter(context,commentBean.getAppendixs());
            LinearLayoutManager linearLayoutManagerAppendix = new LinearLayoutManager(context);
            mRcAppendixList.setLayoutManager(linearLayoutManagerAppendix);
            mRcAppendixList.setAdapter(appendixAdapter);

            appendixAdapter.setOnItemClickListener(new ProgressDetailCommentAppendixAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String path, int position, View view) {
                    final File folder = context.getExternalFilesDir(null);
                    final String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
                    if (!folder.exists()) {
                        folder.mkdir();
                    }

                    if (fileIsExists(String.valueOf(folder)+"/download/" + fileName)) {
//

                        filePath = (folder)+"/download/" + fileName;
                        showPuPopWindow(view);

//

                    } else {
                        spotDialog = new SpotsDialog.Builder().setContext(context).setTheme(R.style.SpotDialogCustom)
                                .setCancelable(false).build();
                        spotDialog.show();
                        DownLoadFile(Api.URL + path,(folder)+"/download/" + fileName);



                    }
                }
            });

//            //1.拿到okhttp对象
//            OkHttpClient okHttpClient = new OkHttpClient();
//
//            //2.构造request
//            Request request = new Request.Builder()
//                    .get()
//                    .url(Api.URL+"/v1/schedule/detail?scheduleId="+String.valueOf(scheduleId))
//                    .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                    .addHeader("uid", "8")
////                    .addHeader("token", getTokenToSp("token", ""))
////                    .addHeader("uid", getUidToSp("uid", ""))
//                    .build();
//            //3.将request封装为call
//            Call call = okHttpClient.newCall(request);
//            //4.执行call
////        同步执行
////        Response response = call.execute();
//
//            //异步执行
//            call.enqueue(new Callback() {
//
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                    e.printStackTrace();
//
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtils.showTextToast2(context, "网络请求失败");
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                    final String res = response.body().string();
//                    Log.d("searchUserInfo2", res);
//
//                    //封装java对象
//
//                    Gson gson = new Gson();
//                    final ProgressDetailCommentResponse2 progressDetailCommentResponse = gson.fromJson(res,ProgressDetailCommentResponse2.class);
//
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                            if (progressDetailCommentResponse.getCode() == 0) {
//                                pictureAdapter = new ProgressDetailCommentPictureAdapter(context,progressDetailCommentResponse.getData().getComment().get(position).getPictures());
//                                LinearLayoutManager linearLayoutManagerPic = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false){
//                                    @Override
//                                    public boolean canScrollHorizontally() {
//                                        return false;
//                                    }
//                                };
//                                mRcPictureList.setLayoutManager(linearLayoutManagerPic);
//                                mRcPictureList.setAdapter(pictureAdapter);
//                                pictureAdapter.setOnItemClickListener(new ProgressDetailCommentPictureAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(String path, int position) {
//                                        Intent intent = new Intent(context, CheckPhotoUrlActivity.class);
//                                        intent.putExtra("path", Api.URL+path);
//                                        context.startActivity(intent);
//                                    }
//                                });
//
//                                appendixAdapter = new ProgressDetailCommentAppendixAdapter(context,progressDetailCommentResponse.getData().getComment().get(position).getAppendixs());
//                                LinearLayoutManager linearLayoutManagerAppendix = new LinearLayoutManager(context);
//                                mRcPictureList.setLayoutManager(linearLayoutManagerAppendix);
//                                mRcAppendixList.setAdapter(appendixAdapter);
//
//                                appendixAdapter.setOnItemClickListener(new ProgressDetailCommentAppendixAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(String path, int position, View view) {
//                                        final File folder = context.getExternalFilesDir(null);
//                                        final String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
//                                        if (!folder.exists()) {
//                                            folder.mkdir();
//                                        }
//
//                                        if (fileIsExists(String.valueOf(folder)+"/download/" + fileName)) {
////
//
//                                            filePath = (folder)+"/download/" + fileName;
//                                            showPuPopWindow(view);
//
////
//
//                                        } else {
//                                            ToastUtils.showTextToast3(context, "下载中，请稍候...");
//                                            DownLoadFile(Api.URL + path,(folder)+"/download/" + fileName);
//
//
//
//                                        }
//                                    }
//                                });
//
//
//                            } else if (progressDetailCommentResponse.getCode() == 10009) {
//                                if (dialogTokenIntent == null) {
//                                    dialogTokenIntent = new DialogTokenIntent(context, R.style.CustomDialog);
//                                    dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
//                                        @Override
//                                        public void OnConfirm(DialogTokenIntent dialog) {
//                                            Intent intent = new Intent(context, LoginActivity.class);
//                                            ((Activity)context).finish();
//                                            context.startActivity(intent);
//
//                                        }
//                                    }).show();
//
//                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
//                                    dialogTokenIntent.setCancelable(false);
//                                }
//                            } else {
//                                String msg = progressDetailCommentResponse.getMsg();
//                                ToastUtils.showTextToast2(context, msg);
//                            }
//
//
//                        }
//                    });
//
//
//                }
//
//
//            });




        }




        public void showPuPopWindow(View popupView){
            if (popupWindow == null) {
                popupView = View.inflate(context, R.layout.file_popview_item, null);
                // 参数2,3：指明popupwindow的宽度和高度
                popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);

                // 设置背景图片， 必须设置，不然动画没作用
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
                animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(250);

                // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                popupWindow.showAtLocation(progressDetailActivity.findViewById(R.id.progress_detail_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupView.startAnimation(animation);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        popupWindow = null;
                    }
                });

                popupView.findViewById(R.id.file_popwindow_tv_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (FileUtils.isImageFileType1(filePath) == true){
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//给临时权限
                            sharingIntent.setType("image/*");//根据文件类型设定type
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                        FileProvider.getUriForFile(context, context.getPackageName() +".fileprovider",
                                                new File(filePath)));

                            }else {
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                            }

                            context.startActivity(Intent.createChooser(sharingIntent, "分享"));
                        }else {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//给临时权限
                            sharingIntent.setType("*/*");//根据文件类型设定type
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                        FileProvider.getUriForFile(context, context.getPackageName() +".fileprovider",
                                                new File(filePath)));

                            }else {
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                            }

                            context.startActivity(Intent.createChooser(sharingIntent, "分享"));
                        }

                        popupWindow.dismiss();
                    }
                });

                popupView.findViewById(R.id.file_popwindow_tv_check_path).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                   ToastUtils.showTextToast3(ProgressDetailActivity.this,filePath);
                        Toast.makeText(context,filePath,Toast.LENGTH_LONG).show();
                        popupWindow.dismiss();
                    }
                });

                popupView.findViewById(R.id.file_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

            }
        }

        /**
         * 判断文件是否存在
         * @param filePath
         * @return
         */
        private boolean fileIsExists(String filePath) {
            try {
                File f = new File(filePath);
                if (!f.exists()) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        /**
         * 下载文件到本地
         * @param url
         * @param path
         */
        private void DownLoadFile(String url,String path){
            FileDownloader.getImpl().create(url).setPath(path).setListener(new FileDownloadListener() {
                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {


                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {



                }

                @Override
                protected void completed(BaseDownloadTask task) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            /**
                             * 延时执行的代码
                             */
                            spotDialog.dismiss();
                            ToastUtils.showTextToast2(context, "下载完成");

                        }
                    },1500); // 延时1.5秒


                }

                @Override
                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    spotDialog.dismiss();
                    ToastUtils.showTextToast2(context, "下载失败");
                }

                @Override
                protected void warn(BaseDownloadTask task) {

                }
            }).start();
        }





    }





}
