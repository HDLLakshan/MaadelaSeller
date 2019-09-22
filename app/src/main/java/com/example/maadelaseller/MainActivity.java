package com.example.maadelaseller;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
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


                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                       System.out.println( FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() );
                    DatabaseReference readref = FirebaseDatabase.getInstance().getReference().child( "SellerUser" );
                    readref.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                                SellerUser s = userSnapshot.getValue(SellerUser.class);

                                if(s.getPhonenum().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){

                                    System.out.println( "lssssslslslsl"+ FirebaseAuth.getInstance().getCurrentUser());
                                    SharedPreferences preferences = getSharedPreferences( "shopname", MODE_PRIVATE );
                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putString( "username", s.getShopname() );
                                    editor.commit();

                                    Intent intent = new Intent(MainActivity.this, SellingFishItem.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    System.out.println( "eeeeeeeeerrrrrrrrrrrooooooo" );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );



                }else {
                      Intent intent = new Intent( MainActivity.this, SignUp.class );
                      startActivity( intent );
                      finish();
                }


            }
        }, 500 );
    }
}
