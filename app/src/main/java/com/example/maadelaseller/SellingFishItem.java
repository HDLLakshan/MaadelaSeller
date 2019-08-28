package com.example.maadelaseller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class SellingFishItem extends Activity {

    private static final String[] Fish = new String[]{"Balaya", "Thora", "Thalmaha", "Tuuna"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_selling_fish_item );

        AutoCompleteTextView et = findViewById( R.id.fishname );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, Fish );
        et.setAdapter( adapter );
    }

    public void sendMessage(View view) {
        Intent intent = new Intent( this, MyShop.class );
        // EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity( intent );
    }
}
