package com.treward.info;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.gson.JsonArray;
import com.startapp.networkTest.net.WebApiClient;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.treward.info.Service.CustomTimerTask;
import com.treward.info.Service.Sample_service;
import com.treward.info.models.WebsiteModel;
import com.treward.info.utils.Const;
import com.treward.info.utils.Constant;
import com.treward.info.utils.Utils;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameAllActivity extends AppCompatActivity implements MaxAdViewAdListener, MaxRewardedAdListener {

    private ImageView buttonPlayOne, buttonPlayTwo, buttonPlayThree, buttonPlayFour, buttonPlayFive, buttonPlaySix, buttonPlaySeven, buttonPlayEight;
    public static int value = 34;
    public static String eightbuttonReward = "50";
    public static TextView textCounter;
    ArrayList<WebsiteModel> datalist=new ArrayList<>();
    public static String rootValue, btnVal,clickButton="0";
    private StartAppAd startAppAd;
    private MaxInterstitialAd maxInterstitialAd;
    private MaxRewardedAd maxRewardedAd;
    private MaxAdView adView;
    int counter=0;
    GameAllActivity activity;
    private  int adscounter=0;
    private BannerView bottomBanner;
    String adType;

    private AdColonyInterstitial interstitialAdColony;
    private AdColonyInterstitialListener interstitialListener;
    private AdColonyAdOptions interstitialAdOptions;
    private static boolean isInterstitialLoaded;
    private AdColonyAdView bannerAdColony;

    private AdColonyInterstitial rewardAdColony;
    private AdColonyInterstitialListener rewardListener;
    private AdColonyAdOptions rewardAdOptions;
    private static boolean isRewardLoaded;
    public final static String[] AD_UNIT_Zone_Ids = new String[] {Constant.UNITY_BANNER_ID,Constant.UNITY_INTERSTITAL_ID,Constant.UNITY_REWARDED_ID};


    private static String url="http://fastvpnreward.videoreward.info/admin_panel/api/visit_settings.php?get_visit_settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_all);
        rootValue = getIntent().getStringExtra("Intent");
        adType = Constant.getString(GameAllActivity.this, Constant.AD_TYPE);
        activity=this;
        clickButton="0";
        loadAdColony();





        if (adType.equalsIgnoreCase("startapp")) {
            LoadStartAppBanner();
            LoadStartAppInterstital();
        } else if (adType.equalsIgnoreCase("unity")){
           // loadUnityBanner();
            LoadadcolonyBannerAd();
            LoadColonyInstital();
            loadColonoyReward();
        } else if (adType.equalsIgnoreCase("applovin")){
            loadApplovinBanner();
            loadApplovinInterstitial();
            loadApplovinRewarded();
        }



        getdataFromserver();
        initlization();
        clickLister();


    }

    private void loadAdColony(){

        AdColonyAppOptions appOptions = new AdColonyAppOptions().setKeepScreenOn(true);;

        AdColony.configure(activity, appOptions, Constant.UNITY_GAME_ID, AD_UNIT_Zone_Ids);
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
        AdColony.requestAdView(Constant.UNITY_BANNER_ID, bannerListener, AdColonyAdSize.BANNER, adOptions);
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

                AdColony.requestInterstitial(Constant.UNITY_REWARDED_ID, rewardListener, rewardAdOptions);
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
        AdColony.requestInterstitial(Constant.UNITY_REWARDED_ID,rewardListener,rewardAdOptions);
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

                AdColony.requestInterstitial(Constant.UNITY_INTERSTITAL_ID, interstitialListener, interstitialAdOptions);
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
        AdColony.requestInterstitial(Constant.UNITY_INTERSTITAL_ID, interstitialListener, interstitialAdOptions);
    }

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {

        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };
    private void loadApplovinBanner() {
        final LinearLayout layout = findViewById(R.id.adsid);
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
    private void LoadStartAppBanner() {
        final LinearLayout layout = findViewById(R.id.adsid);
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


    private void getdataFromserver() {
        RequestQueue queue= Volley.newRequestQueue(GameAllActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int icounter=0;icounter < jsonArray.length();icounter++){
                        JSONObject jsonObject=jsonArray.getJSONObject(icounter);

                        String id=jsonObject.getString("id");
                        String is_visit_enable=jsonObject.getString("is_visit_enable");
                        String visittitle=jsonObject.getString("visit_title");
                        String link=jsonObject.getString("visit_link");
                        String coin=jsonObject.getString("visit_coin");
                        String visitTime=jsonObject.getString("visit_timer");
                        String browser=jsonObject.getString("browser");
                        datalist.add(new WebsiteModel(id,is_visit_enable,visittitle,link,coin,visitTime,browser,"","",""));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);

    }


    private void clickLister() {
        buttonPlayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.size() > 0 ){
                    adscounter=0;
                    startService(new Intent(GameAllActivity.this , Sample_service.class));
                    value=Integer.parseInt(datalist.get(0).getVisit_timer());
                    clickButton="1";
                    //Utils.savegame(GameAllActivity.this, btnVal, "true");
                    timerAndWeb(datalist.get(0).getVisit_link());


                }

            }
        });
        buttonPlayTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.size() > 1){
                    String decodeavility=rootValue + "1";
                    String abilty=Utils.getFrmSharedgame(GameAllActivity.this,decodeavility,"null");
                    if (!abilty.equals("null") && !abilty.equals("false")){
                        adscounter=0;
                        startService(new Intent(GameAllActivity.this , Sample_service.class));
                        value=Integer.parseInt(datalist.get(1).getVisit_timer());
                        clickButton="2";
                        timerAndWeb(datalist.get(1).getVisit_link());


                    }else {
                        Constant.showToastMessage(GameAllActivity.this,"Unlock previous task fast");
                    }
                }


            }
        });
        buttonPlayThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.size() > 2){
                    String decodeavility=rootValue + "2";
                    String abilty=Utils.getFrmSharedgame(GameAllActivity.this,decodeavility,"null");
                    if (!abilty.equals("null") && !abilty.equals("false")){
                        adscounter=0;
                        startService(new Intent(GameAllActivity.this , Sample_service.class));
                        value=Integer.parseInt(datalist.get(2).getVisit_timer());
                        clickButton="3";
                        timerAndWeb(datalist.get(2).getVisit_link());




                    }else {
                        Constant.showToastMessage(GameAllActivity.this,"Unlock previous task fast");
                    }
                }
            }
        });
        buttonPlayFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.size() > 3){
                    String decodeavility=rootValue + "3";
                    String abilty=Utils.getFrmSharedgame(GameAllActivity.this,decodeavility,"null");
                    if (!abilty.equals("null") && !abilty.equals("false")){
                        adscounter=0;
                        startService(new Intent(GameAllActivity.this , Sample_service.class));
                        value=Integer.parseInt(datalist.get(3).getVisit_timer());
                        clickButton="4";
                        timerAndWeb(datalist.get(3).getVisit_link());




                    }else {
                        Constant.showToastMessage(GameAllActivity.this,"Unlock previous task fast");
                    }
                }
            }
        });
        buttonPlayFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.size()>4){
                    String decodeavility=rootValue + "4";
                    String abilty=Utils.getFrmSharedgame(GameAllActivity.this,decodeavility,"null");
                    if (!abilty.equals("null") && !abilty.equals("false")){
                        adscounter=0;
                        startService(new Intent(GameAllActivity.this , Sample_service.class));
                        value=Integer.parseInt(datalist.get(4).getVisit_timer());
                        clickButton="5";
                        timerAndWeb(datalist.get(4).getVisit_link());



                    }else {
                        Constant.showToastMessage(GameAllActivity.this,"Unlock previous task fast");
                    }
                }
            }
        });
        buttonPlaySix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.size()>5){
                    String decodeavility=rootValue + "5";
                    String abilty=Utils.getFrmSharedgame(GameAllActivity.this,decodeavility,"null");
                    if (!abilty.equals("null") && !abilty.equals("false")){
                        adscounter=0;
                        startService(new Intent(GameAllActivity.this , Sample_service.class));
                        value=Integer.parseInt(datalist.get(5).getVisit_timer());
                        clickButton="6";
                        timerAndWeb(datalist.get(5).getVisit_link());



                    }else {
                        Constant.showToastMessage(GameAllActivity.this,"Unlock previous task fast");
                    }
                }
            }
        });
        buttonPlaySeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("datalist size", String.valueOf(datalist.size()));
                if (datalist.size() >= 6){
                    String decodeavility=rootValue + "6";
                    String abilty=Utils.getFrmSharedgame(GameAllActivity.this,decodeavility,"null");
                    if (!abilty.equals("null") && !abilty.equals("false")){
                        adscounter=0;
                        startService(new Intent(GameAllActivity.this , Sample_service.class));
                        value=Integer.parseInt(datalist.get(6).getVisit_timer());
                        clickButton="7";
                        timerAndWeb(datalist.get(6).getVisit_link());



                    }else {
                        Constant.showToastMessage(GameAllActivity.this,"Unlock previous task fast");
                    }
                }
            }
        });
        buttonPlayEight.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                String val1 =rootValue+"1";
                String val2 = rootValue+ "2";
                String val3 = rootValue+"3";
                String val4 = rootValue+"4";
                String val5 =rootValue+ "5";
                String val6 = rootValue+ "6";
                String val7 = rootValue+"7";

                if(Utils.getFrmSharedgame(GameAllActivity.this, val1, "").equals("true") &&
                        Utils.getFrmSharedgame(GameAllActivity.this, val2, "").equals("true") &&
                        Utils.getFrmSharedgame(GameAllActivity.this, val3, "").equals("true") &&
                        Utils.getFrmSharedgame(GameAllActivity.this, val4, "").equals("true") &&
                        Utils.getFrmSharedgame(GameAllActivity.this, val5, "").equals("true") &&
                        Utils.getFrmSharedgame(GameAllActivity.this, val6, "").equals("true") &&
                        Utils.getFrmSharedgame(GameAllActivity.this, val7, "").equals("true")
                ){
                /*startService(new Intent(GameAllActivity.this , Sample_service.class));
                btnVal = rootValue + "8";
                Utils.saveInShared(GameAllActivity.this, Const.btnVal, btnVal);
                timerAndWeb(datalist.get(8).getVisit_link());*/
                    Utils.savegame(GameAllActivity.this, val1, "false");
                    Utils.savegame(GameAllActivity.this, val2, "false");
                    Utils.savegame(GameAllActivity.this, val3, "false");
                    Utils.savegame(GameAllActivity.this, val4, "false");
                    Utils.savegame(GameAllActivity.this, val5, "false");
                    Utils.savegame(GameAllActivity.this, val6, "false");
                    Utils.savegame(GameAllActivity.this, val7, "false");
                    showDialogPoints(1,datalist.get(0).getVisit_coin(),true);

                }else{
                    Toast.makeText(GameAllActivity.this, "First Complete all steps", Toast.LENGTH_SHORT).show();
                }



            }
        });

      /*  textCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.addPoints(GameAllActivity.this, 8, 0);
                Toast.makeText(GameAllActivity.this, "Coins Added", Toast.LENGTH_SHORT).show();
                String val1 = rootValue + "1";
                String val2 = rootValue + "2";
                String val3 = rootValue + "3";
                String val4 = rootValue + "4";
                String val5 = rootValue + "5";
                String val6 = rootValue + "6";
                String val7 = rootValue + "7";
                String val8 = rootValue + "8";
                String valT = rootValue + "T";
                Utils.saveInShared(GameAllActivity.this, val1, "false");
                Utils.saveInShared(GameAllActivity.this, val2, "false");
                Utils.saveInShared(GameAllActivity.this, val3, "false");
                Utils.saveInShared(GameAllActivity.this, val4, "false");
                Utils.saveInShared(GameAllActivity.this, val5, "false");
                Utils.saveInShared(GameAllActivity.this, val6, "false");
                Utils.saveInShared(GameAllActivity.this, val7, "false");
                Utils.saveInShared(GameAllActivity.this, val8, "false");
                Utils.saveInShared(GameAllActivity.this, valT, "false");
            }
        });*/

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void seteverytasklight() {
        buttonPlayOne.setEnabled(true);
        //buttonPlayOne.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.white)));
        buttonPlayOne.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.one));

        buttonPlayTwo.setEnabled(true);
        buttonPlayTwo.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.two));

        buttonPlayThree.setEnabled(true);
        buttonPlayThree.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.three));

        buttonPlayFour.setEnabled(true);
        buttonPlayFour.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.four));

        buttonPlayFive.setEnabled(true);
        buttonPlayFive.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.five));

        buttonPlaySix.setEnabled(true);
        buttonPlaySix.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.six));

        buttonPlaySeven.setEnabled(true);
        buttonPlaySeven.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.seven));
    }

    private void showDialogPoints(final int addOrNoAddValue, final String points, boolean isShowDialog) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        if (Constant.isNetworkAvailable(this)) {
            if (addOrNoAddValue == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    imageView.setImageResource(R.drawable.sad);
                    title_text.setText(getResources().getString(R.string.better_luck));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else if (points.equals("no")) {
                    Log.e("TAG", "showDialogPoints: rewared cancel");
                    imageView.setImageResource(R.drawable.sad);
                    title_text.setText(getResources().getString(R.string.not_watch_full_video));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    imageView.setImageResource(R.drawable.congo);
                    title_text.setText(getResources().getString(R.string.you_won));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.add_to_wallet));
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                imageView.setImageResource(R.drawable.donee);
                title_text.setText(getResources().getString(R.string.today_chance_over));
                points_text.setVisibility(View.GONE);
                add_btn.setText(getResources().getString(R.string.okk));
            }

            add_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    Constant.addPoints(GameAllActivity.this, Integer.parseInt(points), 0);
                    clickButton="0";
                    dialog.dismiss();
                    seteverytasklight();
                    Toast.makeText(GameAllActivity.this, "Coins Added", Toast.LENGTH_SHORT).show();


                }
            });
        } else {
            Constant.showInternetErrorDialog(GameAllActivity.this, getResources().getString(R.string.no_internet_connection));
        }
        if (isShowDialog) {
            dialog.show();
        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void timerAndWeb(String link) {
        textCounter.setText(String.valueOf(value));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);

    }

    private void initlization() {
        buttonPlayOne = findViewById(R.id.one);
        buttonPlayTwo = findViewById(R.id.two);
        buttonPlayThree = findViewById(R.id.three);
        buttonPlayFour = findViewById(R.id.four);
        buttonPlayFive = findViewById(R.id.five);
        buttonPlaySix = findViewById(R.id.six);
        buttonPlaySeven = findViewById(R.id.seven);
        buttonPlayEight = findViewById(R.id.eight);

        textCounter= findViewById(R.id.textCounter);
    }

    public static void txtview(String str){
        textCounter.setText(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        //allRewardedShow();
        checkValidBtn();
        if (adscounter == 0){
            ShowStartappInterstital();
        }


        try {
            stopService(new Intent(GameAllActivity.this , Sample_service.class));
            stopService(new Intent(GameAllActivity.this , CustomTimerTask.class));

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private void allRewardedShow() {
        if (adType.equals("startapp")) {
            ShowStartappInterstital();
            adscounter=1;
        } else if (adType.equals("unity")){
            unityRewardedAd();
            adscounter=1;
        } else if (adType.equals("applovin")){
            loadApplovinRewarded();
            adscounter=1;
        }
    }
    public void unityRewardedAd () {
        UnityAds.show(GameAllActivity.this, Constant.getString(GameAllActivity.this, Constant.UNITY_REWARDED_ID), new UnityAdsShowOptions(), showListener);
    }
    private void unityStartAppInterstitialShow() {
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")) {
            unityInterstitialAd();
        } else if (adType.equalsIgnoreCase("applovin")) {
            showApplovinRewarded();
        }
    }
    public void unityInterstitialAd() {
        UnityAds.show(this, Constant.getString(this, Constant.UNITY_INTERSTITAL_ID), new UnityAdsShowOptions(), showListener);
    }
    private void ShowStartappInterstital() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd(new AdDisplayListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void adHidden(Ad ad) {
                    adscounter=1;
                    Updatestate();
                    checkValidBtn();



                }

                @Override
                public void adDisplayed(Ad ad) {
                    Log.i("doc","addisplayed");

                }

                @Override
                public void adClicked(Ad ad) {
                    Log.i("doc","ad clickde");

                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void adNotDisplayed(Ad ad) {
                    //LoadStartAppInterstital();
                    Log.i("doc","ad not displayed");
                    Updatestate();
                    checkValidBtn();
                }
            });
        } else {
            LoadStartAppInterstital();
        }
    }

    private void Updatestate() {
        if (!clickButton.equals("0")){
            if (textCounter.getText().toString().equals("0")){
                Utils.savegame(GameAllActivity.this, rootValue+clickButton, "true");
                //showDialogPoints(1,datalist.get(Integer.parseInt(clickButton)).getVisit_coin(),true);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkValidBtn(){
        String val1 = rootValue + "1";
        String val2 = rootValue + "2";
        String val3 = rootValue + "3";
        String val4 = rootValue + "4";
        String val5 = rootValue + "5";
        String val6 = rootValue + "6";
        String val7 = rootValue + "7";
        String val8 = rootValue + "8";
        String valT = rootValue + "T";


        if(Utils.getFrmSharedgame(GameAllActivity.this, val1, "").equals("true")){
            buttonPlayOne.setEnabled(false);
            buttonPlayOne.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val2, "").equals("true")){
            buttonPlayTwo.setEnabled(false);
            buttonPlayTwo.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val3, "").equals("true")){
            buttonPlayThree.setEnabled(false);
            buttonPlayThree.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val4, "").equals("true")){
            buttonPlayFour.setEnabled(false);
            buttonPlayFour.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val5, "").equals("true")){
            buttonPlayFive.setEnabled(false);
            buttonPlayFive.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val6, "").equals("true")){
            buttonPlaySix.setEnabled(false);
            buttonPlaySix.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val7, "").equals("true")){
            buttonPlaySeven.setEnabled(false);
            buttonPlaySeven.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, val8, "").equals("true")){
            buttonPlayEight.setEnabled(false);
            buttonPlayEight.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.blackTrans)));
            Utils.saveInShared(this, valT, "true");
        }

        if(Utils.getFrmSharedgame(GameAllActivity.this, valT, "").equals("true")){
            textCounter.setText("Collect Coins");
            textCounter.setEnabled(true);
        }


    }
    private void LoadStartAppInterstital() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(App.getContext());
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }

    /*private void loadApplovinBanner() {

        final LinearLayout layout = findViewById(R.id.adviewid);
        addviewt = new MaxAdView(Constant.getString(GameAllActivity.this, Constant.APPLOVIN_BANNER_ID), this);
        addviewt.setListener(GameAllActivity.this);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        final boolean isTablet = AppLovinSdkUtils.isTablet(this);
        final int heightPx = AppLovinSdkUtils.dpToPx(this, isTablet ? 90 : 50);
        addviewt.setLayoutParams(new LinearLayout.LayoutParams(width, heightPx));
        layout.addView(addviewt);

        addviewt.loadAd();
        addviewt.setVisibility(View.VISIBLE);
        addviewt.startAutoRefresh();
    }*/
    private void showApplovinInterstital(int position) {
        if (maxInterstitialAd.isReady()) {
            maxInterstitialAd.showAd();
        } else {
            loadApplovinInterstitial();
            startService(new Intent(GameAllActivity.this , Sample_service.class));
            value=Integer.parseInt(datalist.get(position).getVisit_timer());
            clickButton=String.valueOf(position);
            timerAndWeb(datalist.get(position).getVisit_link());
        }
    }


    private void loadApplovinInterstitial() {
        if (maxInterstitialAd == null) {
            maxInterstitialAd = new MaxInterstitialAd(Constant.getString(this, Constant.APPLOVIN_INTERSTITAL_ID), this);
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
    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
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

    private void loadApplovinRewarded() {
        if (maxRewardedAd == null) {
            maxRewardedAd = MaxRewardedAd.getInstance(Constant.getString(this, Constant.APPLOVIN_REWARDED_ID), this);
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
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }
}