package com.annis.tensioncable.UI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;

import org.litepal.LitePal;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;

import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    private static final String TAG = "MainActivity";
    /**
     * 图片比例
     */
    public static final double ratio_image = 0.45;

    private WifiManager mWifiManager;
    private int state;

    @BindString(R.string.app_name)
    String app_name;

    @BindColor(R.color.colorPrimary)
    int colorPrimary;

    @BindView(R.id.top_image)
    ImageView top_image;

    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;

    @BindView(R.id.main_switch)
    SwitchCompat main_switch;

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
        //创建数据库
        LitePal.deleteDatabase("iputils");
        LitePal.getDatabase();
        startAcitvity(HighMeasure.class);
    }

    /**
     * 帮助
     */
    @OnClick(R.id.tel_data_help)
    void help(View view) {
        right_menu.setVisibility(View.GONE);
    }


    /**
     * 主页更多菜单
     */
    @OnClick(R.id.right_view)
    void more() {
        if (right_menu.getVisibility() == View.GONE)
            right_menu.setVisibility(View.VISIBLE);
        else
            right_menu.setVisibility(View.GONE);
    }

    /**
     * 触控消失菜单
     */
    @OnClick(R.id.part_right_menu_back)
    void gone(View view) {
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 初始化事件
     */
    @Override
    protected void initViewAndEvent() {
        long item = System.currentTimeMillis();
        long limit = new Date(2018, 8, 1).getTime();
        if (item > limit) {
            startAcitvity(About.class);
            finish();
        }
        main_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 判断是否有WRITE_SETTINGS权限if(!Settings.System.canWrite(this))
                    if (!Settings.System.canWrite(this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1);
                    } else {
                        createWifitHot("你家大爷的wife", "95279527");
                    }
                }
            } else {
                closeWifiHot();
            }
        });
        //setTopImage();
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

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                    //state状态为：10---正在关闭；11---已关闭；12---正在开启；13---已开启
                    state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    switch (state) {
                        case 10:
                            Toast.makeText(MainActivity.this, "正在关闭热点", Toast.LENGTH_SHORT).show();
                            break;
                        case 11:
                            Toast.makeText(MainActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
                            break;
                        case 12:
                            Toast.makeText(MainActivity.this, "正在开启热点", Toast.LENGTH_SHORT).show();
                            break;
                        case 13:
                            Toast.makeText(MainActivity.this, "已开启", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "无状态", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            }
        };
        IntentFilter mIntentFilter = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
        registerReceiver(mReceiver, mIntentFilter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }

    }

    /**
     * 加载数据
     */
    @Override
    protected void laodData() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("主页", colorPrimary).setRightViewId(R.layout.part_title_right_more);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWifiHot();
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

    /**
     * 设置热点
     */
    public void createWifitHot(String name, String password) {
        try {
            //wifi和热点不能同时打开，所以先判断wifi是否打开，打开则关闭
            if (mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(false);
            }
            //java反射机制得到Method
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            //配置热点信息
            WifiConfiguration config = new WifiConfiguration();
            config.SSID = name;
            config.preSharedKey = password;
            config.hiddenSSID = false;
            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(4);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            //通过java反射调用WifiManager的setWifiApEnabled方法
            method.invoke(mWifiManager, config, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭热点
     */
    public void closeWifiHot() {
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}