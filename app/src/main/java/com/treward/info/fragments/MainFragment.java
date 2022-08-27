package com.treward.info.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyZone;
import com.bumptech.glide.Glide;
import com.startapp.sdk.adsbase.StartAppAd;
import com.treward.info.App;
import com.treward.info.GameAllActivity;
import com.treward.info.R;
import com.treward.info.activity.AppsActivity;
import com.treward.info.activity.GameActivity;
import com.treward.info.activity.GameLoader;
import com.treward.info.activity.QuizActivity;
import com.treward.info.activity.ScratchActivity;
import com.treward.info.activity.SpinActivity;
import com.treward.info.activity.VideoActivity;
import com.treward.info.activity.WatchAndEarnActivity;
import com.treward.info.utils.Constant;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;


public class MainFragment extends Fragment {

    private CardView referCardView, walletCardView, dailyCheckIn, silverCardView, platinumCardView, goldCardView;
    private Context activity;
    String isGhbNotExternalOpen;
    RelativeLayout reward_btn,
            telegram, instagram;
    StartAppAd startAppAd;
    private Context mContext;
    boolean isAdsShow = true;
    private ImageView game_banner, promotion_1, promotion_2, promotion_3, promotion_4;

    private LinearLayout promotion_height,game_layout,buttonPlayOne, buttonPlayTwo, buttonPlayThree, buttonPlayFour, buttonPlayFive, buttonPlaySix, buttonPlaySeven, buttonPlayEight;
    private ImageView buttonScratch, buttonPlaySpin, buttonWatch, buttonYoutube;
    private ImageView promote_btn, playgames_btn, hotoffer_btn;
    private TextView user_points_text_view;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //checkIpState();
        user_points_text_view = view.findViewById(R.id.user_points_text_view);
        setPoints();
        referCardView = view.findViewById(R.id.refer_cardView);
        walletCardView = view.findViewById(R.id.walletCardView);
        dailyCheckIn = view.findViewById(R.id.daily_check_in);
        promotion_height = view.findViewById(R.id.promotion_height);
        silverCardView = view.findViewById(R.id.silverCardView);
        platinumCardView = view.findViewById(R.id.platinumCardView);
        goldCardView = view.findViewById(R.id.goldCardView);
        promotion_1 = view.findViewById(R.id.promotion_1);
        promotion_2 = view.findViewById(R.id.promotion_2);
        game_banner = view.findViewById(R.id.game_banner);
        promotion_3 = view.findViewById(R.id.promotion_3);
        promotion_4 = view.findViewById(R.id.promotion_4);

        //Ahsan
//        buttonPlayOne, buttonPlayTwo, buttonPlayThree, buttonPlayFour, buttonPlayFive, buttonPlaySix, buttonPlaySeven, buttonPlayEight;


        buttonScratch = view.findViewById(R.id.buttonScratch);
        buttonPlaySpin = view.findViewById(R.id.buttonPlaySpin);
        buttonYoutube = view.findViewById(R.id.buttonYoutube);
        buttonWatch = view.findViewById(R.id.buttonWatch);
        game_layout = view.findViewById(R.id.game_layout);

        promote_btn = view.findViewById(R.id.promote_btn);
        playgames_btn = view.findViewById(R.id.playgames_btn);
        hotoffer_btn = view.findViewById(R.id.hotoffer_btn);

        buttonPlayOne = view.findViewById(R.id.buttonPlayOne);
        buttonPlayTwo = view.findViewById(R.id.buttonPlayTwo);
        buttonPlayThree = view.findViewById(R.id.buttonPlayThree);
        buttonPlayFour = view.findViewById(R.id.buttonPlayFour);
        buttonPlayFive = view.findViewById(R.id.buttonPlayFive);
        buttonPlaySix = view.findViewById(R.id.buttonPlaySix);
        buttonPlaySeven = view.findViewById(R.id.buttonPlaySeven);
        buttonPlayEight = view.findViewById(R.id.buttonPlayEight);
        telegram = view.findViewById(R.id.telegram);
        instagram = view.findViewById(R.id.instagram);
        isGhbNotExternalOpen = Constant.getString(getContext(), Constant.GAME_HOME_BANNER_GAME_ACTIVITY);
        ViewGroup.LayoutParams params = promotion_height.getLayoutParams();
        promotion_height.setLayoutParams(params);

