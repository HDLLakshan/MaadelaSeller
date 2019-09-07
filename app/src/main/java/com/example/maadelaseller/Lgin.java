package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Lgin extends AppCompatActivity {
    EditText un,pw;
    String uname,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_lgin );

        un = (EditText)findViewById( R.id.uname );
        pw = (EditText)findViewById( R.id.pwd );


    }

    public void login(View view) {
        uname = un.getText().toString().trim();
        pwd = pw.getText().toString().trim();

        if (pwd.equals( "123" )) {
            SharedPreferences preferences = getSharedPreferences( "shopname", MODE_PRIVATE );
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString( "username", uname );
            editor.commit();

            // Intent intent = new Intent( Lgin.this,SellingFishItem.class );
            // startActivity( intent );

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat( "dd-MMM-yyyy" );
            final String DateShopOpend = df.format( c );

            DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child( "DailySelling" ).
                    child( DateShopOpend );
            readRef.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //   Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    if(dataSnapshot.hasChild(uname)) {
                        Intent intent = new Intent( Lgin.this, MyShop.class );
                        startActivity( intent );
                        finish();
                    }else {
                        Intent intent = new Intent( Lgin.this, SellingFishItem.class );
                        startActivity( intent );
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

        }
    }
}
