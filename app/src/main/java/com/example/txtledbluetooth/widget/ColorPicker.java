package com.example.txtledbluetooth.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPicker extends View {
    private int arrowPointerSize;
    private float[] colorHSV = {0.0F, 0.0F, 1.0F};
    private RectF colorPointerCoords;
    private Paint colorPointerOuterPaint;
    private Paint colorPointerPaint;
    private Paint colorViewPaint;
    private Path colorViewPath;
    private Bitmap colorWheelBitmap;
    private Paint colorWheelPaint;
    private int colorWheelRadius;
    private Matrix gradientRotationMatrix;
    private int innerPadding;
    private int innerWheelRadius;
    private RectF innerWheelRect;
    private Boolean isDown = Boolean.valueOf(false);
    private OnColorChangedListener mListener; // 小球移动的监听
    private int outerPadding;
    private int outerWheelRadius;
    private RectF outerWheelRect;
    private float pointerRadius;
    private Paint valuePointerPaint;
    private Paint valueSliderPaint;
    private Path valueSliderPath;
    private int valueSliderWidth;

    public ColorPicker(Context paramContext) {
        super(paramContext);
        init();
    }

    public ColorPicker(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public ColorPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private Bitmap createColorWheelBitmap(int paramInt1, int paramInt2) {
        Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
        int[] arrayOfInt = new int[13];
        float[] arrayOfFloat = {0.0F, 1.0F, 1.0F};
        for (int i = 0; ; i++) {
            int j = arrayOfInt.length;
            if (i >= j)
                break;
            arrayOfFloat[0] = ((180 + i * 30) % 360);
            arrayOfInt[i] = Color.HSVToColor(arrayOfFloat);
        }
        arrayOfInt[12] = arrayOfInt[0];
        SweepGradient localSweepGradient = new SweepGradient(paramInt1 / 2, paramInt2 / 2,
                arrayOfInt, null);
        ComposeShader localComposeShader = new ComposeShader(localSweepGradient, new RadialGradient(
                paramInt1 / 2, paramInt2 / 2, this.colorWheelRadius, -1,
                16777215, Shader.TileMode.CLAMP), PorterDuff.Mode.SRC_OVER);
        this.colorWheelPaint.setShader(localComposeShader);
        new Canvas(localBitmap).drawCircle(paramInt1 / 2, paramInt2 / 2, this.colorWheelRadius,
                this.colorWheelPaint);
        return localBitmap;
    }

    private void init() {
        this.colorPointerPaint = new Paint();
        this.colorPointerPaint.setStyle(Paint.Style.STROKE);
        this.colorPointerPaint.setStrokeWidth(4.0F);
        this.colorPointerPaint.setARGB(128, 0, 0, 0);
        this.colorPointerOuterPaint = new Paint();
        this.colorPointerOuterPaint.setStyle(Paint.Style.STROKE);
        this.colorPointerOuterPaint.setStrokeWidth(4.0F);
        this.colorPointerOuterPaint.setARGB(128, 255, 255, 255);
        this.valuePointerPaint = new Paint();
        this.valuePointerPaint.setStyle(Paint.Style.STROKE);
        this.valuePointerPaint.setStrokeWidth(2.0F);
        this.colorWheelPaint = new Paint();
        this.colorWheelPaint.setAntiAlias(true);
        this.colorWheelPaint.setDither(true);
        this.valueSliderPaint = new Paint();
        this.valueSliderPaint.setAntiAlias(true);
        this.valueSliderPaint.setDither(true);
        this.colorViewPaint = new Paint();
        this.colorViewPaint.setAntiAlias(true);
        this.colorViewPath = new Path();
        this.valueSliderPath = new Path();
        this.outerWheelRect = new RectF();
        this.innerWheelRect = new RectF();
        this.colorPointerCoords = new RectF();
    }

    public int getColor() {
        return Color.HSVToColor(this.colorHSV);
    }

    public float[] getColorHSV() {
        return this.colorHSV;
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas paramCanvas) {
        int i = getWidth() / 2;
        int j = getHeight() / 2;
        paramCanvas.drawBitmap(this.colorWheelBitmap, i - this.colorWheelRadius,
                j - this.colorWheelRadius, null);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setColor(Color.argb(255 - (int) (255.0F * this.colorHSV[2]), 0,
                0, 0));
        paramCanvas.drawCircle(i, j, this.colorWheelRadius, localPaint);
        this.colorViewPaint.setColor(Color.HSVToColor(this.colorHSV));
        paramCanvas.drawPath(this.colorViewPath, this.colorViewPaint);
        float f = (float) Math.toRadians(this.colorHSV[0]);
        int k = i + (int) (-Math.cos(f) * this.colorHSV[1] * (this.colorWheelRadius -
                this.pointerRadius));
        int m = j + (int) (-Math.sin(f) * this.colorHSV[1] * (this.colorWheelRadius -
                this.pointerRadius));
        int n = (int) (k - this.pointerRadius);
        int i1 = (int) (m - this.pointerRadius);
        this.colorPointerCoords.set(n, i1, n + 2.0F * this.pointerRadius,
                i1 + 2.0F * this.pointerRadius);
        paramCanvas.drawOval(this.colorPointerCoords, this.colorPointerPaint);
        this.colorPointerCoords.inset(-4.0F, -4.0F);
        paramCanvas.drawOval(this.colorPointerCoords, this.colorPointerOuterPaint);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        int i = Math.min(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
        setMeasuredDimension(i, i);
    }

    protected void onRestoreInstanceState(Parcelable paramParcelable) {
        if ((paramParcelable instanceof Bundle)) {
            Bundle localBundle = (Bundle) paramParcelable;
            this.colorHSV = localBundle.getFloatArray("color");
            super.onRestoreInstanceState(localBundle.getParcelable("super"));
            return;
        }
        super.onRestoreInstanceState(paramParcelable);
    }

    protected Parcelable onSaveInstanceState() {
        Bundle localBundle = new Bundle();
        localBundle.putFloatArray("color", this.colorHSV);
        localBundle.putParcelable("super", super.onSaveInstanceState());
        return localBundle;
    }

    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt1 / 2;
        int j = paramInt2 / 2;
        this.innerPadding = (0 / 100);
        this.outerPadding = (0 / 100);
        this.arrowPointerSize = (paramInt1 * 8 / 100);
        this.valueSliderWidth = (0 / 100);
        this.outerWheelRadius = (paramInt1 / 2 - this.outerPadding - this.arrowPointerSize);
        this.innerWheelRadius = (this.outerWheelRadius - this.valueSliderWidth);
        this.colorWheelRadius = (this.innerWheelRadius - this.innerPadding);
        this.pointerRadius = (0.075F * this.colorWheelRadius);
        this.outerWheelRect.set(i - this.outerWheelRadius, j - this.outerWheelRadius,
                i + this.outerWheelRadius, j + this.outerWheelRadius);
        this.innerWheelRect.set(i - this.innerWheelRadius, j - this.innerWheelRadius,
                i + this.innerWheelRadius, j + this.innerWheelRadius);
        this.colorWheelBitmap = createColorWheelBitmap(2 * this.colorWheelRadius,
                2 * this.colorWheelRadius);
        this.gradientRotationMatrix = new Matrix();
        this.gradientRotationMatrix.preRotate(270.0F, paramInt1 / 2, paramInt2 / 2);
        this.colorViewPath.arcTo(this.outerWheelRect, 270.0F, -180.0F);
        this.colorViewPath.arcTo(this.innerWheelRect, 90.0F, 180.0F);
        this.valueSliderPath.arcTo(this.outerWheelRect, 270.0F, 180.0F);
        this.valueSliderPath.arcTo(this.innerWheelRect, 90.0F, -180.0F);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        int i = paramMotionEvent.getAction();
        switch (i) {
            default:
                return super.onTouchEvent(paramMotionEvent);
            case 0:
            case 2:
                int j = (int) paramMotionEvent.getX();
                int k = (int) paramMotionEvent.getY();
                if (j<=150 && k<=150)
                    return super.onTouchEvent(paramMotionEvent);
                int m = j - getWidth() / 2;
                int n = k - getHeight() / 2;
                double d = Math.sqrt(m * m + n * n);
                if (d > this.colorWheelRadius - this.pointerRadius) {
                    if (i == 0)
                        return true;
                    d = this.colorWheelRadius - this.pointerRadius;
                }
                if (i == 0)
                    this.isDown = Boolean.valueOf(true);
                while (this.isDown.booleanValue()) {
                    this.colorHSV[0] = ((float) (180.0D + Math.toDegrees(Math.atan2(n, m))));
                    this.colorHSV[1] = Math.max(0.0F, Math.min(1.0F, (float) (d / (this.colorWheelRadius
                            - this.pointerRadius))));
                    invalidate();
                    return true;
                }
                return true;
            case 1:
        }
        this.isDown = Boolean.valueOf(false);
        if (this.mListener != null)
            this.mListener.onColorSelect(getColor());
        return true;
    }

    public void setBrightness(float paramFloat) {
        this.colorHSV[2] = paramFloat;
        invalidate();
    }

    public void setColor(int paramInt) {
        Color.colorToHSV(paramInt, this.colorHSV);
    }

    public void setPaintPixel(int colorInt) {
        Color.colorToHSV(colorInt, this.colorHSV);
        invalidate();

    }

    public void setColorHSV(float[] paramArrayOfFloat) {
        this.colorHSV = paramArrayOfFloat;
        invalidate();
    }

    // 颜色发生变化的回调接口
    public interface OnColorChangedListener {
        void onColorSelect(int color);
    }

    public void setOnColorSelectListener(OnColorChangedListener listener) {
        this.mListener = listener;
    }


}