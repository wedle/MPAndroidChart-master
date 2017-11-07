package com.xxmassdeveloper.mpchartexample;

import android.os.Bundle;
import com.xxmassdeveloper.mpchartexample.custom.HistogramView;
import com.xxmassdeveloper.mpchartexample.custom.MyHistogramView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

public class MyChatActivity  extends DemoBase {

    private MyHistogramView histogramView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);
        initView();
        initData();
    }

    private void initView() {
        histogramView = (MyHistogramView) findViewById(R.id.my_histogramView);
    }

    private void initData() {
        int arr[] = {110,10,50,30,40,140,60,80,100,120,130,150};//定义一个需要累加的数组
        int sum=0;//定义一个变量
        for(int i=0;i<arr.length;i++){
            sum = sum+arr[i];//通过for循环，去除数组中的元素，累加到sum中
        }
        histogramView.upData(arr,sum);

    }
}
