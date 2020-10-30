package com.example.infs3605team3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.math.BigDecimal;

public class SpaceRatingBar extends LinearLayout {

    private boolean mClickable;

    private int starCount;

    private OnRatingChangeListener onRatingChangeListener;

    private float starImageSize;

    private float starPadding;

    private float starStep;

    private Drawable starEmptyDrawable;

    private Drawable starFillDrawable;

    private Drawable starHalfDrawable;

    private StepSize stepSize;

    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }


    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }


    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }


    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }


    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setStepSize(StepSize stepSize) {
        this.stepSize = stepSize;
    }

    public SpaceRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        starImageSize = mTypedArray.getDimension(R.styleable.RatingBar_starImageSize, 20);
        starPadding = mTypedArray.getDimension(R.styleable.RatingBar_starPadding, 10);
        starStep = mTypedArray.getFloat(R.styleable.RatingBar_starStep, 1.0f);
        stepSize = StepSize.fromStep(mTypedArray.getInt(R.styleable.RatingBar_stepSize, 1));
        starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        starFillDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starFill);
        starHalfDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starHalf);
        mClickable = mTypedArray.getBoolean(R.styleable.RatingBar_clickable, true);
        mTypedArray.recycle();
        for (int i = 0; i < starCount; ++i) {
            final ImageView imageView = getStarImageView();
            imageView.setImageDrawable(starEmptyDrawable);
            imageView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mClickable) {
                                //浮点数的整数部分
                                int fint = (int) starStep;
                                BigDecimal b1 = new BigDecimal(Float.toString(starStep));
                                BigDecimal b2 = new BigDecimal(Integer.toString(fint));
                                //浮点数的小数部分
                                float fPoint = b1.subtract(b2).floatValue();
                                if (fPoint == 0) {
                                    fint -= 1;
                                }

                                if (indexOfChild(v) > fint) {
                                    setStar(indexOfChild(v) + 1);
                                } else if (indexOfChild(v) == fint) {
                                    if (stepSize == StepSize.Full) {//如果是满星 就不考虑半颗星了
                                        return;
                                    }
                                    //点击之后默认每次先增加一颗星，再次点击变为半颗星
                                    if (imageView.getDrawable().getCurrent().getConstantState().equals(starHalfDrawable.getConstantState())) {
                                        setStar(indexOfChild(v) + 1);
                                    } else {
                                        setStar(indexOfChild(v) + 0.5f);
                                    }
                                } else {
                                    setStar(indexOfChild(v) + 1f);
                                }

                            }

                        }
                    }
            );
            addView(imageView);
        }
        setStar(starStep);
    }

    /**
     * 设置每颗星星的参数
     *
     * @return
     */
    private ImageView getStarImageView() {
        ImageView imageView = new ImageView(getContext());

        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                Math.round(starImageSize), Math.round(starImageSize));//设置每颗星星在线性布局的大小
        layout.setMargins(0, 0, Math.round(starPadding), 0);//设置每颗星星在线性布局的间距
        imageView.setLayoutParams(layout);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setMinimumWidth(10);
        imageView.setMaxHeight(10);
        return imageView;

    }


    /**
     * 设置星星的个数
     *
     * @param rating
     */

    public void setStar(float rating) {

        if (onRatingChangeListener != null) {
            onRatingChangeListener.onRatingChange(rating);
        }
        this.starStep = rating;
        //浮点数的整数部分
        int fint = (int) rating;
        BigDecimal b1 = new BigDecimal(Float.toString(rating));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        //浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();

        //设置选中的星星
        for (int i = 0; i < fint; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        //设置没有选中的星星
        for (int i = fint; i < starCount; i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
        //小数点默认增加半颗星
        if (fPoint > 0) {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);
        }
    }

    /**
     * 操作星星的点击事件
     */
    public interface OnRatingChangeListener {
        /**
         * 选中的星星的个数
         *
         * @param
         */
        void onRatingChange(float ratingCount);

    }

    /**
     * 星星每次增加的方式整星还是半星，枚举类型
     * 类似于View.GONE
     */
    public enum StepSize {
        Half(0), Full(1);
        int step;

        StepSize(int step) {
            this.step = step;
        }

        public static StepSize fromStep(int step) {
            for (StepSize f : values()) {
                if (f.step == step) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
