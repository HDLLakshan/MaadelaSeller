package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_orders);

        databaseOrder = FirebaseDatabase.getInstance().getReference("OrderClass");

        listVieworders = (ListView) findViewById(R.id.orderlistshow);

        olist = new ArrayList<>();
        name="FreshFish";


        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                olist.clear();
                for(DataSnapshot order : dataSnapshot.getChildren()){
                    OrderClass ordclass = order.getValue(OrderClass.class);
                    if(ordclass.getStatus().equals("Pending"))
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

        builder.setTitle("Accept or reject request ");
        builder.setCancelable(false);
        builder.setPositiveButton( "Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateasconfrim(j);
            }
        } ).setNegativeButton( "Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


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
                    olist.get(i).setSellerName(name);
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

    public void ViewConfirmOrderss(View view){
        Intent intent = new Intent( ShowOrders.this,OrderRequestNotification.class );
        startActivity( intent );
    }
}