        if (Constant.isNetworkAvailable(getActivity()) && Constant.isOnline(getActivity())) {
            String adType = Constant.getString(getActivity(), Constant.AD_TYPE);
            if (adType.equalsIgnoreCase("startapp")) {
                LoadStartAppInterstital();
            } else if (adType.equalsIgnoreCase("unity")) {
                //UnityAds.load(Constant.getString(getActivity(), Constant.UNITY_INTERSTITAL_ID), loadListener);

            }
        } else {
            Constant.showInternetErrorDialog(getActivity(), "Please Check your Internet Connection");
        }

        promotion1();
        promotion2();
        promotion3();
        promotion4();
        gameBanner();

        onClick();
        return view;
    }




    private void LoadStartAppInterstital() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(App.getContext());
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }
    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(getActivity(), Constant.getString(mContext, Constant.UNITY_INTERSTITAL_ID), new UnityAdsShowOptions(), showListener);
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
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
            isAdsShow = false;
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            isAdsShow = false;
        }
    };

    private void gameBanner() {
        Glide.with(mContext)
                .load(Constant.getString(mContext, Constant.HOME_GAME_BANNER))
                .into(game_banner);
        String isGameBannerShow = Constant.getString(mContext, Constant.GAME_HOME_BANNER_HIDE);
        if (!isGameBannerShow.equalsIgnoreCase("true")) {
            game_layout.setVisibility(View.GONE);
        }
    }

    private void promotion4() {
        String isPromotion4True = Constant.getString(mContext, Constant.IS_P4_ENABLED);
        String isInAppOpen4 = Constant.getString(mContext, Constant.IS_INAPP_OPEN4);
        if (isPromotion4True.equalsIgnoreCase("true")) {
            Glide.with(mContext)
                    .load(Constant.getString(mContext, Constant.P4_IMAGE))
                    .into(promotion_4);
            promotion_4.setOnClickListener(v -> {
                if (isInAppOpen4.equalsIgnoreCase("chrome_tab")) {
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                    openCustomTab(getActivity(), customIntent.build(), Uri.parse(Constant.getString(mContext, Constant.P4_LINK)));
                } else if (isInAppOpen4.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(mContext, Constant.P4_LINK));
                } else if (isInAppOpen4.equalsIgnoreCase("external_browser")) {
                    downloadAction(Constant.getString(mContext, Constant.P4_LINK));
                } else if (isInAppOpen4.equalsIgnoreCase("game_activity")) {
                    Constant.GotoNextActivity(mContext, GameActivity.class, "");
                }
            });
        } else {
            promotion_4.setVisibility(View.GONE);
        }
    }

    private void promotion3() {
        String isPromotion3True = Constant.getString(mContext, Constant.IS_P3_ENABLED);
        String isInAppOpen3 = Constant.getString(mContext, Constant.IS_INAPP_OPEN3);
        if (isPromotion3True.equalsIgnoreCase("true")) {
            Glide.with(mContext)
                    .load(Constant.getString(mContext, Constant.P3_IMAGE))
                    .into(promotion_3);
            promotion_3.setOnClickListener(v -> {
                if (isInAppOpen3.equalsIgnoreCase("chrome_tab")) {
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                    openCustomTab(getActivity(), customIntent.build(), Uri.parse(Constant.getString(mContext, Constant.P3_LINK)));
                } else if (isInAppOpen3.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(mContext, Constant.P3_LINK));
                } else if (isInAppOpen3.equalsIgnoreCase("external_browser")) {
                    downloadAction(Constant.getString(mContext, Constant.P3_LINK));
                } else if (isInAppOpen3.equalsIgnoreCase("game_activity")) {
                    Constant.GotoNextActivity(mContext, GameActivity.class, "");
                }
            });
        } else {
            promotion_3.setVisibility(View.GONE);
        }
    }

    private void promotion2() {
        String isPromotion2True = Constant.getString(mContext, Constant.IS_P2_ENABLED);
        String isInAppOpen2 = Constant.getString(mContext, Constant.IS_INAPP_OPEN2);

        if (isPromotion2True.equalsIgnoreCase("true")) {
            Glide.with(mContext)
                    .load(Constant.getString(mContext, Constant.P2_IMAGE))
                    .into(promotion_2);
            promotion_2.setOnClickListener(v -> {
                if (isInAppOpen2.equalsIgnoreCase("chrome_tab")) {
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                    openCustomTab(getActivity(), customIntent.build(), Uri.parse(Constant.getString(mContext, Constant.P2_LINK)));
                } else if (isInAppOpen2.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(mContext, Constant.P2_LINK));
                } else if (isInAppOpen2.equalsIgnoreCase("external_browser")) {
                    downloadAction(Constant.getString(mContext, Constant.P2_LINK));
                } else if (isInAppOpen2.equalsIgnoreCase("game_activity")) {
                    Constant.GotoNextActivity(mContext, GameActivity.class, "");
                }
            });
        } else {
            promotion_2.setVisibility(View.GONE);
        }
    }

    private void promotion1() {
        String isPromotion1True = Constant.getString(mContext, Constant.IS_P1_ENABLED);
        String isInAppOpen1 = Constant.getString(mContext, Constant.IS_INAPP_OPEN1);

        if (isPromotion1True.equalsIgnoreCase("true")) {
            Glide.with(mContext)
                    .load(Constant.getString(mContext, Constant.P1_IMAGE))
                    .into(promotion_1);
            promotion_1.setOnClickListener(v -> {
                if (isInAppOpen1.equalsIgnoreCase("chrome_tab")) {
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                    openCustomTab(getActivity(), customIntent.build(), Uri.parse(Constant.getString(mContext, Constant.P1_LINK)));
                } else if (isInAppOpen1.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(mContext, Constant.P1_LINK));
                } else if (isInAppOpen1.equalsIgnoreCase("external_browser")) {
                    downloadAction(Constant.getString(mContext, Constant.P1_LINK));
                } else if (isInAppOpen1.equalsIgnoreCase("game_activity")) {
                    Constant.GotoNextActivity(mContext, GameActivity.class, "");
                }
            });
        } else {
            promotion_1.setVisibility(View.GONE);
        }
    }
    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
    public void inAppAction(String url) {
        Intent intent = new Intent(mContext, GameLoader.class);
        intent.putExtra("GAME_PASSING", url);
        startActivity(intent);
    }

    private void setPoints() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
    }

    private void onClick() {

        buttonYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.GotoNextActivity(getActivity(), VideoActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });
        game_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGhbNotExternalOpen.equalsIgnoreCase("external")) {
                    downloadAction(Constant.getString(mContext, Constant.GAME_HOME_BANNER_LINK));
                } else if (isGhbNotExternalOpen.equalsIgnoreCase("inapp")) {
                    inAppAction(Constant.getString(mContext, Constant.GAME_HOME_BANNER_LINK));
                } else {
                    Constant.GotoNextActivity(mContext, GameActivity.class, "");
                    requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });

        buttonWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.GotoNextActivity(getActivity(), WatchAndEarnActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        buttonScratch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.GotoNextActivity(getActivity(), ScratchActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        buttonPlaySpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), SpinActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        promote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.GotoNextActivity(getActivity(), QuizActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        playgames_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.GotoNextActivity(getActivity(), GameActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        hotoffer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), AppsActivity.class, "");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        buttonPlayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "1");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonPlayTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "2");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        telegram.setOnClickListener(v -> {
            downloadAction(getResources().getString(R.string.telegram_url));
        });
        instagram.setOnClickListener(v -> {
            downloadAction(getResources().getString(R.string.instagram_url));
        });
        buttonPlayThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "3");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonPlayFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "4");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonPlayFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "5");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonPlaySix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "6");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonPlaySeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "7");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonPlayEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(getActivity(), GameAllActivity.class, "8");
                requireActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }
    public void downloadAction(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Failed to load.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setPoints();
    }
}