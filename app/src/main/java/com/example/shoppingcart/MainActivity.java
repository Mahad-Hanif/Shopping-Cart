package com.example.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Boolean nextScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Shopping_Cart", "Start");
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sub_heading_personal_details), MODE_PRIVATE);

        preferences.edit().remove(getResources().getString(R.string.f_name)).commit();
        preferences.edit().remove(getResources().getString(R.string.l_name)).commit();
        preferences.edit().remove(getResources().getString(R.string.email)).commit();
        preferences.edit().remove(getResources().getString(R.string.no)).commit();
        preferences.edit().remove(getResources().getString(R.string.city)).commit();
        preferences.edit().remove(getResources().getString(R.string.zip)).commit();
        preferences.edit().remove(getResources().getString(R.string.state)).commit();
        preferences.edit().remove(getResources().getString(R.string.possible_boxes)).commit();
        preferences.edit().remove(getResources().getString(R.string.order_company_name)).commit();
        preferences.edit().remove(getResources().getString(R.string.sub_heading_select_item)).commit();
        preferences.edit().remove("Edit_Order_ID").commit();
        preferences.edit().clear();
        preferences.edit().commit();

        Button start = findViewById(R.id.next_button_main);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Click to show Select Category");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SelectCategory.class);
                nextScreen = true;
                startActivity(intent);
            }
        });

        Button records = findViewById(R.id.show_all_record);

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Click to show All Records");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ListOfOrders.class);
                nextScreen = true;
                startActivity(intent);
            }
        });
    }
}