package com.simon_eye.cardsscoring;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by rami_m on 5/25/2018.
 */

public class MarginTransition extends Animation {

    View mViewToTransform;
    int mNewLeftMargin;
    int mNewRightMargin;

    public MarginTransition(View viewToTransform, int newRightMargin, int newLeftMargin) {
        mViewToTransform = viewToTransform;
        mNewRightMargin = newRightMargin;
        mNewLeftMargin = newLeftMargin;


    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mViewToTransform.getLayoutParams();
        if (mNewLeftMargin != 0)
            layoutParams.leftMargin = (int) (mNewLeftMargin * interpolatedTime);
        if (mNewRightMargin != 0)
            layoutParams.rightMargin = (int) (mNewRightMargin * interpolatedTime);
        mViewToTransform.setLayoutParams(layoutParams);
    }
}
