package com.example.infs3605team3.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.infs3605team3.R;


public class FlowerAttrView extends AppCompatTextView {
    private String attributeValue;

    public FlowerAttrView(Context context) {
        super(context);
        init(context);
    }

    public FlowerAttrView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlowerAttrView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.flower_attr_bg);
        setTextColor(Color.parseColor("#2D4811"));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
        setSingleLine();
        setGravity(Gravity.CENTER);
         setPadding(dp2px(context, 4),0, dp2px(context, 4), 0);

        setMaxWidth(getScreenWidth(context));
    }
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }


    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = getScreenDensity(context);
        return (int) (dipValue * scale + 0.5);
    }
    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
        setText(attributeValue);
    }
}

