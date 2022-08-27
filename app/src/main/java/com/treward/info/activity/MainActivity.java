package com.treward.info.activity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.treward.info.R;
import com.treward.info.fragments.MainFragment;
import com.treward.info.fragments.ProfileNewFragment;
import com.treward.info.fragments.ReferFragment;
import com.treward.info.utils.Constant;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    BottomNavigationView bottomBar;
    MediaPlayer popupSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        popupSound = MediaPlayer.create(activity, R.raw.popup);


        loadFragment(new MainFragment());
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new MainFragment());
                        break;
                    case R.id.refer:
                        Constant.GotoNextActivity(activity, VideoActivity.class, "");
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case R.id.redeem:
                        Constant.GotoNextActivity(activity, GiftCardActivity.class, "");
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case R.id.profile:
                        loadFragment(new ProfileNewFragment());
                        break;
                }
                return true;
            }
        });
       /* bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        loadFragment(new MainFragment());
                        break;
                    case 1:
                        Constant.GotoNextActivity(activity, VideoActivity.class, "");
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case 2:
                        Constant.GotoNextActivity(activity, GiftCardActivity.class, "");
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case 3:
                        loadFragment(new ProfileNewFragment());
                        break;
                }
                return true;
            }
        });*/
        /*bottomBar.setOnItemReselectedListener(new OnItemReselectedListener() {
            @Override
            public void onItemReselect(int i) {
                switch (i) {
                    case 0:
                        loadFragment(new MainFragment());
                        break;
                    case 1:
                        loadFragment(new ReferFragment());
                        break;
                    case 2:
                        Constant.GotoNextActivity(activity, GiftCardActivity.class, "");
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case 3:
                        loadFragment(new ProfileNewFragment());
                        break;
                }
            }
        });*/

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (Constant.getString(activity, "exit").equalsIgnoreCase("exit")) {
            showExitDialog();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void showExitDialog() {
        popupSound.start();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to exit?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        finish();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
    }

}