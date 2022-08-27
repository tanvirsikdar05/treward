package com.treward.info.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyZone;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.treward.info.App;
import com.treward.info.R;
import com.treward.info.utils.Constant;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class ScratchActivity extends AppCompatActivity implements ScratchListener, IUnityAdsInitializationListener,
        MaxAdListener, MaxRewardedAdListener, MaxAdViewAdListener {
    TextView scratch_count_textView, user_points_text_view;
    ScratchActivity activity;
    ScratchCardLayout scratchCardLayout;
    private AdColonyAdView bannerAdColony;
    TextView points_text, total_scratch;
    boolean first_time = true;
    int rewardAdsCount=0;
    int rewardcounter=1;
    public final static String[] AD_UNIT_Zone_Ids = new String[] {"vz5d8ed4276a624525b2","vzd36ae5b740454e54a0","vz6b25d2d9625b42de95"};
    private int scratch_count = 0;
    private final String TAG = ScratchActivity.class.getSimpleName();
    private String random_points, points_ad;
    public int poiints = 0, counter_dialog = 0;
    public boolean rewardShow = true, interstitialShow = true;
    public StartAppAd startAppAd;
    private MaxInterstitialAd maxInterstitialAd;
    private MaxRewardedAd maxRewardedAd;
    private MaxAdView adView;
    private BannerView bottomBanner;
    String adType;
    int adsClickCounter = 0;
    CountDownTimer adsClickTimer;
    String currentDateInvalid, last_date_invalid;
    CountDownTimer collectClickTimer;
    int collectClickCounter = 0;
    boolean collectClickTimerTrue = false;
    boolean isClickTimerTrue = false;
    int seconds;
    long minutes;
    private MediaPlayer scratchSound;
    private MediaPlayer popupSound;
    private MediaPlayer collectSound;
    boolean isStartAppTrue = true;


    private AdColonyInterstitial interstitialAdColony;
    private AdColonyInterstitialListener interstitialListener;
    private AdColonyAdOptions interstitialAdOptions;
    private static boolean isInterstitialLoaded;

    private AdColonyInterstitial rewardAdColony;
    private AdColonyInterstitialListener rewardListener;
    private AdColonyAdOptions rewardAdOptions;
    private static boolean isRewardLoaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        activity = this;

        scratch_count_textView = findViewById(R.id.scratch_count_textView);
        points_text = findViewById(R.id.textView_points_show);
        scratchCardLayout = findViewById(R.id.scratch_view_layout);
        scratchCardLayout.setScratchListener(activity);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        total_scratch = findViewById(R.id.total_scratch);
        scratchCardLayout.setRevealFullAtPercent(90);
        adType = Constant.getString(activity, Constant.AD_TYPE);
        loadAdColony();
        onInit();

        scratchSound = MediaPlayer.create(activity, R.raw.eraser);
        popupSound = MediaPlayer.create(activity, R.raw.popup);
        collectSound = MediaPlayer.create(activity, R.raw.collect);

        minutes = (Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000)  / 60;
        seconds = (int)((Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000) % 60);

        Constant.adsShowingDialog(activity);
        if (adType.equalsIgnoreCase("startapp")) {
            LoadStartAppBanner();
            LoadStartAppInterstital();
        } else if (adType.equalsIgnoreCase("unity")){
            /*loadUnityBanner();
            UnityAds.load(Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), loadListener);
            UnityAds.load(Constant.getString(activity, Constant.UNITY_REWARDED_ID), loadListener);*/
            LoadadcolonyBannerAd();
            LoadColonyInstital();
            loadColonoyReward();
        } else if (adType.equalsIgnoreCase("applovin")){
            loadApplovinBanner();
            loadApplovinInterstitial();
            loadApplovinRewarded();
        }
        total_scratch.setText(Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));

        LoadStartAppInterstital();
        ShowStartappInterstital();
        resetTimer();
        collectTimer();
        Constant.loadInvalidCounter(activity);
    }

    private void loadAdColony(){

        AdColonyAppOptions appOptions = new AdColonyAppOptions().setKeepScreenOn(true);;

        AdColony.configure(getApplication(), appOptions, "appcacf7ab3cdf94003aa", AD_UNIT_Zone_Ids);
    }
    private void LoadadcolonyBannerAd() {
        LinearLayout bannerConatiner =findViewById (R.id.banner_container);
        AdColonyAdViewListener bannerListener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {

                if (bannerConatiner.getChildCount() > 0) {
                    bannerConatiner.removeView(bannerAdColony);
                }
                bannerConatiner.addView(adColonyAdView);
                bannerAdColony = adColonyAdView;
            }
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
            }
            @Override
            public void onOpened(AdColonyAdView ad) {
                super.onOpened(ad);
            }
            @Override
            public void onClosed(AdColonyAdView ad) {
                super.onClosed(ad);
            }
            @Override
            public void onClicked(AdColonyAdView ad) {
                super.onClicked(ad);
            }
            @Override
            public void onLeftApplication(AdColonyAdView ad) {
                super.onLeftApplication(ad);
            }
        };
