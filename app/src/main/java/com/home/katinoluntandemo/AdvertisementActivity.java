package com.home.katinoluntandemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementActivity extends AppCompatActivity {

    private final String TAG = "AdvertisementActivity";
    private NativeAd nativeAd;

    private LinearLayout nativeAdContainer, adView, bottomLinearLayout, nativeAdUnit;
    private Button reciprocalButton, nativeAdCallToAction;
    private ImageView reciprocalOverImageView;
    private AdIconView nativeAdIcon;
    private MediaView nativeAdMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        loadNativeAd();
    }

    private void loadNativeAd() {
        nativeAd = new NativeAd(this, "IMG_16_9_LINK#" + "Your_Placement_ID");
        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {   // Native ad finished downloading all assets
            }

            @Override
            public void onError(Ad ad, AdError adError) {    // Native ad failed to load
                Toast.makeText(AdvertisementActivity.this, "你有填入PlacementID?", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {  // Native ad is loaded and ready to be displayed
                if (nativeAd == null || nativeAd != ad) {  // Race condition, load() called again before last ad was displayed
                    return;
                }
                inflateAd(nativeAd);  // Inflate Native Ad into Container
                countdown5Seconds();
            }

            @Override
            public void onAdClicked(Ad ad) {   // Native ad clicked
            }

            @Override
            public void onLoggingImpression(Ad ad) {  // Native ad impression
            }
        });
        nativeAd.loadAd();   // Request an ad
    }

    private void inflateAd(NativeAd nativeAd) {
        nativeAd.unregisterView();
        bottomLinearLayout =  findViewById(R.id.bottomLinearLayout);
        bottomLinearLayout.setVisibility(View.VISIBLE);
        nativeAdContainer = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(AdvertisementActivity.this);
        adView = (LinearLayout) inflater.inflate(R.layout.layout_native_ad, nativeAdContainer, false);
        nativeAdContainer.addView(adView);
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(AdvertisementActivity.this, nativeAd, true);
        adChoicesContainer.addView(adChoicesView, 0);
        nativeAdUnit = adView.findViewById(R.id.ad_unit);
        nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
        reciprocalButton = adView.findViewById(R.id.reciprocalButton);
        reciprocalOverImageView =  adView.findViewById(R.id.reciprocalOverImageView);

        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        adClickEvent();
    }

    private void adClickEvent() {
        /** 讓廣告可以被點擊跳轉 */
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdUnit);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

        /** 點擊關閉按鈕 結束廣告 */
        reciprocalOverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdvertisementActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /** 處理倒數5秒的相關邏輯 */
    private void countdown5Seconds() {
        new CountDownTimer(5000, 1000) {
            @Override
            public void onFinish() {
                reciprocalButton.setVisibility(View.GONE);
                reciprocalOverImageView.setVisibility(View.VISIBLE);
                adClickEvent();
            }
            @Override
            public void onTick(long millisUntilFinished) {
                reciprocalButton.setText("倒數 " + millisUntilFinished / 1000);
            }
        }.start();
    }

    /**
     * 不能按返回鍵取消廣告
     */
    @Override
    public void onBackPressed() {
    }
}
