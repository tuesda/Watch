package com.tuesda.watch.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by zhanglei on 15/8/1.
 */
public class AnimatorHelp {
    public static void btnClick(View view, int duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f, 1f);
        AnimatorSet animation = new AnimatorSet();
        animation.play(scaleX).with(scaleY);
        animation.setDuration(duration);
        animation.start();
    }
}
