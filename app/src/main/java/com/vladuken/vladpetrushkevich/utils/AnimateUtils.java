package com.vladuken.vladpetrushkevich.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

public final class AnimateUtils {

    static final String TAG = "AnimateUtils";

    private AnimateUtils() {
    }

    public static void animateBackground(View view, int from, int to, int delay){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(delay); // milliseconds
        colorAnimation.addUpdateListener(
                animator -> view.setBackgroundColor((int) animator.getAnimatedValue())
        );
        colorAnimation.start();
    }

    public static void animateToGone(View v, int duration){
        v.animate()
                .alpha(0.0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setVisibility(View.GONE);
                    }
                });
    }

    public static void animateToVisible(View v, int duration){
        v.setVisibility(View.VISIBLE);
        v.animate()
                .alpha(1.0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.d(TAG,"Animation to visible ended");
                    }
                });
    }
}
