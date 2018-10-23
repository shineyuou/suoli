package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.My.Adapter;
import com.annis.tensioncable.My.FileBean;
import com.annis.tensioncable.My.Status;
import com.annis.tensioncable.My.filesize;
import com.annis.tensioncable.R;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareActivtiy extends BaseActivity implements SwipeItemClickListener {

    private static final String PHONE = Environment.getExternalStorageDirectory() + "/1/手机";
    private static final String CABLE = Environment.getExternalStorageDirectory() + "/1/节点";
    private String Root = null;

    //当前目录
    private File mfile = null;
    private @SuppressLint("SimpleDateFormat")
    SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<FileBean> mDataList;
    private Adapter mAdapter;
    private long firstTime = 0;

    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView mRecyclerView;
    private String mSource;

    @Override
    protected int getLayout() {
        return R.layout.activity_share;
    }

    @Override
    protected TitleBean getMyTitle() {
        mSource = getIntent().getStringExtra("source");
        return new TitleBean(mSource).setBack(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        SwipeMenuRecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        if (mSource.equals("手机测量")) {
            Root = PHONE;
            mDataList = createDataList(Root);
        }
        if (mSource.equals("高精度测量")) {
            Root = CABLE;
            mDataList = createDataList(Root);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this,
                R.color.divider_color)));
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        recyclerView.setSwipeItemClickListener(this);

        mAdapter = new Adapter(mDataList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void laodData() {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        File file = mDataList.get(position).getFile_path();
        if (file.isFile()) {
            Toast.makeText(this, "文件", Toast.LENGTH_SHORT).show();
        } else {
            mDataList = createDataList(mDataList.get(position).getFile_path().getPath());
            mAdapter.notifyDataSetChanged(mDataList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 更新UI
     *
     * @param path 更新UI的文件路径
     * @return 文件
     */
    private List<FileBean> createDataList(String path) {
        mfile = new File(path);
        List<FileBean> dataList = new ArrayList<>();
        File[] list = new File(path).listFiles();
        for (File aList : list) {
            String name = aList.getName();
            String time = Formatter.format(aList.lastModified());
            double size = filesize.getFileOrFilesSize(aList.getPath(),
                    filesize.SIZETYPE_KB);
            if (aList.isFile()) {
                dataList.add(new FileBean(R.drawable.csv, name, time, size, aList));
            } else {
                dataList.add(new FileBean(R.drawable.docu, name, time, size, aList));
            }
        }
        return dataList;
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_60);

        // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
        // 2. 指定具体的高，比如80;
        // 3. WRAP_CONTENT，自身高度，不推荐;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(ShareActivtiy.this)
                    .setBackground(R.drawable.selector_share)
                    .setImage(R.drawable.share)
                    .setText("分享")
                    .setTextColor(Color.rgb(219, 219, 219))
                    .setWidth(width)
                    .setHeight(height);
            swipeLeftMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

        }
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(ShareActivtiy.this)
                    .setBackground(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            SwipeMenuItem addItem = new SwipeMenuItem(ShareActivtiy.this)
                    .setBackground(R.drawable.selector_green)
                    .setText("重命名")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new
            SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge) {
                    menuBridge.closeMenu();

                    int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                    final int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                    int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

                    if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                        switch (menuPosition) {
                            case 1:
                                showRename(mDataList.get(adapterPosition).getFile_path(), new Status() {
                                    @Override
                                    public void Positive(String s) {
                                        File file = mDataList.get(adapterPosition).getFile_path();
                                        String houzhui = "";
                                        if (file.isFile()) {
                                            houzhui = file.getName().substring(file.getName().lastIndexOf("."));
                                        }
                                        String replace = file.getPath().replace(file.getName(), s + houzhui);
                                        if (file.renameTo(new File(replace))) {
                                            File file1 = new File(replace);
                                            mDataList.get(adapterPosition).setFile_path(file1);
                                            mDataList.get(adapterPosition).setFile_name(s + houzhui);
                                            mAdapter.notifyDataSetChanged(mDataList);
                                        }
                                    }

                                    @Override
                                    public void Negative() {
                                    }
                                });
                                break;
                            case 0:
                                showDelete(mDataList.get(adapterPosition).getFile_path(), new Status() {
                                    @Override
                                    public void Positive(String s) {
                                        File file = mDataList.get(adapterPosition).getFile_path();
                                        if (file.exists()) {
                                            if (file.delete()) {
                                                mDataList.remove(adapterPosition);
                                                mAdapter.notifyDataSetChanged(mDataList);
                                            }
                                        }
                                    }

                                    @Override
                                    public void Negative() {

                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                        Intent email = new Intent(android.content.Intent.ACTION_SEND);
                        // 附件
                        File file = mDataList.get(adapterPosition).getFile_path();
                        //邮件发送类型：带附件的邮件
                        email.setType("application/octet-stream");
                        //邮件接收者（数组，可以是多位接收者）
                        String[] emailReciver = new String[]{"2674803392@qq.com"};

                        String  emailTitle = "索力数据";
                        String emailContent = mDataList.get(adapterPosition).getFile_name();
                        //设置邮件地址
                        email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
                        //设置邮件标题
                        email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);
                        //设置发送的内容
                        email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
                        //附件
                        email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        //调用系统的邮件系统
                        startActivity(Intent.createChooser(email, "分享方式"));
                    }
                }
            };

    public void showDelete(File file, final Status s) {
        if (file.isFile()) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("删除")
                    .setMessage(file.getName())
                    .setPositiveButton("确认", (dialog1, which) -> s.Positive(null))
                    .setNegativeButton("取消", (dialog12, which) -> s.Negative())
                    .create();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            try {
                Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                mAlert.setAccessible(true);
                Object mAlertController = mAlert.get(dialog);
                //通过反射修改title字体大小和颜色
                Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
                mTitle.setAccessible(true);
                TextView mTitleView = (TextView) mTitle.get(mAlertController);
                mTitleView.setTextSize(19);
                mTitleView.setTextColor(Color.BLACK);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重命名
     *
     * @param prefile 原始文件名
     * @param stautus 回调接口
     */
    public void showRename(File prefile, final Status stautus) {
        final EditText text = new EditText(this);
        text.setSingleLine();
        text.setHint(prefile.getName());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("重命名")
                .setView(text)
                .setPositiveButton("确认", (dialog12, which) -> stautus.Positive(text.getText().toString()))
                .setNegativeButton("取消", (dialog1, which) -> stautus.Negative())
                .create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            //通过反射修改title字体大小和颜色
            Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
            mTitle.setAccessible(true);
            TextView mTitleView = (TextView) mTitle.get(mAlertController);
            mTitleView.setTextSize(19);
            mTitleView.setTextColor(Color.BLACK);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mfile.getPath().equals(Root)) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(ShareActivtiy.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    finish();
                }
            } else {
                String pre_path = mfile.getParent();
                mDataList = createDataList(pre_path);
                mAdapter.notifyDataSetChanged(mDataList);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
