package com.annis.tensioncable.UI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.Toast;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.My.MyMarkerView;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.Utils.ConstantsSP;
import com.annis.tensioncable.model.TensionCable;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

public class AnalyzeActivity extends BaseActivity {
    private LineDataSet Set1;
    private ArrayList<Entry> Values=new ArrayList<>();
    /**
     * 上一页面被选中的拉索
     */
    TensionCable item;
    /**
     * 基频 内容
     */
    @BindView(R.id.n_et)
    TextView et_N;
    /**
     * 索力 内容
     */
    @BindView(R.id.hz_et)
    TextView et_Hz;

    @BindView(R.id.char2)
    LineChart mChart;
    private double mCsa;
    private double mDensity;
    private double mLength;
    private String mName;

    @Override
    protected int getLayout() {
        return R.layout.activity_analyze;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("索力分析").setBack(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        item = (TensionCable) getIntentObj();
        mCsa = item.getCSA();
        mDensity = item.getDensity();
        mLength = item.getLength();
        mName = item.getName();
        Integer frequency = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureFrequency, 200);

        Intent intent = getIntent();
        String filenamne = intent.getStringExtra("filepath");
        Toast.makeText(this,filenamne,Toast.LENGTH_LONG).show();

        initchart(mChart);
        //TODO 设置数据
        setContent("1000", "10000");

        updatachart();
    }

    private void initchart(LineChart char2) {
        mChart.setDrawGridBackground(false);//设置表格背景填充
        mChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("频谱");
        description.setPosition(100,100);
        mChart.setDescription(description);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        XAxis xAxis = mChart.getXAxis();
        //mChart.getAxisLeft().setAxisMaximum(1);
       // mChart.getAxisLeft().setAxisMinimum(0);
        xAxis.enableGridDashedLine(10f, 0f, 0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(5);//设置x轴的间隔
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getAxisRight().setEnabled(false);
        mChart.setHardwareAccelerationEnabled(true);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
    }

    @Override
    protected void laodData() {

    }

    private void updatachart() {
        /**
         * 添加数据
         */
        if (Values.isEmpty()){
            for (int i = 0; i < 512; i++) {
                double random = Math.random();
                Values.add(new Entry((float) (i*0.2), (float) random+12));
            }
        }

        /** mChart.getData():获取图标中线的数组
         * LineDataSet:一条线的数组
         *
         */
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            Set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);

            Set1.setValues(Values);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        } else {
            Set1 = new LineDataSet(Values, "dataset 1");
            Set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            //设置画图的线的格式，第一个参数，设置虚线中每个实线的长度；第二个参数，设置虚线中每个实线之间的间隔；
            Set1.enableDashedLine(1f,0f, 50f);
            Set1.enableDashedHighlightLine(10f, 5f, 0f);//设置十字线的格式
            Set1.setHighLightColor(Color.BLUE);
            Set1.setHighlightEnabled(false);
            Set1.setColor(Color.BLACK);
            Set1.setCircleColor(Color.BLACK);
            Set1.setLineWidth(0.5f);
            Set1.setCircleRadius(3f);
            Set1.setDrawCircles(false);//设置是否显示点
            Set1.setValueTextSize(1f);//设置显示点的值的字体大小
            Set1.setDrawValues(false);//设置是否显示点的值
            Set1.setDrawFilled(false);//线下是否填充
            Set1.setFormLineWidth(0.5f);//设置线宽
            Set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置线的圆滑
            Set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            //set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                Set1.setFillDrawable(drawable);
            } else {
                Set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(Set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
            mChart.getData().setHighlightEnabled(true);
        }
    }

    /**
     * 设置数据图下面的内容
     *
     * @param Hz
     * @param N
     */
    private void setContent(String Hz, String N) {
        et_Hz.setText(Hz);
        et_N.setText(N);
    }
}
