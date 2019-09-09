package com.example.maadelaseller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FiLo = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CoLo = Manifest.permission.ACCESS_COARSE_LOCATION ;
    private static final int Location_re_code = 1234;
    private Boolean mLocationG = false;
    private GoogleMap MMap;
    private FusedLocationProviderClient FLPC;
    LatLng set;
    DatabaseReference dbref;
    LocationAll location;
    String ID;
    Double lan;
    Double lon;
    String shopname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getLocationPermission();
        location = new LocationAll();
        //get ID from previous Intent
        Intent in = getIntent();
        ID = in.getStringExtra("ID");
        System.out.println("==============="+ID+"===============");

        SharedPreferences preferences = getSharedPreferences( "shopname",MODE_PRIVATE );
        shopname = preferences.getString( "username","" );

    }





    public void update(View view){
         Intent intent1 = new Intent(Profile.this,UpdateLocation.class);
        intent1.putExtra("ID", ID);

        startActivity(intent1);
    }
    public void Delete(View view){
        Intent intent1 = new Intent(Profile.this,Lgin.class);
        intent1.putExtra("ID", ID);
        startActivity(intent1);


        DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("location");
        delref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=1;i<=dataSnapshot.getChildrenCount();i++) {
                    System.out.println(ID);

                    if (ID.equals(dataSnapshot.child(String.valueOf(i)).child("id").getValue().toString())) {
                        dbref = FirebaseDatabase.getInstance().getReference().child(String.valueOf(i)).child(ID);
                        dbref.removeValue();
                        Toast.makeText(getApplicationContext(), "DeleteSucessfull", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onMapReady(GoogleMap gm) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "OnMapReady: map is ready");
        MMap = gm;
        MMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        //getDeviceLocation();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        MMap.setMyLocationEnabled(true);



        //get location
        DatabaseReference readref = FirebaseDatabase.getInstance().getReference().child("location");
        readref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(int i=1;i<=dataSnapshot.getChildrenCount();i++) {

                    if(ID.equals(dataSnapshot.child(String.valueOf(i)).child(shopname).getValue().toString())){

                    lan = Double.parseDouble( dataSnapshot.child(String.valueOf(i)).child("lan").getValue().toString());
                    lon = Double.parseDouble( dataSnapshot.child(String.valueOf(i)).child("lon").getValue().toString());
                    set = new LatLng(lan, lon);
                    MMap.addMarker(new MarkerOptions().position(set));
                    moveCamera(set, 15);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    private void getDeviceLocation(){
        Log.d(TAG,"device location");

        FLPC = LocationServices.getFusedLocationProviderClient(this);

        try{
            Task location = FLPC.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d(TAG,"found location");
                        Location current = (Location) task.getResult();

//                        System.out.println(current.getLatitude());

                        moveCamera(new LatLng(current.getLatitude(),current.getLongitude())
                                ,15f);

                    }else{
                        Log.d(TAG,"found location: null");
                        Toast.makeText(Profile.this,"Unable",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG,"getDevice location"+ e.getMessage());
        }

    }

    private void moveCamera(LatLng latLan,float zoom){
        Log.d(TAG,"MOVING...");

        MMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLan,zoom));
    }

    private void initMap(){
        Log.d(TAG,"Initializing Map");
        SupportMapFragment MF=( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        MF.getMapAsync(Profile.this);
    }


    private void getLocationPermission(){
        Log.d(TAG,"getting location");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FiLo)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),CoLo)== PackageManager.PERMISSION_GRANTED ){

                mLocationG=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions,Location_re_code);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,Location_re_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationG=false;

        switch(requestCode){
            case Location_re_code:{
                if(grantResults.length>0){
                    for(int i =0;i<grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationG=false;
                            Log.d(TAG,"Permission failed");
                            break;
                        }
                    }
                    Log.d(TAG,"Permission Granted");
                    mLocationG=true;
                    //intiate our map
                    initMap();
                }

            }
        }
    }






}


