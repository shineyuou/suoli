package com.annis.tensioncable.UI;

import android.Manifest;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;

import java.io.File;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    /***  图片比例  ***/
    public static final double ratio_image = 0.45;
    @BindString(R.string.app_name)
    String app_name;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindView(R.id.top_image)
    ImageView top_image;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("主页", colorPrimary);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        long item = System.currentTimeMillis();
        long limit = new Date(2018, 8, 1).getTime();
        if (item > limit) {
            startAcitvity(About.class);
            finish();
        }
        setTopImage();
        performCodeWithPermission("保存数据到SD卡中", new PermissionCallback() {
            @Override
            public void hasPermission() {
                Log.i(TAG, "申请到sd卡权限");
                File sd = Environment.getExternalStorageDirectory();
                String mPath = sd.getPath() + "/1";
                File file = new File(mPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                sd = Environment.getExternalStorageDirectory();
                mPath = sd.getPath() + "/1/手机";
                file = new File(mPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                sd = Environment.getExternalStorageDirectory();
                mPath = sd.getPath() + "/1/节点";
                file = new File(mPath);
                if (!file.exists()) {
                    file.mkdir();
                }
            }

            @Override
            public void noPermission() {
                Log.i(TAG, "未申请到sd卡权限");
                startAcitvity(About.class);
                finish();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void laodData() {

    }

    /**
     * 手机测量
     */
    @OnClick(R.id.tel_measure)
    void tel_measure() {
        startAcitvity(TelMeasure.class);
    }

    /**
     * 高精度测量
     */
    @OnClick(R.id.high_measure)
    void high_measure() {
        startAcitvity(HighMeasure.class);
    }

    /**
     * 设置图片
     */
    private void setTopImage() {
        int width = top_image.getLayoutParams().width;
        if (width == -1) {
            top_image.getLayoutParams().height = (int) (getWindowManager().getDefaultDisplay().getWidth() * ratio_image);
        } else {
            top_image.getLayoutParams().height = (int) (width * ratio_image);
        }
    }
}