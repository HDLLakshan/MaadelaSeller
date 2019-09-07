package com.example.maadelaseller;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_main );

        Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
               // Intent intent = new Intent( MainActivity.this, SellingFishItem.class );
              //  startActivity( intent );
                //finish();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                final String DateShopOpend = df.format(c);
                String TimeShopOpend = new SimpleDateFormat("HH:mm").format(new Date());

             //   FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               // DatabaseReference reference = firebaseDatabase.getReference();

                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child( "DailySelling" ).
                        child( DateShopOpend );
                readRef.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     //   Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        if(dataSnapshot.hasChild("FreshFis")) {
                            Intent intent = new Intent( MainActivity.this, MyShop.class );
                            startActivity( intent );
                            finish();
                        }else {
                            Intent intent = new Intent( MainActivity.this, SellingFishItem.class );
                            startActivity( intent );
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
            }
        }, 2500 );
    }
}
