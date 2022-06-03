package com.app.yellodriver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.GreetingsQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelEarning.EarningViewModel;
import com.app.yellodriver.model.ModelGreeting.GreetingsViewModel;
import com.app.yellodriver.model.ModelGreeting.ModelGreetings;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class GreetingActivity extends BaseActivity {
    private TextView txtTitle;
    private TextView txtDescription;
    private ImageView imgGreeting;
    private Button btnGo;
    private String shiftType;
    //    private String titleMessage;
    private String userId;

    private GreetingsViewModel greetingsViewModel;
    private CustomProgressDialog customProgressDialog;

    private ArrayList<String> listMessage = new ArrayList<>();
    private int randomNumber;
    private Input<String> context;
    private Input<Boolean> isActive = new Input<>(true, true);

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_greetings;
    }

    @Override
    protected void initializeComponents() {
        imgGreeting = findViewById(R.id.img_greeting);
        txtTitle = findViewById(R.id.txt_greeting_title);
        txtDescription = findViewById(R.id.txt_greeting_description);
        btnGo = findViewById(R.id.btn_greeting_go);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            YLog.e("Time is ", "Good Morning");
            shiftType = "Good Morning ";
            context = new Input<>("MORNING", true);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            YLog.e("Time is ", "Good Afternoon");
            shiftType = "Good Afternoon ";
            context = new Input<>("NOON", true);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            YLog.e("Time is ", "Good Evening");
            shiftType = "Good Evening ";
            context = new Input<>("EVENING", true);
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            YLog.e("Time is ", "Good Night");
            shiftType = "Good Night ";
            context = new Input<>("NIGHT", true);
        }

        ImageLoader.loadImage(GreetingActivity.this, imgGreeting, R.drawable.img_background, 0);

        btnGo.setOnClickListener(view -> {
            startActivity(new Intent(GreetingActivity.this, HomeActivity.class));
            finish();
        });

        greetingsViewModel = ViewModelProviders.of(GreetingActivity.this).get(GreetingsViewModel.class);
        greetingsViewModel.liveDataGreeting.observe(this, observerGreetings);

        customProgressDialog = new CustomProgressDialog(GreetingActivity.this);
        customProgressDialog.showDialog();

        userId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");
        GreetingsQuery greetingsQuery = new GreetingsQuery(context, isActive, userId);
        greetingsViewModel.getGreetings(greetingsQuery);
    }

    Observer<ModelGreetings.Data> observerGreetings = ytGreetings -> {

        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (ytGreetings != null) {
            if (ytGreetings.getYtUser() != null) {
                if (ytGreetings.getYtUser().size() > 0) {
                    txtTitle.append(Utils.getColoredString(shiftType, ContextCompat.getColor(this, R.color.brand_yellow)));
                    txtTitle.append(Utils.getColoredString(ytGreetings.getYtUser().get(0).getFullName(), ContextCompat.getColor(this, R.color.colorWhite)));
                    txtTitle.append(Utils.getColoredString(" !", ContextCompat.getColor(this, R.color.brand_yellow)));
                }
            }
        }

        if (ytGreetings.getYtGreeting() != null) {
            if (ytGreetings.getYtGreeting().size() > 0) {

                for (int i = 0; i < ytGreetings.getYtGreeting().size(); i++) {
                    if (ytGreetings.getYtGreeting().get(i).getContext().equals(context.value)) {
                        listMessage.add(ytGreetings.getYtGreeting().get(i).getMessage());
                    }
                }

                Random random = new Random();
                randomNumber = random.nextInt(ytGreetings.getYtGreeting().size());

                YLog.e("Random Number", randomNumber + "");

                txtDescription.setText(listMessage.get(randomNumber));
            }
        }
    };
}