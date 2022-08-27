package com.treward.info.activity;

import static com.treward.info.utils.Constant.VISIT_REQUEST_CODE;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.treward.info.App;
import com.treward.info.R;
import com.treward.info.adapter.WebsiteAdapter;
import com.treward.info.listener.UpdateListener;
import com.treward.info.models.WebsiteModel;
import com.treward.info.utils.Constant;
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
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VisitActivity extends AppCompatActivity implements IUnityAdsInitializationListener,
        MaxAdListener, MaxRewardedAdListener, MaxAdViewAdListener, UpdateListener {

    VisitActivity activity;
    TextView user_points_text_view, noWebsiteFound;

    private MediaPlayer popupSound;
    private MediaPlayer collectSound;
    private MediaPlayer timerSound;
    private Handler timerHandler, pointsHandler;
    public int poiints = 0, counter_dialog = 0;
    private StartAppAd startAppAd;
    private MaxInterstitialAd maxInterstitialAd;
    private MaxRewardedAd maxRewardedAd;
    private MaxAdView adView;
    private BannerView bottomBanner;
    String adType;
    long soundTime = 10000;

    private AdColonyInterstitial interstitialAdColony;
    private AdColonyInterstitialListener interstitialListener;
    private AdColonyAdOptions interstitialAdOptions;
    private static boolean isInterstitialLoaded;
    private AdColonyAdView bannerAdColony;

    private AdColonyInterstitial rewardAdColony;
    private AdColonyInterstitialListener rewardListener;
    private AdColonyAdOptions rewardAdOptions;
    private static boolean isRewardLoaded;
    public final static String[] AD_UNIT_Zone_Ids = new String[] {"vz5d8ed4276a624525b2","vzd36ae5b740454e54a0","vz6b25d2d9625b42de95"};
    public final static String colonybannerid="vz5d8ed4276a624525b2";
    public final static String colonyinsid="vzd36ae5b740454e54a0";
    public final static String colonyrewardid="vz6b25d2d9625b42de95";
    public final static String colonyappid="appcacf7ab3cdf94003aa";


    CountDownTimer collectClickTimer;
    int collectClickCounter = 0, visit_index = 0;
    boolean collectClickTimerTrue = false, isWebsiteVisited = false;

    String visit1_link, visit_time, visit_browser, visit_id;
    private LinearLayout image_container;
    private WebsiteAdapter websiteAdapter;
    private ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
    private RecyclerView websiteRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        activity = this;
        loadAdColony();
        image_container = findViewById(R.id.image_container);
        noWebsiteFound = findViewById(R.id.noWebsiteFound);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        adType = Constant.getString(activity, Constant.AD_TYPE);
        popupSound = MediaPlayer.create(activity, R.raw.popup);
        collectSound = MediaPlayer.create(activity, R.raw.collect);
        timerSound = MediaPlayer.create(activity, R.raw.timer_sound);
        websiteRv = findViewById(R.id.websiteRv);

        websiteRv.setLayoutManager(new LinearLayoutManager(activity));
        String json = Constant.getString(activity, Constant.WEBSITE_LIST);
        if (!json.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<WebsiteModel>>() {
            }.getType();
            ArrayList<WebsiteModel> websiteModels = gson.fromJson(json, type);
            for (int i = 0; i < websiteModels.size(); i++) {
                if (!Constant.getString(activity, Constant.VISITED1_DATE + websiteModels.get(i).getId()).equalsIgnoreCase(Constant.getString(activity, Constant.TODAY_DATE))) {
                    websiteModelArrayList.add(websiteModels.get(i));
                }
            }
            websiteAdapter = new WebsiteAdapter(websiteModelArrayList, activity, "website", activity);
            websiteRv.setAdapter(websiteAdapter);
        }
        setWebsiteData();
        final LinearLayout layout = findViewById(R.id.banner_container);
        final LinearLayout layout2 = findViewById(R.id.banner_container2);
        image_container.setVisibility(View.GONE);
        if (adType.equalsIgnoreCase("startapp")) {
            LoadStartAppBanner(layout);
            LoadStartAppBanner(layout2);
            LoadStartAppInterstital();
        } else if (adType.equalsIgnoreCase("unity")) {
            LoadadcolonyBannerAd();
            LoadColonyInstital();
            loadColonoyReward();
        } else if (adType.equalsIgnoreCase("applovin")) {
            loadApplovinBanner();
            loadApplovinInterstitial();
            loadApplovinRewarded();
        } else {
            image_container.setVisibility(View.VISIBLE);
        }
        LoadStartAppInterstital();
        ShowStartappInterstital(false);
        collectTimer();

    }
    private void loadAdColony(){

        AdColonyAppOptions appOptions = new AdColonyAppOptions().setKeepScreenOn(true);;

        AdColony.configure(getApplication(), appOptions, "appcacf7ab3cdf94003aa", AD_UNIT_Zone_Ids);
    }
    private void LoadadcolonyBannerAd() {
        LinearLayout bannerConatiner =findViewById (R.id.adsid);
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
        AdColony.requestAdView(colonybannerid, bannerListener, AdColonyAdSize.BANNER, adOptions);
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

                AdColony.requestInterstitial(colonyrewardid, rewardListener, rewardAdOptions);
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
        AdColony.requestInterstitial(colonyrewardid,rewardListener,rewardAdOptions);
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

                AdColony.requestInterstitial(colonyinsid, interstitialListener, interstitialAdOptions);
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
        AdColony.requestInterstitial(colonyinsid, interstitialListener, interstitialAdOptions);
    }

    private void setWebsiteData() {
        if (websiteModelArrayList.isEmpty()) {
            websiteRv.setVisibility(View.GONE);
            noWebsiteFound.setVisibility(View.VISIBLE);
        } else {
            websiteRv.setVisibility(View.VISIBLE);
            noWebsiteFound.setVisibility(View.GONE);
        }
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
    }

    private void showDialogPoints(final int addNoAddValueVideo, final String points) {
        SweetAlertDialog sweetAlertDialog;

        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            if (addNoAddValueVideo == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText(getResources().getString(R.string.better_luck));
                    sweetAlertDialog.setConfirmText("Ok");
                } else if (points.equals("no")) {
                    Log.e("TAG", "showDialogPoints: no points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText("You missed " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
                    sweetAlertDialog.setConfirmText("Ok");
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.you_won));
                    sweetAlertDialog.setContentText(points);
                    sweetAlertDialog.setConfirmText("Collect");
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setTitleText(getResources().getString(R.string.today_chance_over));
                sweetAlertDialog.setConfirmText("Ok");
            }

            sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                collectClickTimer.start();
                if (collectClickTimerTrue) {
                    collectClickCounter++;
                }
                if (collectClickCounter > 1) {
                    putDate(Constant.LAST_DATE_INVALID, "invalid");
                    checkInvalidBlocked();
                }
                int finalPoint;
                if (points.equals("")) {
                    finalPoint = 0;
                } else {
                    finalPoint = Integer.parseInt(points);
                }
                poiints = finalPoint;
                Constant.addPoints(activity, finalPoint, 0, "apps", Constant.getString(activity, ""));
                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                sweetAlertDialog1.dismiss();
                collectSound.start();
                allRewardedShow();
                websiteModelArrayList.remove(visit_index);
                websiteAdapter.notifyItemRemoved(visit_index);
                setWebsiteData();
                putDate(Constant.VISITED1_DATE + visit_id, visit_id);
            }).show();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }


    private void putDate(String watched_date, String scratchType) {
        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            String currentDateWatched = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDateWatched);
            String last_date_watched = Constant.getString(activity, watched_date);
            if (last_date_watched.equalsIgnoreCase("0")) {
                last_date_watched = "";
            }
            Log.e("TAG", "onClick: last_date Date" + last_date_watched);
            if (last_date_watched.equals("")) {
                Constant.setString(activity, watched_date, currentDateWatched);
                Constant.addPoints(activity, 0, 0, scratchType, currentDateWatched);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date pastDAte = sdf.parse(last_date_watched);
                    Date currentDAte = sdf.parse(currentDateWatched);
                    long diff = currentDAte.getTime() - pastDAte.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, watched_date, currentDateWatched);
                        Constant.addPoints(activity, 0, 0, scratchType, currentDateWatched);
                    } else {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }

    void collectTimer() {
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

    private void checkInvalidBlocked() {
        if (Constant.getString(activity, Constant.LAST_DATE_INVALID).equals(Constant.getString(activity, Constant.TODAY_DATE))) {
            Constant.showBlockedDialog(activity, activity, "You are Blocked for today! Reason is: Invalid Clicks");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }


    private void LoadStartAppInterstital() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(App.getContext());
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }

    private void ShowStartappInterstital(boolean b) {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    if (b) {
                        visitLinks();
                    }
                }

                @Override
                public void adDisplayed(Ad ad) {

                }

                @Override
                public void adClicked(Ad ad) {

                }

                @Override
                public void adNotDisplayed(Ad ad) {
                    LoadStartAppInterstital();
                    if (b) {
                        visitLinks();
                    }
                }
            });
        } else {
            LoadStartAppInterstital();
            if (b) {
                visitLinks();
            }
        }
    }

    private void allInterstitialShow() {
        isWebsiteVisited = false;
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital(true);
        } else if (adType.equalsIgnoreCase("unity")) {
           ShowColonyInstital();
        } else if (adType.equalsIgnoreCase("applovin")) {
            showApplovinInterstital();
        }
    }

    private void allRewardedShow() {
        isWebsiteVisited = true;
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital(false);
        } else if (adType.equalsIgnoreCase("unity")) {
            showcolonyreward();
        } else if (adType.equalsIgnoreCase("applovin")) {
            showApplovinRewarded();
        }
    }


    private void LoadStartAppBanner(LinearLayout layout) {
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


    private void loadUnityBanner() {
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
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
            if (!isWebsiteVisited) {
                visitLinks();
            }
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
            if (!isWebsiteVisited) {
                visitLinks();
            }
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
    public void unityInterstitialAd() {
        UnityAds.show(activity, Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), new UnityAdsShowOptions(), showListener);
    }

    public void unityRewardedAd() {
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
        adView.setVisibility(View.VISIBLE);
        adView.startAutoRefresh();
    }


    private void showApplovinInterstital() {
        if (maxInterstitialAd.isReady()) {
            maxInterstitialAd.showAd();
        } else {
            loadApplovinInterstitial();
            visitLinks();
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
        if (!ad.getAdUnitId().equalsIgnoreCase(Constant.getString(activity, Constant.APPLOVIN_BANNER_ID))) {
            if (!isWebsiteVisited) {
                visitLinks();
            }
        }

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        if (!ad.getAdUnitId().equalsIgnoreCase(Constant.getString(activity, Constant.APPLOVIN_BANNER_ID))) {
            if (!isWebsiteVisited) {
                visitLinks();
            }
        }
    }


    private void visitLinks() {
        if (visit_browser.equalsIgnoreCase("external")) {
            openLink(visit1_link);
            timerHandler = new Handler();
            timerHandler.postDelayed(() -> {
                timerSound.start();
                pointsHandler = new Handler();
                pointsHandler.postDelayed(() -> {
                    timerHandler = null;
                    pointsHandler = null;
                    showDialogPoints(1, String.valueOf(poiints));
                }, soundTime);
            }, TimeUnit.MINUTES.toMillis(Long.parseLong(visit_time)));
        } else {
            Intent intent = new Intent(activity, VisitLinksActivity.class);
            intent.putExtra("type", "website");
            intent.putExtra("websiteModal", websiteModelArrayList.get(visit_index));
            startActivityForResult(intent, VISIT_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VISIT_REQUEST_CODE && resultCode == RESULT_OK) {
            showDialogPoints(1, String.valueOf(poiints));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerSound != null) {
            timerSound.stop();
            timerSound = null;
        }
        if (pointsHandler != null) {
            pointsHandler = null;
        }
        if (timerHandler != null) {
            timerHandler = null;
        }
    }

    @Override
    public void UpdateListener(
            @Nullable String coin,
            @Nullable String time,
            @Nullable String link,
            @Nullable String browser,
            @Nullable String id,
            int index,
            String description,
            String logo,
            String packages) {
        visit1_link = link;
        visit_time = time;
        visit_browser = browser;
        visit_id = id;
        visit_index = index;
        poiints = Integer.parseInt(coin);
        allInterstitialShow();
    }
}