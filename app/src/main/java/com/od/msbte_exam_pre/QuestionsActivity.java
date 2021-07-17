package com.od.msbte_exam_pre;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QuestionsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(); // add msbte-exam-preparation-default-rtdb   this path if gives error

    private TextView question,noIndicator;
    private LinearLayout optionCotainer;
    private Button shareBtn,nextBtn;
    private int count = 0;
    private List<QuestionModel> list;
    private int position = 0;
    private int score = 0;
    private String category;
    private int setNO;
    private Dialog loadingDialog;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Changed color of actionbar and text of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500))); //Actionbar background color
        /* getActionBar() or */ getSupportActionBar() .setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        loadAds();
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        optionCotainer = findViewById(R.id.options_container);
        shareBtn = findViewById(R.id.share_btn);
        nextBtn = findViewById(R.id.next_btn);

        category = getIntent().getStringExtra("category");
        setNO = getIntent().getIntExtra("setNO",1); // got the value from category model

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_cor));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

//        list = new ArrayList<>();
//        list.add(new QuestionModel("question 1","a","b","c","d","a"));
//        list.add(new QuestionModel("question 2","a","b","c","d","b"));
//        list.add(new QuestionModel("question 3","a","b","c","d","c"));
//        list.add(new QuestionModel("question 4","a","b","c","d","d"));
//        list.add(new QuestionModel("question 5","a","b","c","d","d"));
//        list.add(new QuestionModel("question 6","a","b","c","d","b"));


        list = new ArrayList<>();

        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNO").equalTo(setNO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(QuestionModel.class));
                }
                if (list.size()>0){
                    for (int i = 0; i<4; i++){
                        optionCotainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }
                    playAnim(question,0,list.get(position).getQuestion()); //when activity open 1st question will show
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position == list.size()){
                                if (mInterstitialAd!=null){
                                    Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                    scoreIntent.putExtra("score",score); // passing data from this activity to score activity
                                    scoreIntent.putExtra("total",list.size());
                                    startActivity(scoreIntent);
                                    finish();
                                    mInterstitialAd.show(QuestionsActivity.this);
                                    return;
                                }
                                //// Score Activity
                                Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                scoreIntent.putExtra("score",score); // passing data from this activity to score activity
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }
                            count = 0;
                            playAnim(question,0,list.get(position).getQuestion());
                        }
                    });
                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body = "Q." + list.get(position).getQuestion() + "\n" + "\n" + "(A) " +
                                    list.get(position).getOptionA() + "\n" + "(B) " +
                                    list.get(position).getOptionB() + "\n" + "(C) " +
                                    list.get(position).getOptionC() + "\n" + "(D) " +
                                    list.get(position).getOptionD() + "\n" +  "\n" +  "Download MSBTE Preparation app for more questions ";

                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Quizzer Challenge");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share via"));
                        }
                    });
                }else {
                    finish();
                    Toast.makeText(QuestionsActivity.this, "Questions Are Coming Soon...", Toast.LENGTH_SHORT).show(); // Add some message on toast
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });

    }

    private void playAnim(View view,int value,String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4){
                    String option = "";
                    if (count == 0){
                        option = list.get(position).getOptionA();
                    } else if (count == 1) {
                        option = list.get(position).getOptionB();
                    } else if (count == 2) {
                        option = list.get(position).getOptionC();
                    } else if (count == 3) {
                        option = list.get(position).getOptionD();
                    }
                    playAnim(optionCotainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            if (value == 0){
                try {
                    ((TextView)view).setText(data);
                    noIndicator.setText(position + 1 + "/" +list.size()); // default set and try executes only 1 times
                }catch (ClassCastException ex){
                    ((Button)view).setText(data);
                }
                view.setTag(data);
                playAnim(view, 1,data);
              }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void checkAnswer(Button selectedOption){
           enableOption(false);
           nextBtn.setEnabled(true);
           nextBtn.setAlpha(1);
           if (selectedOption.getText().toString().equals(list.get(position).getCorrectANS())){
               //correct
               score++;
               Button correctoption = (Button) optionCotainer.findViewWithTag(list.get(position).getCorrectANS());
               Button opA = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionA());
               Button opB = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionB());
               Button opC = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionC());
               Button opD = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionD());
               opA.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               opB.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               opC.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               opD.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Showing Correct Option With Green Color
//               correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); //Showing Correct option when user gives worng answer

           }else {
               //incorrect
               Button correctoption = (Button) optionCotainer.findViewWithTag(list.get(position).getCorrectANS());

               Button opA = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionA());
               Button opB = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionB());
               Button opC = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionC());
               Button opD = (Button) optionCotainer.findViewWithTag(list.get(position).getOptionD());

               opA.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               opB.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               opC.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               opD.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
               correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); //Showing Correct option when user gives worng answer
               selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000"))); // Showing Correct Option With Red Color


           }

    }
    private void enableOption(boolean enable){
        for (int i = 0; i<4; i++){
            optionCotainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionCotainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0FF9800")));
                // color get original again for next que. Ex If answer is b for 1st then it will also show for 2nd as b but we don't wont

            }
        }
    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        InterstitialAd.load(this,getString(R.string.intersitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, "Faild to load");
                        mInterstitialAd = null;
                    }
                });
    }
}