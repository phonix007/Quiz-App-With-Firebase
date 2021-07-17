package com.od.msbte_exam_pre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CatrgorisActivity extends AppCompatActivity {

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

//        myRef.setValue("Hello, World!");

    private Dialog loadingDialog;

    private RecyclerView recyclerView;
    private List<CategoryModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrgoris);
        //Changed color of actionbar and text of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500))); //Actionbar background color
        /* getActionBar() or */ getSupportActionBar() .setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));
        Toolbar toolbar = findViewById(R.id.toolbar);

        loadAds();

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_cor));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        recyclerView = findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

         list = new ArrayList<>();

         CatagoryAdapter adapter = new CatagoryAdapter(list);
        recyclerView.setAdapter(adapter);

        loadingDialog.show();
        myRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() { // This listner fetch values only one time other are fetch it realtime
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                 list.add(dataSnapshot1.getValue(CategoryModel.class));
               }
                adapter.notifyDataSetChanged();
              loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CatrgorisActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
// On fiberbase you can update or change or edit the category as much you want