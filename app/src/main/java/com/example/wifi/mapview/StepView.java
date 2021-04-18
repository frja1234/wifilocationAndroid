package com.example.wifi.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

public class  StepView extends View {
    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mArrowPath; // 箭头路径

    private int cR = 10;     // 圆点半径
    private int arrowR = 20; // 箭头半径

    public float mCurX;
    public float mCurY;

    private float stepX;
    private float stepY;


    private int mOrient;
    private Bitmap mBitmap;
    private List<PointF> mPointList = new ArrayList<>();//已经使用的点

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mStrokePaint = new Paint(mPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();

    }

    public float getStepX() {
        return stepX;
    }

    public void setStepX(float stepX) {
        this.stepX = mBitmap.getWidth()/stepX;
    }

    public float getStepY() {
        return stepY;
    }

    public void setStepY(float stepY) {
        this.stepY = mBitmap.getWidth()/stepY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) return;
        canvas.drawBitmap(mBitmap, new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), new Rect(0, 0, getWidth(), getHeight()), null); // 将mBitmap绘到canLock
        for (PointF p : mPointList) {
            canvas.drawCircle(p.x, p.y, cR, mPaint);
        }
        canvas.save();                  // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(mOrient);         // 转动画布
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaint);
        canvas.restore();               // 恢复画布
    }
    public void setImage(Bitmap bitmap){
        mBitmap = bitmap;
    }

    /**
     * 自动增加点
     */
    public void autoAddPoint(float stepLen) {
        mCurX += (float) (stepLen * Math.sin(Math.toRadians(mOrient)));
        mCurY += -(float) (stepLen * Math.cos(Math.toRadians(mOrient)));
        mPointList.add(new PointF(mCurX, mCurY));
        invalidate();
    }
    //得到X轴和Y轴坐标并刷新页面
    public void getCoord(float x,float y)
    {
        //this.mCurX=x*80+225;
        //this.mCurY=y*80+234;
        this.mCurX = stepX*x;
        this.mCurY = stepY*y;
        System.out.println(mCurX);
        System.out.println(mCurY);
        System.out.println("你好");
        postInvalidate();                //重新绘制UI
    }

    public void autoDrawArrow(int orient) {
        mOrient = orient;
        invalidate();
    }
}