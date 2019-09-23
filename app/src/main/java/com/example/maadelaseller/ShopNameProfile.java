package com.example.maadelaseller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopNameProfile extends AppCompatActivity {

        String phonenumber;
        EditText shopname, address, accNo;
        Button button_register;
        ProgressBar progressBar;
        private FirebaseAuth firebaseAuth;
        DatabaseReference dbref;
        SellerUser su;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shop_name_profile);

            su = new SellerUser();
            shopname = (EditText)findViewById( R.id.txtt );
            address = (EditText)findViewById(R.id.add) ;
            accNo =(EditText)findViewById(R.id.accno);


            button_register=(Button)findViewById( R.id.button );

            phonenumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();


            button_register.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(TextUtils.isEmpty( shopname.getText().toString() ))
                        Toast.makeText( getApplicationContext(),"EnterShopName",Toast.LENGTH_LONG ).show();

                     else {
                        dbref = FirebaseDatabase.getInstance().getReference().child( "SellerUser" );
                        su.setId( FirebaseAuth.getInstance().getCurrentUser().toString() );
                        su.setPhonenum( phonenumber );
                        su.setShopname( shopname.getText().toString().trim() );
                        su.setAddress(address.getText().toString().trim());
                        su.setAccNo(accNo.getText().toString().trim());


                        dbref.child( su.getShopname() ).setValue( su );


                        SharedPreferences preferences = getSharedPreferences( "shopname", MODE_PRIVATE );




                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString( "username", su.getShopname() );
                        editor.putString( "Address", su.getAddress() );
                        editor.putString( "Accno", su.getAccNo() );
                        editor.commit();

                        Intent i = new Intent( ShopNameProfile.this, setLocation.class );
                        startActivity( i );
                    }
                }
            } );



         /*   findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(ShopNameProfile.this, setLocation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                }
            });*/
    }
}
