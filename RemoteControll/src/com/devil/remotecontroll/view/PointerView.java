package com.devil.remotecontroll.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PointerView extends View{
	
	private float radius = 36.0f;

	private float curX;
	private float curY;
	
	public PointerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PointerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PointerView(Context context) {
		super(context);
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	

	public void setCurX(float curX) {
		this.curX = curX;
	}

	public void setCurY(float curY) {
		this.curY = curY;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		canvas.drawCircle(curX, curY, radius, paint);
	}
}
