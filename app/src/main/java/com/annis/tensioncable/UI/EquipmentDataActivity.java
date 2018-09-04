package com.annis.tensioncable.UI;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.My.Compare;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.Utils.ConstantsSP;
import com.annis.tensioncable.model.Equipment;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

public class EquipmentDataActivity extends BaseActivity {
    private static final String TAG = "EquipmentDataActivity";

    private long Start_time = 0;
    private long End_time = 0;

    private static final int START = 1, PAUSE = 2, STOP = 0;
    private int status_flag;

    private static final int XNUM = 400;// x轴长度,即屏幕中显示的点数

    private float light[] = {0, 0, 0};
    private int[] colors = new int[]{Color.GREEN, Color.RED, Color.rgb(255, 69, 0),
            Color.BLACK};
    private String[] titles = new String[]{"X", "Y", "Z"};// 图表一的图例
    private String[] xkedu;// x轴数据缓冲
    private List<Float> dataList; // 存放数据的集合
    private List<XYSeries> seriesList;
    private List<Float[]> catchList;// 存放缓存数据的集合
    private GraphicalView mChartView;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer mRenderer;

    private Timer timer;
    private Handler handle;
    private SimpleDateFormat nowTime; // 获取当前时间

    private XYMultipleSeriesDataset mDataSet;
    private XYMultipleSeriesRenderer mRender;


    private MyReceiver receiver = null;

    @BindView(R.id.item_status_iv)
    ImageView status_iv;
    @BindView(R.id.item_status_tv)
    TextView status_tv;
    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;
    @BindView(R.id.char1)
    LinearLayout Char1;

    Equipment item;
    String title = "振动数据";
    private String mMac;
    private File mFile = null;
    private Integer limit_Time;

    @Override
    protected int getLayout() {
        return R.layout.activity_tel_measure;
    }

    @Override
    protected TitleBean getMyTitle() {
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        mMac = intent.getStringExtra("object");
        title = mMac + "振动数据";
        TextView textView = new TextView(this);
        textView.setTextColor(Color.WHITE);
        textView.setText("索力分析");
        textView.setTextSize(16);
        textView.setOnClickListener(v -> startAcitvity(SelectCableActivity.class, mFile.getPath()));
        return new TitleBean(title).setBack(true).setRightView(textView);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            double count = bundle.getDouble("count");
            try {
                dealdata(count);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO 对传入的数据进行处理

        }
    }

    /**
     * 1.对当前状态进行判断
     * 2.仿照手机端，首先是暂停状态
     * 3.开启后才会进行存储，按照设定时间存储
     * 4.时间未到之前按下停止或暂停会删除此次存储数据
     */
    private void dealdata(double data) throws IOException {

        switch (status_flag) {
            case START:
                String s = System.currentTimeMillis() + "," + data + "\n";
                long now = System.currentTimeMillis();
                FileOutputStream fos = new FileOutputStream(mFile, true);
                fos.write(s.getBytes());
                fos.close();
                if (now >= End_time) {
                    setStatus(STOP);
                    break;
                }
                updatechart(data);
                break;
            case STOP:
                break;
            case PAUSE:
                break;
            default:
                break;
        }

    }

