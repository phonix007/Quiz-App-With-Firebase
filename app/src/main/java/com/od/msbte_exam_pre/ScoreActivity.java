package com.od.msbte_exam_pre;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ScoreActivity extends AppCompatActivity {
    private TextView scored,total;
    private Button doneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Changed color of actionbar and text of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500))); //Actionbar background color
        /* getActionBar() or */ getSupportActionBar() .setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        loadAds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scored = findViewById(R.id.scored);
        total = findViewById(R.id.total);
        doneBtn = findViewById(R.id.done_btn);

        scored.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        total.setText("OUT OF "+String.valueOf(getIntent().getIntExtra("total",0)));  //  seting data at final activty

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}