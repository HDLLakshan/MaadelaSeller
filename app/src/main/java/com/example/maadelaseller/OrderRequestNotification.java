package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestNotification extends AppCompatActivity {

    DatabaseReference oodatabaseOrder;
    ListView oolistVieworders;
    List<OrderClass> ooolist;
    DatabaseReference ooodbr;
    String ooname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request_notification);

        oodatabaseOrder = FirebaseDatabase.getInstance().getReference("OrderClass");

        oolistVieworders = (ListView) findViewById(R.id.orreqqlist);

        ooolist = new ArrayList<>();
        ooname="FreshFish";


        oodatabaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot oorder : dataSnapshot.getChildren()){
                    // ooolist.clear();
                    OrderClass oordclass = oorder.getValue(OrderClass.class);
                  //if(oordclass.getCustomerName().matches(ooname))
                    if(oordclass.getStatus().equals("Confirmed"))
                        ooolist.add(oordclass);
                }
                ArrayAdapter adapter = new OrderList(OrderRequestNotification.this,ooolist);
                oolistVieworders.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
  /*  public void ConfirmBox(int i){

        final int j = i;
        if(orderrequestl.get( i ).getStatus().equals( "Pending" )) {

            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            builder.setTitle( "Confirm Request" );

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
            nbutton.setBackgroundColor( Color.GREEN );
            Button pbutton = alertDialog.getButton( DialogInterface.BUTTON_POSITIVE );
            pbutton.setBackgroundColor( Color.RED );
        }
    }

    public void updateAsConfim(final int i){

        DatabaseReference uprefs = FirebaseDatabase.getInstance().getReference().child("OrderClass");
        uprefs.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(customersname));

                orderrequestl.get( i ).setStatus( "Confirmed" );
                odbr = FirebaseDatabase.getInstance().getReference().child("OrderClass").child( orderrequestl.get( i ).getId() );
                odbr.setValue(orderrequestl.get( i ));
                Toast.makeText(getApplicationContext(), "Update Succesfull As Confirmed",Toast.LENGTH_SHORT).show();
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
                if(dataSnapshot.hasChild(customersname));
                orderrequestl.get( i ).setStatus( "Reject" );
                odbr = FirebaseDatabase.getInstance().getReference().child("OrderClass").child( orderrequestl.get( i ).getId() );
                odbr.setValue(orderrequestl.get( i ));
                Toast.makeText(getApplicationContext(), "Update Sucessfull As Reject",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }*/

}

