package com.od.msbte_exam_pre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private long backPressedTime;
    private Toast backToast;
//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.start_btn);
        //Changed color of actionbar and text of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500))); //Actionbar background color
        /* getActionBar() or */ getSupportActionBar() .setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        MobileAds.initialize(this);

        loadAds();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent = new Intent(MainActivity.this, CatrgorisActivity.class);
                startActivity(categoryIntent);
            }
        });
    }
    // Three Dot Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }

    // onclick actions are will open
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.refer){
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, " " + getString(R.string.app_name));
                String msg = "Download this app for MSCIT Practice \n http://play.google.com/store/apps/details?=" + BuildConfig.APPLICATION_ID+"\n\n"; // Change your message
                intent.putExtra(Intent.EXTRA_TEXT,msg);
                startActivity(Intent.createChooser(intent,"share via"));
            }catch (Exception e){
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else
             if (id == R.id.rateus){
                 // This will directly transfer to playstore no need to change something
                 try {
                     startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                 } catch (ActivityNotFoundException e){
                     startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()))); // remove id if error occrs 
                 }
            return true;
        }
        else
            if (id == R.id.prapo){
                //open privacy policy url
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://onkar.google.com"))); // Add privacy policy url
                }catch (Exception e){
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            return true;
        }
        else
            if (id == R.id.contact){
                // It will show all the dialog
                ExampleDialog exampleDialog = new ExampleDialog();
                exampleDialog.show(getSupportFragmentManager(),"My Message");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    @Override
    public void onBackPressed() { //double press to exit
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}