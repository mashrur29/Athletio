package views;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import general.Day;
import utility.HashMapSort;

/**
 * Created by kashob on 10/23/17.
 */

public class ProgressLineChartDrawing {
    LineChart lineChart;
    HashMap<String, Integer> chartValue;

    public ProgressLineChartDrawing(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    public void Init() {
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawBorders(true);
    }

    public void chartUpdate(final HashMap<String, Integer> chartValue) {
        this.chartValue = chartValue;
        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Date: ");
        Map<String, Integer> sortedHashMap;
        sortedHashMap = new HashMapSort(this.chartValue).doSort();
        int counter = 0;
        for (Map.Entry<String, Integer> iterator : sortedHashMap.entrySet()) {
            counter++;
            Day day = new Day(iterator.getKey());
            xAxisLabel.add(day.day + "/" + day.month + "/" + day.year);
            entries.add(new Entry(counter, (float) iterator.getValue()));
        }
        LineDataSet dataset = new LineDataSet(entries, "Labels");
        final XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if ((int) value < xAxisLabel.size() && (int) value >= 0)
                    return xAxisLabel.get((int) value);
                return "";
            }

            public int getDecimalDigits() {
                return 0;
            }
        });
        LineData data = new LineData(dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawFilled(true);
        dataset.setCubicIntensity(.2f);
        dataset.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataset.setCircleColor(ColorTemplate.getHoloBlue());
        dataset.setLineWidth(2f);
        dataset.setCircleSize(2f);
        dataset.setFillAlpha(65);
        dataset.setHighLightColor(Color.rgb(244, 117, 177));
        dataset.setValueTextSize(10f);
        dataset.setValueTextColor(Color.BLACK);
        lineChart.setData(data);
        lineChart.animateY(500);
        lineChart.animateX(500);
        lineChart.invalidate();
    }
}
