package com.example.maadelaseller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private void clearControls(){
        Fishname.setText("");
        ratekg.setText("");

    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_selling_fish_item );

        //   AutoCompleteTextView et = findViewById( R.id.fishname );
        //   ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.activity_list_item, Fish );
        //    et.setAdapter( adapter );


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        TimeShopOpend = new SimpleDateFormat("HH:mm").format(new Date());

        Fishname = (EditText)findViewById( R.id.fishname );
        ratekg = (EditText)findViewById( R.id.rate );
        dailySelling = new DailySelling();
        shopname = "FreshFish1";

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
                dbRef.child(dailySelling.getFishname()).setValue(dailySelling);
                Toast.makeText(getApplicationContext(), " data saved Sussessfully ", Toast.LENGTH_SHORT).show();
                clearControls();
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


}
