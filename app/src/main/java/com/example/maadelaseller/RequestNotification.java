package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;

public class RequestNotification extends Activity {

    DatabaseReference dbref;

    ListView listViewRequest;
    List<Requests> requestsList;

    String DateShopOpend,shopname,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_request_notification );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);

        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        shopname = preferences.getString( "username","" );

        requestsList = new ArrayList<>(  );
        listViewRequest = (ListView)findViewById( R.id.rlist );
        dbref = FirebaseDatabase.getInstance().getReference("Request").child(DateShopOpend);

        dbref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear();

              try {
                  for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                      Requests requests = reqSnapshot.getValue( Requests.class );
                      if(requests.getShopname().equals( shopname ))
                      requestsList.add( requests );
                      System.out.println(  shopname +"errrrkkkkk");
                  }
                  RequestList adapter = new RequestList( RequestNotification.this, requestsList );
                  listViewRequest.setAdapter( adapter );
              }catch (Exception e){
                  System.out.println( "errrrooo" );
              }

              listViewRequest.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                      if(requestsList.get( i ).getStatus().equals( "Pending" ))
                      ConfirmBox( i );
                      else if(requestsList.get( i ).getStatus().equals( "Confirmed" )) {
                          getphonenum( i );
                      }
                  }
              } );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void ConfirmBox(int i){
        final int j = i;
       if(requestsList.get( i ).getStatus().equals( "Pending" )) {

           AlertDialog.Builder builder = new AlertDialog.Builder( this );
           builder.setTitle( "Confirm Request" );
           builder.setMessage( "Fish Name :" + requestsList.get( i ).getFishname() + " \n " + "Amount  :" +
                   requestsList.get( i ).getAmount() +"Kg" );
           builder.setCancelable( false );
           builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   updateAsConfim( j );
                   recreate();
               }
           } ).setNegativeButton( "No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   updateAsReject( j );
                   dialogInterface.dismiss();

               }
           } );
           AlertDialog alertDialog = builder.create();
           alertDialog.setCanceledOnTouchOutside( true );
           alertDialog.show();
           Button nbutton = alertDialog.getButton( DialogInterface.BUTTON_NEGATIVE );
           nbutton.setTextColor( Color.BLACK );
           Button pbutton = alertDialog.getButton( DialogInterface.BUTTON_POSITIVE );
           pbutton.setTextColor( Color.BLACK );
       }
    }

    public void updateAsConfim(final int i){
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("Request");
        upref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(DateShopOpend));
                requestsList.get( i ).setAcctime( time );
                requestsList.get( i ).setStatus( "Confirmed" );
                dbref = FirebaseDatabase.getInstance().getReference().child("Request").child( DateShopOpend ).child( requestsList.get( i ).getReqid() );
                dbref.setValue(requestsList.get( i ));
                Toast.makeText(getApplicationContext(), "Update Succesfull As Confirmed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    public void updateAsReject(final int i){
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("Request");
        upref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(DateShopOpend));
                requestsList.get( i ).setAcctime( time );
                requestsList.get( i ).setStatus( "Reject" );
                dbref = FirebaseDatabase.getInstance().getReference().child("Request").child( DateShopOpend ).child( requestsList.get( i ).getReqid() );
                dbref.setValue(requestsList.get( i ));
                Toast.makeText(getApplicationContext(), "Update Sucessfull As Reject",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    public void updateAsSold(final int i){
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("Request");
        upref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(DateShopOpend));
                requestsList.get( i ).setAcctime( time );
                requestsList.get( i ).setStatus( "Sold" );
                dbref = FirebaseDatabase.getInstance().getReference().child("Request").child( DateShopOpend ).child( requestsList.get( i ).getReqid() );
                dbref.setValue(requestsList.get( i ));
                Toast.makeText(getApplicationContext(), "Update Sucessfull As Reject",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

     public void callBox(int i){
        final int j = i;
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Customer  Details" );
        builder.setMessage( "Customer Name  : "+ requestsList.get( i ).getCusname() +"\n Tel             : "+phoneNumber );




        builder.setPositiveButton( "Call Customer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent( Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
            }
        } );
         builder.setNegativeButton( "Sold", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 updateAsSold( j );
             }
         } );

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
         Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
         nbutton.setTextColor( Color.BLACK);


     }

    public void getphonenum(int i){
        final int j = i;
        DatabaseReference rref = FirebaseDatabase.getInstance().getReference().child( "User" ).child( requestsList.get( i ).getCusname() );
       rref.addListenerForSingleValueEvent( new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChildren())
                   phoneNumber = dataSnapshot.child( "contactNo" ).getValue().toString();
               callBox( j );
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       } );
    }





}
