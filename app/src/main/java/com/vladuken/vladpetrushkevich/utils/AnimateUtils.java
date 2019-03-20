package com.vladuken.vladpetrushkevich.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

public class AnimateUtils {
    public static void animateBackground(View view, int from, int to, int delay){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(delay); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}
