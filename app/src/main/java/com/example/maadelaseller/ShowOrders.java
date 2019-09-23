package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ShowOrders extends Activity {
    DatabaseReference databaseOrder;
    ListView listVieworders;
    List<OrderClass> olist;
    DatabaseReference odbr;
    String shopname , sellContact;
    double fprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_orders);

        databaseOrder = FirebaseDatabase.getInstance().getReference("OrderClass");

        listVieworders = (ListView) findViewById(R.id.orderlistshow);

        olist = new ArrayList<>();


        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        shopname = preferences.getString( "username","" );

        getsellerPhonenum();

        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                olist.clear();
                for(DataSnapshot order : dataSnapshot.getChildren()){
                    OrderClass ordclass = order.getValue(OrderClass.class);
                    if(ordclass.getStatus().equals("Pending") )
                        olist.add(ordclass);
                }
                ArrayAdapter adapter = new OrderList(ShowOrders.this,olist);
                listVieworders.setAdapter(adapter);

                listVieworders.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println("dfsfsd");
                        //  Toast.makeText(getApplicationContext(),fishlist.get( i ).getFishname(),Toast.LENGTH_LONG ).show();
                        sendMessage(i);

                    }
                } );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void sendMessage(int i){
        final int j = i;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Accept Request");
        builder.setMessage("Requested amount : "+olist.get(i).getAmount());
        builder.setCancelable(false);

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setGravity( Gravity.CENTER_VERTICAL );
        input.setHint("Enter Rate " );
        input.setInputType( InputType.TYPE_CLASS_NUMBER );
        builder.setView(input);

        builder.setPositiveButton( "Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               updateasconfrim(i);
                updatePrice( j, Double.parseDouble( input.getText().toString().trim() ) );
            }
        } ).setNegativeButton( "Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateAsReject(i);
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



    public void updateasconfrim(final int i){

        DatabaseReference uprefs = FirebaseDatabase.getInstance().getReference().child("OrderClass");
        uprefs.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(olist.get(i).getId())) {
                    olist.get(i).setSellerName(shopname);
                    olist.get(i).setSellerContact(sellContact);
                    olist.get(i).setStatus("Confirmed");
                    odbr = FirebaseDatabase.getInstance().getReference().child("OrderClass").child(olist.get(i).getId());
                    odbr.setValue(olist.get(i));
                    Toast.makeText(getApplicationContext(), "Update Succesfull As Confirmed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    public void updateAsReject(final int i){
        DatabaseReference uprefs = FirebaseDatabase.getInstance().getReference().child("OrderClass");
        uprefs.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(olist.get(i).getId()));
                olist.get(i).setSellerName(shopname);
                olist.get(i).setSellerContact(sellContact);
                olist.get( i ).setStatus( "Reject" );
                odbr = FirebaseDatabase.getInstance().getReference().child("OrderClass").child( olist.get( i ).getId() );
                odbr.setValue(olist.get( i ));
                Toast.makeText(getApplicationContext(), "Update Sucessfull As Reject",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }



    public void getsellerPhonenum(){
        DatabaseReference rref = FirebaseDatabase.getInstance().getReference().child( "SellerUser" ).child(shopname );
        rref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                    sellContact = dataSnapshot.child( "phonenum" ).getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    public void updatePrice(final int j, final double d){

        final String m = olist.get( j ).getId();
        DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child( "OrderClass" );
        upref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild( m )) {
                    try {
                        olist.get( j ).setTotprice( d );

                        odbr = FirebaseDatabase.getInstance().getReference().child( "OrderClass" ).child( olist.get( j ).getId() );
                        //odbr.setValue( olist.get( j ) );
                         fprice = d * olist.get(j).getAmount();
                         olist.get(j).setTotprice(fprice);
                        odbr.setValue( olist.get( j ) );

                    } catch (NumberFormatException e) {
                        Toast t =Toast.makeText( getApplicationContext(), "Update Unsucesssfull", Toast.LENGTH_SHORT );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }

    public void ViewConfirmOrderss(View view){
        Intent intent = new Intent( ShowOrders.this,OrderRequestNotification.class );
        startActivity( intent );
    }
}