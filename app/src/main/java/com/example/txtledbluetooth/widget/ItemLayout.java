package com.example.txtledbluetooth.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.txtledbluetooth.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public class ItemLayout extends RelativeLayout {
    @BindView(R.id.layout_item)
    RelativeLayout layoutItem;
    @BindView(R.id.iv_item_left)
    ImageView ivLeft;
    @BindView(R.id.tv_left_top)
    TextView tvLeftTop;
    @BindView(R.id.tv_left_bottom)
    TextView tvLeftBottom;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_item_right)
    ImageView ivRight;
    private Context mContext;
    public OnItemListener onItemListener;
    public OnIvRightListener onIvRightListener;

    public OnItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public OnIvRightListener getOnTvRightListener() {
        return onIvRightListener;
    }

    public void setOnTvRightListener(OnIvRightListener onIvRightListener) {
        this.onIvRightListener = onIvRightListener;
    }

    public interface OnItemListener {
        void onClickItemListener(View v);
    }

    public interface OnIvRightListener {
        void onClickIvRightListener(View v);
    }

    public ItemLayout(Context context) {
        this(context, null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable drawableLeft = null;
        Drawable drawableRight = null;
        String strLeftTop = "";
        String strLeftBottom = "";
        String strRight = "";
        int itemBgColor = 0;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ItemLayout,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ItemLayout_text_left_top:
                    strLeftTop = a.getString(attr);
                    break;
                case R.styleable.ItemLayout_text_left_bottom:
                    strLeftBottom = a.getString(attr);
                    break;
                case R.styleable.ItemLayout_text_right:
                    strRight = a.getString(attr);
                    break;
                case R.styleable.ItemLayout_iv_left:
                    drawableLeft = a.getDrawable(attr);
                    break;
                case R.styleable.ItemLayout_iv_right:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.ItemLayout_item_bg:
                    itemBgColor = a.getColor(attr, context.getResources().getColor(R.color.
                            content_bg));
                    break;
            }

        }
        a.recycle();
        init(context);
        tvLeftTop.setText(strLeftTop);
        if (TextUtils.isEmpty(strLeftBottom)) {
            tvLeftBottom.setVisibility(GONE);
        } else {
            tvLeftBottom.setVisibility(VISIBLE);
            tvLeftBottom.setText(strLeftBottom);
        }
        if (TextUtils.isEmpty(strRight)) {
            tvRight.setVisibility(GONE);
        } else {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(strRight);
        }
        if (drawableLeft == null) {
            ivLeft.setVisibility(GONE);
        } else {
            ivLeft.setVisibility(VISIBLE);
            ivLeft.setImageDrawable(drawableLeft);
        }
        if (drawableRight == null) {
            ivRight.setVisibility(GONE);
        } else {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageDrawable(drawableRight);
        }
        if (itemBgColor == 0) {
            layoutItem.setBackground(getResources().getDrawable(R.drawable.ripple_item_layout_effect));
        } else {
            layoutItem.setBackgroundColor(itemBgColor);
        }
    }

    public void setIsItemSelected(boolean isItemSelected) {
        if (isItemSelected) {
            layoutItem.setBackgroundColor(getResources().getColor(R.color.item_selected_bg));
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(R.mipmap.icon_selected);
        } else {
            layoutItem.setBackgroundColor(getResources().getColor(R.color.content_bg));
            ivRight.setVisibility(GONE);
        }
    }

    public void setTvRightStr(String showStr) {
        if (!TextUtils.isEmpty(showStr)) {
            tvRight.setText(showStr);
        }
    }

    public String getTvLeftStr() {
        return tvLeftTop.getText().toString();
    }

    public String getTvRightStr() {
        return tvRight.getText().toString();
    }

    public void init(Context c) {
        this.mContext = c;
        LayoutInflater.from(mContext).inflate(R.layout.item_layout, this, true);
        ButterKnife.bind(this);
        initListener();

    }

    private void initListener() {
        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.onClickItemListener(v);
                }
            }
        });
        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIvRightListener != null) {
                    onIvRightListener.onClickIvRightListener(v);
                }
            }
        });
    }
}
