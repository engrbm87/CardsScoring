package com.simon_eye.cardsScoring

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

/**
 * Created by rami_m on 5/25/2018.
 */

class MarginTransition(internal var mViewToTransform: View, internal var mNewRightMargin: Int, internal var mNewLeftMargin: Int) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val layoutParams = mViewToTransform.layoutParams as LinearLayout.LayoutParams
        if (mNewLeftMargin != 0)
            layoutParams.marginStart = (mNewLeftMargin * interpolatedTime).toInt()
        //            layoutParams.leftMargin = (int) (mNewLeftMargin * interpolatedTime);
        if (mNewRightMargin != 0)
            layoutParams.marginEnd = (mNewRightMargin * interpolatedTime).toInt()
        //            layoutParams.rightMargin = (int) (mNewRightMargin * interpolatedTime);
        mViewToTransform.layoutParams = layoutParams
    }
}