    /**
     * 更新数据
     */
    private void updatechart(double data) {
        float dataOne = (float) data;
        float dataTwo = (float) data;
        float dataThree = (float) data;
        dataList.clear();
        dataList.add(dataOne);
        dataList.add(dataTwo);
        dataList.add(dataThree);
        String xKeduValue = nowTime.format(new java.util.Date());
        Log.i(TAG, "" + xKeduValue + ",,," + data);
        //得到x轴上点的数量
        int seriesItemLenght = seriesList.get(0).getItemCount();
        // x轴控制显示10个数值
        if (seriesItemLenght > XNUM) {
            seriesItemLenght = XNUM;
        }
        // 移除旧的点集
        for (int i = 0; i < titles.length; i++) {
            dataset.removeSeries(seriesList.get(i));
        }

        if (seriesItemLenght < XNUM) {
            for (int i = 0; i < titles.length; i++) {
                seriesList.get(i).add(seriesItemLenght + 1, dataList.get(i));
            }
            //renderer.addXTextLabel(seriesItemLenght + 1, xKeduValue);
            xkedu[seriesItemLenght] = xKeduValue;
        } else {
            // 将x,y数值缓存
            for (int i = 0; i < seriesItemLenght - 1; i++) {
                for (int j = 0; j < titles.length; j++) {
                    catchList.get(j)[i] = (float) seriesList.get(j).getY(i + 1);
                }
                xkedu[i] = xkedu[i + 1];
            }
            // 移除旧点
            for (int i = 0; i < titles.length; i++) {
                seriesList.get(i).clear();
            }
            // 添加新点,变换坐标
            double maxy1[] = new double[3];
            double miny1[] = new double[3];
            for (int i = 0; i < seriesItemLenght - 1; i++) {
                for (int j = 0; j < titles.length; j++) {
                    seriesList.get(j).add(i + 1, catchList.get(j)[i]);
                    //获取最大最小值
                    maxy1[j] = seriesList.get(j).getMaxY();
                    miny1[j] = seriesList.get(j).getMinY();

                }
                //renderer.addXTextLabel(i + 1, xkedu[i]);
            }
            double maxY = Compare.max(maxy1, 3);
            double minY = Compare.min(miny1, 3);
            xkedu[XNUM - 1] = xKeduValue;
            for (int i = 0; i < titles.length; i++) {
                seriesList.get(i).add(XNUM, dataList.get(i));
            }
            mRenderer.setYAxisMin(minY - 3);// Y最小值
            mRenderer.setYAxisMax(maxY + 3);// Y最小值
        }
        for (int i = 0; i < titles.length; i++) {
            dataset.addSeries(seriesList.get(i));
        }
        mChartView.invalidate();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.project.moli.demobroad.MyService");
        EquipmentDataActivity.this.registerReceiver(receiver, filter);

        setStatus(0);

        initview();
        createView();
    }

    private void createView() {
        mChartView = ChartFactory.getCubeLineChartView(this, getDataSet(), getRender(), 0.3f);
        Char1.removeAllViews();
        Char1.addView(mChartView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public XYMultipleSeriesDataset getDataSet() {
        dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, int i) {
        for (String title : titles) {
            XYSeries series = new XYSeries(title, i);
            seriesList.add(series);
            dataset.addSeries(series);
        }
    }

    public XYMultipleSeriesRenderer getRender() {
        mRenderer = new XYMultipleSeriesRenderer();
        setRenderer(mRenderer, colors);
        mRenderer.setPointSize(5.5f);
        int yMin = -5;
        int yMax = 15;
        String chartLineTitle = this.getString(R.string.ownchar1);
        setChartSettings(mRenderer, chartLineTitle, "", null/*yMessage*/, 0.0,
                yMin, yMax, Color.BLACK, Color.BLACK);// 设置图表的X轴，Y轴,标题
        mRenderer.setXLabels(0);// 取消x轴的数字,动态设置
        mRenderer.setYLabels(10);// Y轴均分10项
        mRenderer.setShowGrid(true);// 显示表格
        mRenderer.setXLabelsAlign(Paint.Align.RIGHT);// 右对齐
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setZoomButtonsVisible(false);// 不显示放大缩小
        mRenderer.setClickEnabled(true);// 不允许放大或缩小
        mRenderer.setPanEnabled(false, false);// 上下左右都不可以移动
        mRenderer.setBarSpacing(0.5);
        return mRenderer;
    }

    // 图表样式设置
    protected void setChartSettings(XYMultipleSeriesRenderer renderer,
                                    String title, String xTitle, String yTitle, double xMin,
                                    double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);// X轴标题
        renderer.setYTitle("加速度");// Y轴标题
        renderer.setXAxisMin(xMin);// X最小值
        renderer.setYAxisMin(yMin);// Y最小值
        renderer.setYAxisMax(yMax);// Y最小值
        renderer.setAxesColor(axesColor);// 轴的颜色
        renderer.setLabelsColor(labelsColor);// 标题的颜色
    }


    /**
     * 设置描绘器属性
     */
    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors) {
        renderer.setAxisTitleTextSize(40);//设置轴标题的大小
        renderer.setChartTitleTextSize(40);//设置图标标题的大小
        renderer.setLabelsTextSize(40);//轴刻度文字大小
        renderer.setLegendTextSize(30);//图例文字的大小
        //renderer.setPointSize(5f);
        renderer.setMargins(new int[]{0, 80, 0, 0});// 上,左,下,右
        renderer.setMarginsColor(Color.argb(0, 255, 255, 255));//设置图标边框的颜色

        for (int i = 0; i < titles.length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setDisplayChartValues(false);//设置是否显示线上点的值
            r.setLineWidth(2f);// 宽度
            r.setFillPoints(true);// 完全填充
            r.setChartValuesSpacing(0);
            renderer.addSeriesRenderer(r);
        }
    }