// Optional Ad specific options to be sent with request
        AdColonyAdOptions adOptions = new AdColonyAdOptions();
//Request Ad
        AdColony.requestAdView("vz5d8ed4276a624525b2", bannerListener, AdColonyAdSize.BANNER, adOptions);
    }
    private void loadColonoyReward() {

        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {


            }
        });

        rewardListener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adReward) {

                rewardAdColony = adReward;
                isRewardLoaded=true;
            }
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
            }
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
            }
            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);

                AdColony.requestInterstitial("vz6b25d2d9625b42de95", rewardListener, rewardAdOptions);
            }
            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
            }
            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };

// Ad specific options to be sent with request
        rewardAdOptions = new AdColonyAdOptions()
                .enableConfirmationDialog(false)
                .enableResultsDialog(false);
        AdColony.requestInterstitial("vz6b25d2d9625b42de95",rewardListener,rewardAdOptions);
    }
    public void ShowColonyInstital() {
        if (interstitialAdColony!=null && isInterstitialLoaded) {
            interstitialAdColony.show();
            isInterstitialLoaded=false;
        }
        else {
            Toast.makeText(getApplicationContext(),"Interstitial Ad Is Not Loaded Yet or Request Not Filled",Toast.LENGTH_SHORT).show();
        }
    }
    public void showcolonyreward() {
        if (rewardAdColony!=null && isRewardLoaded) {
            rewardAdColony.show();
            isRewardLoaded=false;
        }
        else {
            Toast.makeText(getApplicationContext(),"Reward Ad Is Not Loaded Yet or Request Not Filled",Toast.LENGTH_SHORT).show();
        }
    }
    private void LoadColonyInstital() {

        interstitialListener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adIn) {

                interstitialAdColony = adIn;
                isInterstitialLoaded=true;
            }
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
            }
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
            }
            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);

                AdColony.requestInterstitial("vzd36ae5b740454e54a0", interstitialListener, interstitialAdOptions);
            }
            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
            }
            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };
