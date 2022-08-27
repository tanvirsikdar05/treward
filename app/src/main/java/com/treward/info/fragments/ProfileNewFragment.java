package com.treward.info.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.treward.info.BuildConfig;
import com.treward.info.R;
import com.treward.info.activity.RedeemHistoryActivity;
import com.treward.info.utils.Constant;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileNewFragment extends Fragment {


    private TextInputEditText name_editText, email_editText, number_editText;
    private AppCompatButton update_profile_btn;
    private CircleImageView user_image;
    private Context activity;


    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private String imageUrl = "";

    private Bitmap bitmap;
    private String image;

    private CardView incomeCard, inviteCard, rankcard, feedbackCard,settingCard,terms;
    private TextView user_points_text_view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_new, container, false);


        user_points_text_view = view.findViewById(R.id.user_points_text_view);
        setPoints();
        name_editText = view.findViewById(R.id.name_pf_edit_text);
        email_editText = view.findViewById(R.id.email_pf_edit_text);
        number_editText = view.findViewById(R.id.number_pf_edit_text);
        user_image = view.findViewById(R.id.user_profile_update);
        update_profile_btn = view.findViewById(R.id.update_btn);

        incomeCard = view.findViewById(R.id.incomCard);
        inviteCard = view.findViewById(R.id.inviteCard);
        rankcard = view.findViewById(R.id.rankCard);
        feedbackCard = view.findViewById(R.id.feedbackCard);
        terms = view.findViewById(R.id.terms);
        settingCard = view.findViewById(R.id.settingCard);

        onClick();
        return view;
    }

    private void setPoints() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setPoints();
    }

    private void onClick() {
        name_editText.setText(Constant.getString(getContext(), Constant.USER_NAME));
//        email_editText.setText(Constant.getString(activity, Constant.USER_EMAIL));
//        number_editText.setText(Constant.getString(activity, Constant.USER_NUMBER));
//        String user_email = Constant.getString(activity, Constant.USER_EMAIL);
//        imageUrl = Constant.getString(activity, Constant.USER_IMAGE);
//        Glide.with(activity)
//                .load(BaseUrl.LOAD_USER_IMAGE + imageUrl)
//                .centerCrop()
//                .placeholder(R.drawable.profile)
//                .into(user_image);
//        user_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStoragePermissionGranted()) {
//                    SelectImage();
//                }
//            }
//        });




        incomeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.GotoNextActivity(getContext(), RedeemHistoryActivity.class, "");
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        feedbackCard.setOnClickListener(view -> {
            downloadAction(getResources().getString(R.string.telegram_link));
        });
        terms.setOnClickListener(view -> {
            downloadAction(getResources().getString(R.string.terms_and_condition_link));
        });

        inviteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        rankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadFragment(new ReferFragment());
            }
        });

    }
    private void downloadAction(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Failed to load.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loadFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main, fragment)
                .addToBackStack(null)
                .commit();
    }
}