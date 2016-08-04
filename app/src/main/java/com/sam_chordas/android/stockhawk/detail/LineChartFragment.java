package com.sam_chordas.android.stockhawk.detail;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.DateHigh;
import com.sam_chordas.android.stockhawk.data.DateHighMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LineChartFragment extends Fragment {

    private Typeface tf;
    static final ArrayList<Entry> xAxisValues= null;

    private static final String DESCRIBABLE_KEY = "describable_key";
    private DateHighMain mDescribable;
    List<String> upperLowerLimitList = new ArrayList<String>();
    private String upperLimit;
    private String lowerLimit;

    public LineChartFragment() {
        // Required empty public constructor
    }

    public static LineChartFragment newInstance(DateHighMain describable) {
        LineChartFragment fragment = new LineChartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

        mDescribable = (DateHighMain) getArguments().getSerializable(
                DESCRIBABLE_KEY);
        LineChart mChart = (LineChart) rootView.findViewById(R.id.chart);
        LineData data = new LineData(getXAxisValues(),getDataSet());

        setUpChart(mChart,data);

        return rootView;
    }

    private void setUpChart(LineChart mChart, LineData data) {
        mChart.setData(data);
        mChart.setDescription(getString(R.string.quote_history_text ) );
        mChart.setNoDataTextDescription(getString(R.string.quote_history_error));
        mChart.setDrawGridBackground(false);

        ArrayList<ILineDataSet> sets = (ArrayList<ILineDataSet>) mChart.getData()
                .getDataSets();

        for (ILineDataSet set : sets) {
                set.setDrawFilled(true);
        }
        // enable touch gestures
        mChart.setTouchEnabled(true);
        data.setDrawValues(true);
        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);

        mChart.setBackgroundColor(Color.BLACK);

        Legend l = mChart.getLegend();

        // modify the legend ...

        l.setForm(Legend.LegendForm.LINE);

        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);


        XAxis xAxis = mChart.getXAxis();

        xAxis.setEnabled(false);



        YAxis leftAxis = mChart.getAxisLeft();

        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(Float.parseFloat(Collections.max(upperLowerLimitList))+ 50f);
        leftAxis.setDrawGridLines(true);

        //  adding limit lines
        LimitLine ll1 = new LimitLine(Float.parseFloat(Collections.max(upperLowerLimitList)), Collections.max(upperLowerLimitList));
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setLineColor(Color.GREEN);

        LimitLine ll2 = new LimitLine(Float.parseFloat(Collections.min(upperLowerLimitList)), Collections.min(upperLowerLimitList));
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setLineColor(Color.RED);


        YAxis rightAxis = mChart.getAxisRight();

        rightAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        rightAxis.addLimitLine(ll1);
        rightAxis.addLimitLine(ll2);
        rightAxis.setDrawLimitLinesBehindData(true);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMaxValue(Float.parseFloat(Collections.max(upperLowerLimitList))+ 50f);
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        mChart.animateXY(2000, 2000);
        mChart.invalidate();
    }

    private ArrayList<ILineDataSet> getDataSet() {
        ArrayList<ILineDataSet> dataSets = null;

        ArrayList<Entry> valueSet = new ArrayList<>();
        int i=0;
        upperLowerLimitList.clear();
        for (DateHigh dh:mDescribable.datehigh
                ) {
            valueSet.add(new Entry(Float.parseFloat(dh.getQuoteHighValue()), i++));
            upperLowerLimitList.add(dh.getQuoteHighValue());
        }

        LineDataSet lineDataSet = new LineDataSet(valueSet, DetailActivity.symbol);
        lineDataSet.setColor(Color.rgb(0, 155, 0));

        dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        for (DateHigh dh:mDescribable.datehigh ) {
            xAxis.add("A");
        }
        return xAxis;
    }





}
