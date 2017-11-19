package com.zyuco.peachgarden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class UnlockingActivity extends AppCompatActivity {
    final UnlockingActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlocking);
        animation();
    }

    private void animation() {
        final int DURATION = 3000;
        final int OFFSET = 1000;

        int offset = 0;
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setStartOffset(offset);
        fadeIn.setDuration(DURATION);
        findViewById(R.id.unlocking_poem_1).startAnimation(fadeIn);
        offset += OFFSET;

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setStartOffset(offset);
        fadeIn.setDuration(DURATION);
        findViewById(R.id.unlocking_poem_2).startAnimation(fadeIn);
        offset += OFFSET;

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setStartOffset(offset);
        fadeIn.setDuration(DURATION);
        findViewById(R.id.unlocking_poem_3).startAnimation(fadeIn);
        offset += OFFSET;

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setStartOffset(offset);
        fadeIn.setDuration(DURATION);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(context, UnlockSuccessActivity.class);
                startActivity(intent);
                context.finish();
                context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        findViewById(R.id.unlocking_poem_4).startAnimation(fadeIn);
    }
}
