package com.home.katinoluntandemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.home.katinoluntandemo.databinding.ActivityWelcomeBinding;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = "WelcomeActivity";
    private ActivityWelcomeBinding binding;
    private boolean alreadyImplemented = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        /** 處理耗時動畫 */
        int animationDuration = 3000;
        binding.circleProgressbar.setProgressWithAnimation(100, animationDuration);
        binding.circleProgressbar.setOnProgressbarChangeListener(new CircleProgressbar.OnProgressbarChangeListener() {
            @Override
            public void onProgressChanged(CircleProgressbar circleSeekbar, float progress, boolean fromUser) {
                if ((int) progress == 99) {
                    if (!alreadyImplemented) {
                        binding.colorImageView.setBackgroundColor(getColor(R.color.colorWhite));
                        binding.srcImageView.setImageResource(R.drawable.icon_welcome_green);
                        alreadyImplemented = true;
                    }
                }
                if (progress == 100) {
                    amplificationAnimation(binding.roundAnimatedImageFrameLayout);
                }
            }
            @Override
            public void onStartTracking(CircleProgressbar circleSeekbar) {
            }
            @Override
            public void onStopTracking(CircleProgressbar circleSeekbar) {
            }
        });
    }

    /**
     * 實現放大的動畫, 以及跳轉至廣告頁面
     */
    private void amplificationAnimation(final View view) {
        view.animate()
                .scaleX(1.12f)
                .scaleY(1.12f)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator())
                .start();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(400)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            }
        }, 600);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, AdvertisementActivity.class));
                finish();
            }
        }, 1100);
    }
}