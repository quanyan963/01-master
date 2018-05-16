
package com.example.txtledbluetooth.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import com.wx.wheelview.util.WheelUtils;

public class WheelItem extends FrameLayout {
    private ImageView mImage;
    private TextView mText;

    public WheelItem(Context context) {
        super(context);
        this.init();
    }

    public WheelItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public WheelItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @SuppressLint("WrongConstant")
    private void init() {
        LinearLayout layout = new LinearLayout(this.getContext());
        LayoutParams layoutParams = new LayoutParams(-1, WheelUtils.dip2px(this.getContext(), 45.0F));
        layout.setOrientation(0);
        layout.setPadding(20, 20, 20, 20);
        layout.setGravity(17);
        this.addView(layout, layoutParams);
        this.mImage = new ImageView(this.getContext());
        this.mImage.setTag(Integer.valueOf(100));
        this.mImage.setVisibility(8);
        LayoutParams imageParams = new LayoutParams(-2, -2);
        imageParams.rightMargin = 20;
        layout.addView(this.mImage, imageParams);
        this.mText = new TextView(this.getContext());
        this.mText.setTag(Integer.valueOf(101));
        this.mText.setEllipsize(TruncateAt.END);
        this.mText.setSingleLine();
        this.mText.setIncludeFontPadding(false);
        this.mText.setGravity(17);
        this.mText.setTextColor(-16777216);
        LayoutParams textParams = new LayoutParams(-1, -1);
        layout.addView(this.mText, textParams);
    }

    public void setText(CharSequence text) {
        this.mText.setText(text);
    }

    @SuppressLint("WrongConstant")
    public void setImage(int resId) {
        this.mImage.setVisibility(0);
        this.mText.setVisibility(GONE);
        this.mImage.setImageResource(resId);
    }
}
