package com.xxmassdeveloper.mpchartexample.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by xiufeng on 2017/11/6.
 */

public class MyHistogramView extends View {
    /**
     * 第一步：声明画笔
     */
    private Paint mPaint_X;//X坐标轴画笔
    private Paint mPaint_Y;//Y坐标轴画笔
    private Paint mPaint_InsideLine;//内部虚线P
    private Paint mPaint_Text;//字体画笔
    private Paint mPaint_TextXY;//字体画笔
    private Paint mPaint_Rec;//矩形画笔

    //数据
    private int[] data ;
    private int totalData;
    //视图的宽高
    private int width;
    private int height;


    //坐标轴数据
    private String[] mText_X ;

    private String[] mText_Y = new String[]{"","武汉", "十堰", "咸宁", "随州", "恩施", "宜昌", "孝感", "黄州", "恩施", "麻城", "红安", "新洲"};//默认Y轴坐标

    public MyHistogramView(Context context) {
        super(context);
        init(context,null);
    }

    public MyHistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 更新XY轴坐标
     */
    public void upDateTextForX(String[] text_X)
    {
        mText_X = text_X;
        this.postInvalidate();  //更新视图
    }

    /**
     * 更新数据
     */
    public void upData(int [] data,int totalData)
    {
        this.data = data;
        this.totalData = totalData;
        this.postInvalidate();  //更新视图
        mText_X = getText_X(data);
    }

    /**
     * 初始化画笔
     */
    private void init(Context context, AttributeSet attrs) {
        mPaint_X = new Paint();
        mPaint_InsideLine = new Paint();
        mPaint_Text = new Paint();
        mPaint_TextXY = new Paint();
        mPaint_Rec = new Paint();
        mPaint_Y = new Paint();


        mPaint_Y.setColor(Color.DKGRAY);
        mPaint_Y.setStrokeWidth(3);

        mPaint_X.setColor(Color.GRAY);

        mPaint_InsideLine.setColor(Color.LTGRAY);
        mPaint_InsideLine.setAntiAlias(true);


        mPaint_Text.setTextSize(25);
        mPaint_Text.setColor(Color.GREEN);
        mPaint_Text.setTextAlign(Paint.Align.CENTER);

        mPaint_TextXY.setTextSize(20);
        mPaint_TextXY.setColor(Color.DKGRAY);
        mPaint_TextXY.setTextAlign(Paint.Align.CENTER);

        mPaint_Rec.setColor(Color.RED);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        int leftHeight_Every = (width-50)/4; //Y轴每个数据的间距
        int downWeight_Every = (height-50)/mText_Y.length*2/3;//X轴每个数据的间距

        //画XY坐标轴
        canvas.drawLine(50, 50, width, 50, mPaint_X);
        canvas.drawLine(50, 78+ downWeight_Every * data.length + 12, 50, 50, mPaint_Y);
        //画X轴坐标
        for(int i =1;i<mText_X.length+1;i++){
            canvas.drawText(mText_X[mText_X.length-i], 50 + leftHeight_Every * i,30, mPaint_TextXY);
        }

        if(this.data != null && this.data.length >0){
            //画Y轴坐标
            for(int i =1;i<mText_Y.length+1;i++){
                canvas.drawText(mText_Y[i-1],20,downWeight_Every*(i-1)+60,mPaint_TextXY);
            }
            //画矩形
            DecimalFormat df   = new DecimalFormat("######0.00");
            for(int i =1;i<data.length+1;i++){
                int data_Y_One = Integer.parseInt(mText_X[3]); //Y轴首个数值

                double data_Yx = (double)data[i-1]/data_Y_One;
                RectF rect = new RectF();
                rect.left  = 50;
                rect.right = (float)(data_Yx*leftHeight_Every+50);
                rect.top =50+ downWeight_Every * i  - 12;
                rect.bottom = 50+ downWeight_Every * i + 12;

                canvas.drawRoundRect(rect, 5, 5,mPaint_Rec);
                canvas.drawText(data[i - 1] + ",  "+(df.format((double)data[i - 1]/totalData*100))+"%", (float) (70 +(data_Yx*leftHeight_Every+50)),downWeight_Every*i+60 , mPaint_Text);
            }
        }

    }



    /**
     * 获取一组数据的最大值
     */
    public static int getMax(int[] arr) {
        int max = arr[0];
        for(int x=1;x<arr.length;x++)
        {
            if(arr[x]>max)
                max = arr[x];
        }
        return max;
    }

    /**
     * 功能：根据传入的数据动态的改变Y轴的坐标
     * 返回：取首数字的前两位并除以2，后面变成0。作为Y轴的基坐标
     */
    public static String[] getText_X(int[] data){
        String[] text_X;
        int textX = 0;

        String numMax = getMax(data)+"";
        char[] charArray = numMax.toCharArray();
        int dataLength = charArray.length;//数据长度 eg：5684：4位
        String twoNumString = "";
        if(dataLength >= 2){
            for (int i = 0; i < 2; i++) {
                twoNumString += charArray[i];
            }
            int twoNum =Integer.parseInt(twoNumString);
            textX = (int) Math.ceil(twoNum/3);
            //将数据前两位后加上“0” 用于返回前两位的整数
            if(dataLength - 2 == 1){
                textX *= 10;
            }else if(dataLength -2 == 2){
                textX *= 100;
            }else if(dataLength -2 == 3){
                textX *= 1000;
            }else if(dataLength -2 == 4){
                textX *= 10000;
            }else if(dataLength -2 == 5){
                textX *= 100000;
            }
            text_X = new String[]{"", textX * 3 + "", textX * 2 + "", textX + ""};

        }else{
            text_X = new String[]{"", 15+"",10+"",5+""};
        }
        return text_X;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            width = widthSpecSize;
        } else {
            width = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            height = dipToPx(400);
        } else {
            height = heightSpecSize;
        }
        setMeasuredDimension(width, height);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
