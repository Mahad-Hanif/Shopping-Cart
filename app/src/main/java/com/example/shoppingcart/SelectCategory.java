package com.example.shoppingcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectCategory extends AppCompatActivity {
    private int [] arr = {0,0,0,0};
    private Boolean nextScreen = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Shopping_Cart", "Show Select Category");

        setContentView(R.layout.activity_select_category);

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

        CardView item1 = findViewById(R.id.g0);
        CardView item2 = findViewById(R.id.g1);
        CardView item3 = findViewById(R.id.g2);
        CardView item4 = findViewById(R.id.g3);

        final String isForResult = getIntent().getStringExtra("Order Data");

        if(isForResult != null)
        {
            switch (isForResult) {
                case "Papers":
                    onClickItem(0);
                    break;
                case "Other Stationery":
                    onClickItem(1);
                    break;
                case "Hard Drive":
                    onClickItem(2);
                    break;
                case "Others Waste Material":
                    onClickItem(3);
                    break;
                default:
                    break;
            }
        }

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Select First");
                onClickItem(0);
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Select Second");
                onClickItem(1);
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Select Third");
                onClickItem(2);
            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Select Fourth");
                onClickItem(3);
            }
        });

    }

    private void onClickItem(int temp)
    {
        arr[temp] = 1;

        for(int i=0;i<arr.length;i++)
        {
            if(temp != i)
            {
                if(arr[i] == 1)
                {
                    Log.d("Shopping_Cart", "Deselect "+i);
                    deselectItem(i);
                }
            }
            else
            {
                Log.d("Shopping_Cart", "Select "+i);
                selectItem(i);
            }
        }
        Button next = findViewById(R.id.next_button_select_item);
        next.setVisibility(View.VISIBLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Click to Form Fill");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GetPersonalData.class);
                nextScreen = true;
                startActivity(intent);
            }
        });
    }

    private void deselectItem(int temp)
    {
        ImageView imageView;
        switch (temp)
        {
            case 0:
                imageView = findViewById(R.id.papers);
                imageView.setImageResource(R.drawable.papers_black);
                break;
            case 1:
                imageView = findViewById(R.id.stationery);
                imageView.setImageResource(R.drawable.stationery_black);
                break;
            case 2:
                imageView = findViewById(R.id.hard_disk);
                imageView.setImageResource(R.drawable.hard_disk_black);
                break;
            case 3:
                imageView = findViewById(R.id.waste);
                imageView.setImageResource(R.drawable.waste_black);
                break;
        }
    }

    private void selectItem(int temp)
    {
        ImageView imageView;
        final SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sub_heading_personal_details), 0);
        switch (temp)
        {
            case 0:
                imageView = findViewById(R.id.papers);
                imageView.setImageResource(R.drawable.papers);
                preferences.edit().putString(getResources().getString(R.string.sub_heading_select_item), getResources().getString(R.string.items_paper)).apply();
                break;
            case 1:
                imageView = findViewById(R.id.stationery);
                imageView.setImageResource(R.drawable.stationery);
                preferences.edit().putString(getResources().getString(R.string.sub_heading_select_item), getResources().getString(R.string.items_stationery)).apply();
                break;
            case 2:
                imageView = findViewById(R.id.hard_disk);
                imageView.setImageResource(R.drawable.hard_disk);
                preferences.edit().putString(getResources().getString(R.string.sub_heading_select_item), getResources().getString(R.string.item_hard_drive)).apply();
                break;
            case 3:
                imageView = findViewById(R.id.waste);
                imageView.setImageResource(R.drawable.waste);
                preferences.edit().putString(getResources().getString(R.string.sub_heading_select_item), getResources().getString(R.string.items_other_1)).apply();
                break;
        }
    }
}