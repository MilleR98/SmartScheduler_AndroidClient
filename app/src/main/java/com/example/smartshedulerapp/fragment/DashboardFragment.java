package com.example.smartshedulerapp.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

  @BindView(R.id.pieChart)
  PieChart pieChart;
  @BindView(R.id.switchLastWeek)
  Button switchLastWeek;
  @BindView(R.id.switchLastMonth)
  Button switchLastMonth;
  @BindView(R.id.switchLastHalfYear)
  Button switchLastHalfYear;
  @BindView(R.id.switchLastYear)
  Button switchLastYear;

  boolean isWeek;
  boolean isMonth;
  boolean isHalfYear;
  boolean isYear;

  public DashboardFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
    ButterKnife.bind(this, view);

    setSwitchBtnDisabled(switchLastWeek);
    setSwitchBtnDisabled(switchLastMonth);
    setSwitchBtnEnabled(switchLastHalfYear);
    setSwitchBtnDisabled(switchLastYear);

    drawChart();

    return view;
  }

  @OnClick(R.id.switchLastWeek)
  public void showLastWeekStatistic() {
      setSwitchBtnEnabled(switchLastWeek);
      setSwitchBtnDisabled(switchLastMonth);
      setSwitchBtnDisabled(switchLastHalfYear);
      setSwitchBtnDisabled(switchLastYear);
  }

  @OnClick(R.id.switchLastMonth)
  public void showLastMonthStatistic() {
    setSwitchBtnDisabled(switchLastWeek);
    setSwitchBtnEnabled(switchLastMonth);
    setSwitchBtnDisabled(switchLastHalfYear);
    setSwitchBtnDisabled(switchLastYear);
  }

  @OnClick(R.id.switchLastHalfYear)
  public void showLastHalfYearStatistic() {
    setSwitchBtnDisabled(switchLastWeek);
    setSwitchBtnDisabled(switchLastMonth);
    setSwitchBtnEnabled(switchLastHalfYear);
    setSwitchBtnDisabled(switchLastYear);
  }

  @OnClick(R.id.switchLastYear)
  public void showLastYearStatistic() {
    setSwitchBtnDisabled(switchLastWeek);
    setSwitchBtnDisabled(switchLastMonth);
    setSwitchBtnDisabled(switchLastHalfYear);
    setSwitchBtnEnabled(switchLastYear);
  }

  private void drawChart() {

    pieChart.setUsePercentValues(true);

    ArrayList<PieEntry> yvalues = new ArrayList<>();
    yvalues.add(new PieEntry(8f, "SPORT", 0));
    yvalues.add(new PieEntry(15f, "WORK", 1));
    yvalues.add(new PieEntry(12f, "FAMILY", 2));
    yvalues.add(new PieEntry(25f, "FRIENDS", 3));
    yvalues.add(new PieEntry(23f, "ENTERTAINMENT", 4));
    yvalues.add(new PieEntry(17f, "HOBIE", 5));

    PieDataSet dataSet = new PieDataSet(yvalues, "");
    PieData data = new PieData(dataSet);

    data.setValueFormatter(new PercentFormatter());
    pieChart.setData(data);
    pieChart.setDrawHoleEnabled(true);
    pieChart.setTransparentCircleRadius(40f);
    pieChart.setHoleRadius(40f);
    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
    data.setValueTextSize(13f);
    data.setValueTextColor(Color.DKGRAY);

  }

  private void setSwitchBtnDisabled(Button switchBtn) {
    GradientDrawable gradientDrawableForEventsBtn = (GradientDrawable) switchBtn.getBackground().mutate();
    gradientDrawableForEventsBtn.setColor(Color.TRANSPARENT);
    switchBtn.setTextColor(Color.WHITE);
  }

  private void setSwitchBtnEnabled(Button switchBtn) {
    GradientDrawable gradientDrawableForTasksBtn = (GradientDrawable) switchBtn.getBackground().mutate();
    gradientDrawableForTasksBtn.setColor(getResources().getColor(R.color.colorPrimary));
    switchBtn.setTextColor(Color.WHITE);
  }
}