// Optional Ad specific options to be sent with request
        interstitialAdOptions = new AdColonyAdOptions();
        AdColony.requestInterstitial("vzd36ae5b740454e54a0", interstitialListener, interstitialAdOptions);
    }


    private void LoadStartAppInterstital() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(App.getContext());
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }

    private void ShowStartappInterstital() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        adsClickCounter = 0;
                        int counter = Integer.parseInt(scratch_count_textView.getText().toString());
                        showDialogPoints(1, "no", counter, true);
                    }
                }
                @Override
                public void adDisplayed(Ad ad) {
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        Constant.showToastMessage(activity, "Click on this Ads to win " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
                    }
                }
                @Override
                public void adClicked(Ad ad) {
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        adsClickCounter = 0;
                        adsClickTimer.start();
                    } else {
                        Constant.invalidClickCount++;
                        Log.i("checkInvalidClick", String.valueOf(Constant.invalidClickCount));
                    }
                }
                @Override
                public void adNotDisplayed(Ad ad) {
                    LoadStartAppInterstital();
                }
            });
        } else {
            LoadStartAppInterstital();
        }
    }


    private void onInit() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
        String scratchCount = Constant.getString(activity, Constant.SCRATCH_COUNT);
        if (scratchCount.equals("0")) {
            scratchCount = "";
            Log.e("TAG", "onInit: scratch card 0");
        }
        if (scratchCount.equals("")) {
            Log.e("TAG", "onInit: scratch card empty vala part");
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_SCRATCH);
            Log.e("TAG", "Lat date" + last_date);
            if (last_date.equalsIgnoreCase("0")) {
                last_date = "";
            }
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                scratch_count_textView.setText(Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.SCRATCH_COUNT, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.LAST_DATE_SCRATCH, currentDate);
                Constant.addDate(activity, "scratch", Constant.getString(activity, Constant.LAST_DATE_SCRATCH), Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
            } else {
                Log.e("TAG", "onInit: last date not empty part");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date current_date = sdf.parse(currentDate);
                    Date lastDate = sdf.parse(last_date);
                    long diff = current_date.getTime() - lastDate.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Difference" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_SCRATCH, currentDate);
                        Constant.setString(activity, Constant.SCRATCH_COUNT, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                        scratch_count_textView.setText(Constant.getString(activity, Constant.SCRATCH_COUNT));
                        Constant.addDate(activity, "scratch", Constant.getString(activity, Constant.LAST_DATE_SCRATCH), Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                    } else {
                        scratch_count_textView.setText("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: scracth card in preference part");
            scratch_count_textView.setText(scratchCount);
        }
    }

    private void LoadStartAppBanner() {
        final LinearLayout layout = findViewById(R.id.banner_container);
        Banner banner = new Banner(activity, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                layout.addView(view);
            }

            @Override
            public void onFailedToReceiveAd(View view) {

            }

            @Override
            public void onImpression(View view) {

            }

            @Override
            public void onClick(View view) {
                Constant.invalidClickCount++;
                Log.i("checkInvalidClick", String.valueOf(Constant.invalidClickCount));
            }
        });
        banner.loadAd(300, 50);
    }
    private void ShowStartappInterstitaltwo() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd();
        } else {
            LoadStartAppInterstital();
        }
    }


    private void showDialogPoints(final int addOrNoAddValue, final String points, final int counter, boolean isDialogShow) {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog ;

        if (Constant.isNetworkAvailable(activity)) {

            if (rewardAdsCount %2 == 0){
                ShowStartappInterstitaltwo();
            }else {
                loadApplovinRewarded();
                showApplovinRewarded();
            }
            rewardAdsCount +=1;

            if (addOrNoAddValue == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText(getResources().getString(R.string.better_luck));
                    sweetAlertDialog.setConfirmText("Ok");
                } else if (points.equals("no")){
                    Log.e("TAG", "showDialogPoints: no points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText("You missed " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
                    sweetAlertDialog.setConfirmText("Ok");
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    Log.e("TAG", "showDialogPoints: rewared cancel");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.you_won));
                    sweetAlertDialog.setContentText(points);
                    sweetAlertDialog.setConfirmText("Collect");
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setTitleText(getResources().getString(R.string.today_chance_over));
                sweetAlertDialog.setConfirmText("Ok");
            }

            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    if (rewardcounter % 2 == 0){
                        showApplovinRewarded();
                    }else {
                        allInterstitialShow();
                    }
                    rewardcounter=rewardcounter+1;
                    collectClickTimer.start();
                    if (collectClickTimerTrue){
                        collectClickCounter++;
                    }

                    if (collectClickCounter >= 2){
                        putInvalidClickDate();
                        checkInvalidBlocked();
                    }


                    if (addOrNoAddValue == 1) {
                        if (points.equals("0")) {
                        } else if (points.equals("no")){

                        }else {
                            collectSound.start();
                        }
                    } else {
                    }

                    first_time = true;
                    scratchCardLayout.resetScratch();
                    poiints = 0;
                    if (addOrNoAddValue == 1) {
                        if (points.equals("0") || points.equalsIgnoreCase("no")) {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT, current_counter);
                            scratch_count_textView.setText(Constant.getString(activity, Constant.SCRATCH_COUNT));
                            sweetAlertDialog.dismiss();
                        } else {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT, current_counter);
                            scratch_count_textView.setText(Constant.getString(activity, Constant.SCRATCH_COUNT));
                            try {
                                int finalPoint;
                                if (points.equals("")) {
                                    finalPoint = 0;
                                } else {
                                    finalPoint = Integer.parseInt(points);
                                }
                                poiints = finalPoint;
                                Constant.addPoints(activity, finalPoint, 0, "scratch", Constant.getString(activity, Constant.SCRATCH_COUNT));
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                            } catch (NumberFormatException ex) {
                                Log.e("TAG", "onScratchComplete: " + ex.getMessage());
                            }
                            sweetAlertDialog.dismiss();
                        }
                    } else {
                        sweetAlertDialog.dismiss();
                    }
                    if (addOrNoAddValue == 1) {
                        if (scratch_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
                            if (rewardShow) {


                                rewardShow = false;
                                isStartAppTrue = true;
                                scratch_count = 0;
                            }else {
                                Log.e(TAG, "onReachTarget: interstitial ads showing method");

                                rewardShow = true;
                                scratch_count = 0;
                            }
                        } else {
                            scratch_count++;
                        }
                    }
                }
            }).show();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }



    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        Log.e("onScratch", "onScratchProgress");
    }

    @Override
    public void onScratchStarted() {

        Log.e("onScratch", "onScratchStarted");
        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            if (Integer.parseInt(scratch_count_textView.getText().toString().trim()) <= 0) {
                showDialogPoints(0, "0", 0, true);
                return;
            }
            random_points = "";
            Random random = new Random();
            String str = Constant.getString(activity, Constant.SCRATCH_PRICE_COIN);
            String[] parts = str.split("-", 2);
            int low = Integer.parseInt(parts[0]);
            int high = Integer.parseInt(parts[1]);
            int result = random.nextInt(high - low) + low;
            random_points = String.valueOf(result);
            points_text.setText(random_points);
            scratchSound.start();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }




    @Override
    public void onScratchComplete() {
        if (first_time) {
            first_time = false;
            Log.e("onScratch", "Complete");
            Log.e("onScratch", "Complete" + random_points);
            adsClickCounter ++;
            int counter = Integer.parseInt(scratch_count_textView.getText().toString());
            counter_dialog = Integer.parseInt(scratch_count_textView.getText().toString());

            if (counter <= 0) {
                showDialogPoints(0, "0", counter, true);
            } else {
                if (adType.equalsIgnoreCase("startapp")){
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        adsClickDialog();
                    } else {
                        showDialogPoints(1, points_text.getText().toString(), counter, true);
                    }
                } else {
                    showDialogPoints(1, points_text.getText().toString(), counter, true);
                }
            }
        }
    }


    private void adsClickDialog() {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog;

        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle("Wow! You won " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
        sweetAlertDialog.setContentText("Click on this ads & wait " + seconds + " sec" + " to win " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Constant.showAdsLoadingDialog();
                Constant.dismissAdsLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        allInterstitialShow();
                        sweetAlertDialog.dismiss();
                    }
                }, Constant.adsDialogTime);
            }
        }).show();
    }

    void resetTimer(){
        adsClickTimer = new CountDownTimer(Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Toast.makeText(activity, "Timer left: " + millisUntilFinished/1000, Toast.LENGTH_LONG).show();
                isClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                isClickTimerTrue = false;
                int counter = Integer.parseInt(scratch_count_textView.getText().toString());
                showDialogPoints(1, Constant.getString(activity, Constant.ADS_CLICK_COINS), counter, true);
            }
        };
    }


    void collectTimer(){
        collectClickTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                collectClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                collectClickTimerTrue = false;
            }
        };
    }

    private void putInvalidClickDate(){
        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            currentDateInvalid = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDateInvalid);
            last_date_invalid = Constant.getString(activity, Constant.LAST_DATE_INVALID);
            if (last_date_invalid.equalsIgnoreCase("0")) {
                last_date_invalid = "";
            }
            Log.e("TAG", "onClick: last_date Date" + last_date_invalid);
            if (last_date_invalid.equals("")) {
                Constant.setString(activity, Constant.LAST_DATE_INVALID, currentDateInvalid);
                Constant.addPoints(activity, 0, 0, "invalid", currentDateInvalid);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date pastDAte = sdf.parse(last_date_invalid);
                    Date currentDAte = sdf.parse(currentDateInvalid);
                    long diff = currentDAte.getTime() - pastDAte.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_INVALID, currentDateInvalid);
                        Constant.addPoints(activity, 0, 0, "invalid", currentDateInvalid);
                    } else {
                        //showDialogOfPoints(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }

    private void checkInvalidBlocked(){
        if (Constant.getString(activity, Constant.LAST_DATE_INVALID).equals(Constant.getString(activity, Constant.TODAY_DATE))){
            Constant.showBlockedDialog(activity,activity,"You are Blocked for today! Reason is: Invalid Clicks");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    private void allInterstitialShow() {
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")){
            ShowColonyInstital();
        } else if (adType.equalsIgnoreCase("applovin")){
            showApplovinInterstital();
        }
    }

    private void allRewardedShow() {
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")){
            showcolonyreward();

        } else if (adType.equalsIgnoreCase("applovin")){
            showApplovinRewarded();
        }
    }

    private void loadUnityBanner(){
        final LinearLayout layout = findViewById(R.id.banner_container);
        bottomBanner = new BannerView(activity, Constant.getString(activity, Constant.UNITY_BANNER_ID), new UnityBannerSize(320, 50));
        bottomBanner.setListener(bannerListener);
        bottomBanner.load();
        layout.addView(bottomBanner);
    }

    private BannerView.IListener bannerListener = new BannerView.IListener() {
        @Override
        public void onBannerLoaded(BannerView bannerAdView) {
            Log.v("unityCheck", "onBannerLoaded: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
            Log.e("unityCheck", "Unity Ads failed to load banner for " + bannerAdView.getPlacementId() + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage);
        }

        @Override
        public void onBannerClick(BannerView bannerAdView) {
            Log.v("unityCheck", "onBannerClick: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerLeftApplication(BannerView bannerAdView) {
            Log.v("unityCheck", "onBannerLeftApplication: " + bannerAdView.getPlacementId());
        }
    };

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {

        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
            UnityAds.load(Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), loadListener);
            UnityAds.load(Constant.getString(activity, Constant.UNITY_REWARDED_ID), loadListener);
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
            UnityAds.load(Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), loadListener);
            UnityAds.load(Constant.getString(activity, Constant.UNITY_REWARDED_ID), loadListener);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
        }
    };

    @Override
    public void onInitializationComplete() {

    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [" + error + "] " + message);
    }

    // Implement a function to load an interstitial ad. The ad will start to show once the ad has been loaded.
    public void unityInterstitialAd () {
        UnityAds.show(activity, Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), new UnityAdsShowOptions(), showListener);
    }

    public void unityRewardedAd () {
        UnityAds.show(activity, Constant.getString(activity, Constant.UNITY_REWARDED_ID), new UnityAdsShowOptions(), showListener);
    }


    private void loadApplovinBanner() {
        final LinearLayout layout = findViewById(R.id.banner_container);
        adView = new MaxAdView(Constant.getString(activity, Constant.APPLOVIN_BANNER_ID), activity);
        adView.setListener(this);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
        adView.setLayoutParams(new LinearLayout.LayoutParams(width, heightPx));
        layout.addView(adView);

        adView.loadAd();
        adView.startAutoRefresh();
    }



    private void showApplovinInterstital() {
        if (maxInterstitialAd.isReady()) {
            maxInterstitialAd.showAd();
        } else {
            loadApplovinInterstitial();
        }
    }


    private void loadApplovinInterstitial() {
        if (maxInterstitialAd == null) {
            maxInterstitialAd = new MaxInterstitialAd(Constant.getString(activity, Constant.APPLOVIN_INTERSTITAL_ID), activity);
            maxInterstitialAd.setListener(this);
        }
        // Load the first ad
        maxInterstitialAd.loadAd();
    }

    private void showApplovinRewarded() {
        if (maxRewardedAd.isReady()) {
            maxRewardedAd.showAd();
        } else {
            loadApplovinRewarded();
        }
    }
    private void loadApplovinRewarded() {
        if (maxRewardedAd == null) {
            maxRewardedAd = MaxRewardedAd.getInstance(Constant.getString(activity, Constant.APPLOVIN_REWARDED_ID), activity);
            maxRewardedAd.setListener(this);
        }
        maxRewardedAd.loadAd();
    }


    @Override
    public void onAdExpanded(MaxAd ad) {

    }

    @Override
    public void onAdCollapsed(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (adsClickTimer != null) {
            adsClickTimer.cancel();
            if (isClickTimerTrue){
                isClickTimerTrue = false;
                int counter = Integer.parseInt(scratch_count_textView.getText().toString());
                showDialogPoints(1, "no", counter, true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.saveInvalidCounter(activity);
    }


}