package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestNotification extends Activity {

    DatabaseReference oodatabaseOrder;
    ListView oolistVieworders;
    List<OrderClass> ooolist;
    DatabaseReference ooodbr;
    String ooname;
    String sellcontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_request_notification);

        oodatabaseOrder = FirebaseDatabase.getInstance().getReference("OrderClass");

        oolistVieworders = (ListView) findViewById(R.id.orreqqlist);

        ooolist = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        ooname = preferences.getString( "username","" );


        oodatabaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot oorder : dataSnapshot.getChildren()){
                    // ooolist.clear();
                    OrderClass oordclass = oorder.getValue(OrderClass.class);
                    if(oordclass.getStatus().equals("Confirmed"))
                        if(oordclass.getSellerName().equals(ooname))
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


}

