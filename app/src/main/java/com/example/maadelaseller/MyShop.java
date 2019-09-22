package com.example.maadelaseller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        setContentView( R.layout.activity_my_shop );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);

        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        shopname = preferences.getString( "username","" );

        databaseFish = FirebaseDatabase.getInstance().getReference("DailySelling").child(DateShopOpend).child(shopname);
        listviewfish = (ListView)findViewById( R.id.fishlist );
        fishlist = new ArrayList<>(  );

        databaseFish.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fishlist.clear();
                for(DataSnapshot fishSnapshot : dataSnapshot.getChildren()){
                    DailySelling dailySelling = fishSnapshot.getValue(DailySelling.class);
                    fishlist.add( dailySelling );
                }
                FishList adapter = new FishList( MyShop.this,fishlist );
                listviewfish.setAdapter( adapter );

                listviewfish.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        DialogBox(i);

                    }
                } );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( MyShop.this,SellingFishItem.class );
        startActivity( intent );
    }

    public void DialogBox(int i) {
        final int j = i;

        AlertDialog.Builder builder = new AlertDialog.Builder( this );

         builder.setTitle( "Delete or Update Rate" );
          builder.setMessage( "Fish Name :"+fishlist.get( i ).getFishname()+" \n " +"Rate :"+fishlist.get( i ).getRate());
          builder.setCancelable( false );
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setGravity( Gravity.CENTER_VERTICAL );
        input.setHint("Enter New Rate " );
        input.setInputType( InputType.TYPE_CLASS_NUMBER );
        builder.setView(input);

       // input.setText( Double.toString( fishlist.get( i ).getRate()) );
          builder.setPositiveButton( "Delete Fish Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    delete( j );
                    }
                } ).setNegativeButton( "Update Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().equals( "" )) {
                  Toast t = Toast.makeText( getApplicationContext(), "Please Enter New Rate", Toast.LENGTH_SHORT );
                  designtoast( t );
                }
                else {
                    update( j, Double.parseDouble( input.getText().toString().trim() ) );
                    dialogInterface.dismiss();
                }
            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor( Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
    }

    public void update(final int j, final double d){

            final String m = fishlist.get( j ).getDate();
            DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child( "DailySelling" );
            upref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild( m )) {
                        try {
                            fishlist.get( j ).setRate( d );

                            dbref = FirebaseDatabase.getInstance().getReference().child( "DailySelling" ).child( fishlist.get( j ).getDate() ).child( fishlist.get( j ).getShopName() ).child( fishlist.get( j ).getId() );
                            dbref.setValue( fishlist.get( j ) );
                            Toast t = Toast.makeText( getApplicationContext(), "Update Sucessfull", Toast.LENGTH_SHORT );
                            designtoast( t );

                        } catch (NumberFormatException e) {
                            Toast t =Toast.makeText( getApplicationContext(), "Update Unsucesssfull", Toast.LENGTH_SHORT );
                            designtoast( t );
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );


    }

    public void delete(final int i){
        final String m = fishlist.get( i ).getDate();
        DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("DailySelling");
        delref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(m)){
                    dbref=FirebaseDatabase.getInstance().getReference().child("DailySelling").child(fishlist.get( i ).getDate()).child(fishlist.get( i ).getShopName()).child( fishlist.get( i ).getId());
                    dbref.removeValue();
                    //clearcontrol();
                    Toast t = Toast.makeText(getApplicationContext(), "Delete Sucessfull",Toast.LENGTH_SHORT);
                    designtoast( t );

                }else {
                    Toast t = Toast.makeText( getApplicationContext(), "Delete Unsucessfull", Toast.LENGTH_SHORT );
                    designtoast( t );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void gonotification(View view){
        Intent intent = new Intent(MyShop.this,RequestNotification.class );
        startActivity( intent );
    }

     public void deletemyshop(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( "Are You Sure Close Your Shop " )
                .setCancelable( false )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("DailySelling").child( DateShopOpend ).child( shopname );
                        delref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dbref=FirebaseDatabase.getInstance().getReference().child("DailySelling").child( DateShopOpend ).child( shopname );
                                dbref.removeValue();
                               Toast t = Toast.makeText(getApplicationContext(), "Shop Close Sucessfully",Toast.LENGTH_SHORT);
                               designtoast( t );


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Intent intent = new Intent(MyShop.this,SellingFishItem.class );
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
        pbutton.setTextColor( Color.BLACK);




    }


    public void viewCustomerOrders(View view){
        Intent intent = new Intent(MyShop.this,ShowOrders.class );
        startActivity( intent );
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
