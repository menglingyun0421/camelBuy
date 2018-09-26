package com.lingyun.camelprocurementservice.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.lingyun.camelprocurementservice.R;

public class HistogramView extends View {

	private Paint xLinePaint;// 坐标轴 轴线 画笔：
	private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
	private Paint titlePaint;// 绘制文本的画笔
	private Paint paint;// 矩形画笔 柱状图的样式信息
	private int[] progress = { 2000, 5000, 6000, 8000, 500, 6000, 9000 };// 7
																			// 条，显示各个柱状的数据
	private int[] aniProgress;// 实现动画的值
	private final int TRUE = 0;// 在柱状图上显示数字 1不显示，非1显示
	private int[] text;// 设置点击事件，显示哪一条柱状的信息
	private Bitmap bitmap;
	// 坐标轴左侧的数标
	private String[] ySteps;
	// 坐标轴底部的星期数
	private String[] xWeeks;
	private int flag;// 是否使用动画

	private HistogramAnimation ani;

	public HistogramView(Context context) {
		super(context);
		init();
	}

	public HistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		ySteps = new String[] { "10k", "7.5k", "5k", "2.5k", "0" };
		xWeeks = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
		text = new int[] { 0, 0, 0, 0, 0, 0, 0 ,0};
		aniProgress = new int[] { 0, 0, 0, 0, 0, 0, 0 ,0};
		ani = new HistogramAnimation();
		ani.setDuration(2000);

		xLinePaint = new Paint();
		hLinePaint = new Paint();
		titlePaint = new Paint();
		paint = new Paint();

		// 给画笔设置颜色
		xLinePaint.setColor(Color.DKGRAY);
		hLinePaint.setColor(Color.LTGRAY);
		titlePaint.setColor(Color.BLACK);

		// 加载画图
		bitmap = BitmapFactory
				.decodeResource(getResources(), R.mipmap.column);
	}

	//这个是后添加的方法，目的是动态设置柱状图的数量
	public void setData (String[] ySteps,String[] xWeeks,int [] progress,int [] text,int [] aniProgress){
		this.ySteps=ySteps;
		this.xWeeks=xWeeks;
		this.progress=progress;
		this.text=text;
		this.aniProgress=aniProgress;
	}

	public void start(int flag) {
		this.flag = flag;
		this.startAnimation(ani);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight() - dp2px(50);
		// 绘制底部的线条
		canvas.drawLine(dp2px(30), height + dp2px(3), width - dp2px(30), height
				+ dp2px(3), xLinePaint);

		int leftHeight = height - dp2px(5);// 左侧外周的 需要划分的高度：

		int hPerHeight = leftHeight / 4;// 分成四部分

		hLinePaint.setTextAlign(Align.CENTER);
		// 设置四条虚线
		for (int i = 0; i < 4; i++) {
			canvas.drawLine(dp2px(30), dp2px(10) + i * hPerHeight, width
					- dp2px(30), dp2px(10) + i * hPerHeight, hLinePaint);
		}

		// 绘制 Y 周坐标
		titlePaint.setTextAlign(Align.RIGHT);
		titlePaint.setTextSize(sp2px(12));
		titlePaint.setAntiAlias(true);
		titlePaint.setStyle(Paint.Style.FILL);
		// 设置左部的数字
		for (int i = 0; i < ySteps.length; i++) {
			canvas.drawText(ySteps[i], dp2px(25), dp2px(13) + i * hPerHeight,
					titlePaint);
		}

		// 绘制 X 周 做坐标
		int xAxisLength = width - dp2px(30);
		int columCount = xWeeks.length + 1;
		int step = xAxisLength / columCount;

		// 设置底部的数字
		for (int i = 0; i < columCount - 1; i++) {
			// text, baseX, baseY, textPaint
			canvas.drawText(xWeeks[i], dp2px(25) + step * (i + 1), height
					+ dp2px(20), titlePaint);
		}

		// 绘制矩形
		if (aniProgress != null && aniProgress.length > 0) {
			for (int i = 0; i < aniProgress.length; i++) {// 循环遍历将7条柱状图形画出来
				int value = aniProgress[i];
				paint.setAntiAlias(true);// 抗锯齿效果
				paint.setStyle(Paint.Style.FILL);
				paint.setTextSize(sp2px(15));// 字体大小
				paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
				Rect rect = new Rect();// 柱状图的形状

				rect.left = step * (i + 1);
				rect.right = dp2px(30) + step * (i + 1);//right设置为30则建议最多八条柱状图，如果该值减小，可以增加柱状图数量。
				int rh = (int) (leftHeight - leftHeight * (value / 10000.0));
				rect.top = rh + dp2px(10);
				rect.bottom = height;

				canvas.drawBitmap(bitmap, null, rect, paint);
				// 是否显示柱状图上方的数字
				if (this.text[i] == TRUE) {
					canvas.drawText(value + "", dp2px(15) + step * (i + 1)
							- dp2px(15), rh + dp2px(5), paint);
				}

			}
		}

	}

	private int dp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().density;
		return (int) (v * value + 0.5f);
	}

	private int sp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (v * value + 0.5f);
	}

	/**
	 * 设置点击事件，是否显示数字
	 */
//	public boolean onTouchEvent(MotionEvent event) {
//		int step = (getWidth() - dp2px(30)) / 9;//如果是八跳柱状图则除以9（划分出八个区域），7条柱状图则初一8，以此类推
//		int x = (int) event.getX();
//		for (int i = 0; i < 8; i++) {//i=想要显示柱状图的数量
//			if (x > (dp2px(15) + step * (i + 1) - dp2px(15))
//					&& x < (dp2px(15) + step * (i + 1) + dp2px(15))) {
//				text[i] = 1;
//				for (int j = 0; j < 8; j++) {//j=想要显示柱状图的数量
//					if (i != j) {
//						text[j] = 0;
//					}
//				}
//				if (Looper.getMainLooper() == Looper.myLooper()) {
//					invalidate();
//				} else {
//					postInvalidate();
//				}
//			}
//		}
//		return super.onTouchEvent(event);
//	}

	/**
	 * 集成animation的一个动画类
	 * 
	 * @author 李垭超
	 */
	private class HistogramAnimation extends Animation {
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f && flag == 2) {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = (int) (progress[i] * interpolatedTime);
				}
			} else {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = progress[i];
				}
			}
			invalidate();
		}
	}

}