    /**
     * 初始化图表
     */
    @SuppressLint("SimpleDateFormat")
    private void initview() {
        timer = new Timer();
        Float[] catchOne = new Float[XNUM];
        Float[] catchTwo = new Float[XNUM];
        Float[] catchThree = new Float[XNUM];
        xkedu = new String[XNUM];
        dataList = new ArrayList<>();
        nowTime = new SimpleDateFormat("hh-mm-ss");
        seriesList = new ArrayList<>();
        catchList = new ArrayList<>();
        catchList.add(catchOne);
        catchList.add(catchTwo);
        catchList.add(catchThree);
    }

    @Override
    protected void laodData() {

    }

    /**
     * 菜单显示
     *
     * @param view
     */
    @OnClick(R.id.control_more)
    void showMoreOption(View view) {
        right_menu.setVisibility(View.VISIBLE);
    }

    /**
     * 菜单隐藏
     *
     * @param view
     */
    @OnClick(R.id.part_right_menu_back)
    void hide(View view) {
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 参数设置
     *
     * @param view
     */
    @OnClick(R.id.tel_cable_setting)
    void setCable(View view) {
        startAcitvity(CableSettingActivity.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 测量参数
     *
     * @param view
     */
    @OnClick(R.id.tel_measure_setting)
    void setConfig(View view) {
        startAcitvity(MeasureConfigSetting.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 分享
     *
     * @param view
     */
    @OnClick(R.id.tel_data_share)
    void share(View view) {
        startAcitvity(ShareActivtiy.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 测量 点击
     */
    @OnClick(R.id.controler_start)
    void start() {
        mFile = new File(getCacheDir(), mMac + ".csv");
        boolean delete = mFile.delete();
        Toast.makeText(this, "" + delete, Toast.LENGTH_SHORT).show();

        mFile = new File(getCacheDir(), mMac + ".csv");
        Start_time = System.currentTimeMillis();
        limit_Time = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureTime, 10);
        End_time = limit_Time * 1000 + Start_time;
        setStatus(1);
    }

    /**
     * 暂停 点击
     */
    @OnClick(R.id.controler_pause)
    void pause() {
        if (mFile != null && System.currentTimeMillis() < End_time) {
            if (mFile.isFile() && mFile.exists()) {
                mFile.delete();
            }
        }
        setStatus(2);
    }

    /**
     * 停止 点击
     */
    @OnClick(R.id.controler_stop)
    void stop() {
        if (mFile != null && System.currentTimeMillis() < End_time) {
            if (mFile.isFile() && mFile.exists()) {
                mFile.delete();
            }
        }
        setStatus(0);
    }

    /**
     * 设置状态图表
     *
     * @param status
     */
    private void setStatus(int status) {
        switch (status) {
            /**
             * 测量状态 0: 已停止 1.测量中 2.已暂停
             */
            case 0:
                status_flag = STOP;
                status_tv.setText("已停止");
                status_iv.setImageResource(R.drawable.tz_two);
                status_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                status_flag = START;
                status_tv.setText("测量中");
                status_iv.setImageResource(R.drawable.ks);
                status_tv.setTextColor(Color.rgb(255, 0, 0));
                break;
            case 2:
                status_flag = PAUSE;
                status_tv.setText("已暂停");
                status_iv.setImageResource(R.drawable.zt_two);
                status_tv.setTextColor(Color.rgb(0, 89, 255));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EquipmentDataActivity.this.unregisterReceiver(receiver);

    }
}
