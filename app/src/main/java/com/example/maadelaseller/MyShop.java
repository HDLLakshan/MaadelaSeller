package com.example.maadelaseller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyShop extends Activity {

    ListView listviewfish;
    DatabaseReference databaseFish;
    List<DailySelling> fishlist;
    private String shopname;
    private String DateShopOpend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_my_shop );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        shopname = "FreshFish";
        databaseFish = FirebaseDatabase.getInstance().getReference("DailySelling").child(DateShopOpend).child(shopname);
        listviewfish = (ListView)findViewById( R.id.fishlist );
        fishlist = new ArrayList<>(  );

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseFish.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot fishSnapshot : dataSnapshot.getChildren()){
                    DailySelling dailySelling = fishSnapshot.getValue(DailySelling.class);
                    fishlist.add( dailySelling );
                }
                FishList adapter = new FishList( MyShop.this,fishlist );
                listviewfish.setAdapter( adapter );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
