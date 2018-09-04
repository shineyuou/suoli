package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.My.Compare;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.Utils.ConstantsSP;

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


public class TelMeasure extends BaseActivity {
    private static final String TAG = "TelMeasure";
    private static final int START = 1, PAUSE = 2, STOP = 0;
    private int status_flag;


    private static final int HZ_50 = 1, HZ_200 = 0;
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

    private android.hardware.SensorManager SensorManager;
    private android.hardware.Sensor Sensor;
    private MyListener Listener;
    private XYMultipleSeriesDataset mDataSet;
    private XYMultipleSeriesRenderer mRender;

    private Integer limit_Time;
    private int Count = 0;
    private File mfile;

    private String filename;

    @BindView(R.id.item_status_iv)
    ImageView status_iv;

    @BindView(R.id.char1)
    LinearLayout Char1;

    @BindView(R.id.item_status_tv)
    TextView status_tv;

    @SuppressLint("HandlerLeak")
    private Handler set_status_tv = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (status_flag) {
                case START:
                    if (msg.what == (limit_Time * 200)) {
                        SensorManager.unregisterListener(Listener, Sensor);
                        setStatus(2);
                    }
                    updatechart();
                default:
                    break;
            }
        }
    };


    /**
     * 更新数据
     */
    private void updatechart() {
        float dataOne = light[0];
        float dataTwo = light[1];
        float dataThree = light[2];
        dataList.clear();
        dataList.add(dataOne);
        dataList.add(dataTwo);
        dataList.add(dataThree);
        String xKeduValue = nowTime.format(new java.util.Date());
        Log.i(TAG, "" + xKeduValue);
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

    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;


    @Override
    protected int getLayout() {
        return R.layout.activity_tel_measure;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("手机测量").setBack(true).setLeftTitle("返回").setRightViewId(R.layout.part_title_right_analyze);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        setStatus(0);
        right_menu.setVisibility(View.GONE);
        SensorManager = (android.hardware.SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor = SensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
        Listener = new MyListener();

        initview();
        createView();

    }

    private void createView() {
        mChartView = ChartFactory.getCubeLineChartView(this, getDataSet(), getRender(), 0.3f);
        Char1.removeAllViews();
        Char1.addView(mChartView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
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

    public class MyListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Message msg = new Message();
            light = sensorEvent.values;
            String s = sensorEvent.timestamp + "," + String.valueOf(light[0]) + "," + String.valueOf(light[1])
                    + "," + String.valueOf(light[2]) + "\n";
            try {
                FileOutputStream fos = new FileOutputStream(mfile, true);
                fos.write(s.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Count++;
            msg.what = Count;
            set_status_tv.sendMessage(msg);
        }

        @Override
        public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {
        }
    }

    @Override
    protected void laodData() {

    }

    //点击索力分析
    @OnClick(R.id.right_view)
    void back(View view) {
        startAcitvity(SelectCableActivity.class,mfile.getPath());
    }

    //开始
    @OnClick(R.id.controler_start)
    void start(View view) {
        limit_Time = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureTime, 10);
        filename = nowTime.format(new java.util.Date());
        mfile = new File(Environment.getExternalStorageDirectory() + "/1/手机", filename + ".csv");
        SensorManager.registerListener(Listener, Sensor, HZ_200);
        setStatus(1);
    }

    //暂停
    @OnClick(R.id.controler_pause)
    void pause(View view) {
        SensorManager.unregisterListener(Listener, Sensor);
        if (filename!=null&&Count!=(limit_Time * 200)){
            mfile = new File(Environment.getExternalStorageDirectory() + "/1/手机", filename + ".csv");
            if (mfile.isFile() && mfile.exists()) {
                mfile.delete();
                filename=null;
            }
        }
        setStatus(2);
    }

    //停止
    @OnClick(R.id.controler_stop)
    void stop(View view) {
        SensorManager.unregisterListener(Listener, Sensor);
        if (filename!=null&&Count!=(limit_Time * 200)){
            mfile = new File(Environment.getExternalStorageDirectory() + "/1/手机", filename + ".csv");
            if (mfile.isFile() && mfile.exists()) {
                mfile.delete();
                filename=null;
            }
        }
        setStatus(0);
    }

    //更多按钮
    @OnClick(R.id.control_more)
    void more() {
        right_menu.setVisibility(View.VISIBLE);
    }

    //隐藏更多界面
    @OnClick(R.id.part_right_menu_back)
    void hide(View view) {
        right_menu.setVisibility(View.GONE);
    }

    //桥索参数设置
    @OnClick(R.id.tel_cable_setting)
    void setCable(View view) {
        startAcitvity(CableSettingActivity.class);
        right_menu.setVisibility(View.GONE);
    }

    //测量参数设置
    @OnClick(R.id.tel_measure_setting)
    void setConfig(View view) {
        startAcitvity(MeasureConfigSetting.class,"false");
        right_menu.setVisibility(View.GONE);
    }

    //数据共享
    @OnClick(R.id.tel_data_share)
    void share(View view) {
        startAcitvity(ShareActivtiy.class);
        right_menu.setVisibility(View.GONE);
    }

    private void setStatus(int status) {
        switch (status) {
            /**
             * 测量状态 0: 已停止 1.测量中 2.已暂停
             */
            case STOP:
                status_flag = STOP;
                status_iv.setImageResource(R.drawable.tz_two);
                status_tv.setText("已停止");
                status_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case START:
                Count = 0;
                status_flag = START;
                status_iv.setImageResource(R.drawable.ks);
                status_tv.setText("测量中");
                status_tv.setTextColor(Color.rgb(255, 0, 0));
                break;
            case PAUSE:
                status_flag = PAUSE;
                status_iv.setImageResource(R.drawable.zt_two);
                status_tv.setText("已暂停");
                status_tv.setTextColor(Color.rgb(0, 89, 255));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SensorManager.unregisterListener(Listener, Sensor);
    }
}
