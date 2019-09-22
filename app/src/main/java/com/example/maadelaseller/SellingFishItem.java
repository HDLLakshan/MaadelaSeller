package com.example.maadelaseller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        setContentView( R.layout.activity_selling_fish_item );

        et = findViewById( R.id.fishname );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1,FishItemNames.Fish );
        et.setAdapter( adapter );

        et.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // name=FishItemNames.Fish[i];
                ratekg.requestFocus();
            }
        } );


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        TimeShopOpend = new SimpleDateFormat("HH:mm").format(new Date());

        Fishname = (EditText)findViewById( R.id.fishname );
        ratekg = (EditText)findViewById( R.id.rate );
        dailySelling = new DailySelling();

        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        shopname = preferences.getString( "username","" );





    }

    public void SaveSellingFishItem(View view){
        dbRef = FirebaseDatabase.getInstance().getReference().child("DailySelling").child(DateShopOpend ).child( shopname );
        try {
            if (TextUtils.isEmpty(Fishname.getText().toString())) {
               Toast t = Toast.makeText( getApplicationContext(), "Please enter Fish Name", Toast.LENGTH_SHORT );
               designtoast( t );
            }
            else if
                (TextUtils.isEmpty( ratekg.getText().toString() )){
               Toast t = Toast.makeText( getApplicationContext(), "Please enter an price of One kg", Toast.LENGTH_SHORT );
               designtoast( t );
            }


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

               Toast t = Toast.makeText(getApplicationContext(), " data saved Sussessfully ", Toast.LENGTH_SHORT);
               designtoast( t );

                clearControls();
                et.requestFocus();

            }
        } catch (NumberFormatException e) {
           Toast t = Toast.makeText(getApplicationContext(), "Invalid Rate", Toast.LENGTH_SHORT);
           designtoast( t );
        }catch (Exception e){
            Toast  t= Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT);
            designtoast( t );
        }
    }



    public void GoToMyshop(View view) {

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
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor ( Color.BLACK);
    }

    public void gotoprofile(View view){
        Intent intent = new Intent(this, Profile.class);
      //  Intent intent = new Intent( this, SellingFishItem.class );
        //intent.putExtra("ID", location.getID());
        startActivity(intent);
    }

    public void designtoast(Toast toast){
        toast.setGravity( Gravity.BOTTOM, 0, 0);
        View view = toast.getView();
        view.setBackgroundColor(Color.BLACK);
        TextView text = (TextView) view.findViewById(android.R.id.message);

        //Shadow of the Of the Text Color
        text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
        text.setTextColor(Color.WHITE);
         text.setTextSize(16);
        toast.show();
    }

}
