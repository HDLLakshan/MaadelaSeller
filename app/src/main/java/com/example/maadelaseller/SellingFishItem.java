package com.example.maadelaseller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SellingFishItem extends Activity {

    private static final String[] Fish = new String[]{"Balaya", "Thora", "Thalmaha", "Tuuna"};

    private String shopname;
    private EditText Fishname;
    private EditText ratekg;
    private String DateShopOpend;
    private String TimeShopOpend;
    private DailySelling dailySelling;
    DatabaseReference dbRef;
    AutoCompleteTextView et;

    private void clearControls(){
        Fishname.setText("");
        ratekg.setText("");
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_selling_fish_item );

        et = findViewById( R.id.fishname );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1,Fish );
        et.setAdapter( adapter );


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        TimeShopOpend = new SimpleDateFormat("HH:mm").format(new Date());

        Fishname = (EditText)findViewById( R.id.fishname );
        ratekg = (EditText)findViewById( R.id.rate );
        dailySelling = new DailySelling();

        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        shopname = preferences.getString( "username","" );


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child( "Request" ).child( DateShopOpend ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child:children){
                     // DailySelling dailySelling = child.getValue(DailySelling.class);
//                            String shopname = dataSnapshot.child( DateShopOpend ).getValue().toString();

                    //  Toast.makeText( SearchNavi.this,"New Shop Opend", Toast.LENGTH_LONG).show();
                    NotificationManager notif=(NotificationManager)getSystemService( Context.NOTIFICATION_SERVICE);
                    Notification notify=new Notification.Builder
                            (getApplicationContext()).setContentTitle("New Shop Opend").setContentText(DateShopOpend).setSmallIcon(R.drawable.icon).build();

                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );




    }

    public void SaveSellingFishItem(View view){
        dbRef = FirebaseDatabase.getInstance().getReference().child("DailySelling").child(DateShopOpend ).child( shopname );
        try {
            if (TextUtils.isEmpty(Fishname.getText().toString()))
                Toast.makeText(getApplicationContext(), "please enter Fish Name", Toast.LENGTH_SHORT).show();

            else if (TextUtils.isEmpty(ratekg.getText().toString()))
                Toast.makeText(getApplicationContext(), "please enter an price of 0ne kg", Toast.LENGTH_SHORT).show();



            else {
                dailySelling.setShopName( shopname );
                dailySelling.setFishname(Fishname.getText().toString().trim());
                dailySelling.setRate(Double.parseDouble(ratekg.getText().toString().trim()));
                dailySelling.setDate(DateShopOpend);
                dailySelling.setTime(TimeShopOpend);

                //dbRef.push().setValue(dailySelling);
          DatabaseReference  newref     = dbRef.push();
                      String pushid = newref.getKey();
                      dailySelling.setId( pushid );
                      newref.setValue( dailySelling );
                Toast.makeText(getApplicationContext(), " data saved Sussessfully ", Toast.LENGTH_SHORT).show();
                clearControls();
                Intent intent = new Intent( SellingFishItem.this,SellingFishItem.class );
                startActivity( intent );
                finish();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid Rate", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Invalid !!!!!", Toast.LENGTH_SHORT).show();
        }
    }



    public void sendMessage(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( "Are You Add All selling fish item? " )
                .setCancelable( false )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SellingFishItem.this,MyShop.class );
                        startActivity( intent );
                    }
                } ).setNegativeButton( "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setBackgroundColor(Color.GREEN);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor( Color.RED);
    }

    public void gotoprofile(View view){
        Intent intent = new Intent(this, Profile.class);
      //  Intent intent = new Intent( this, SellingFishItem.class );
        //intent.putExtra("ID", location.getID());
        startActivity(intent);
    }


}
