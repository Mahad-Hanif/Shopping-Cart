package com.example.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppingcart.DAL.OrderDAL;

public class OrderDone extends AppCompatActivity {
    private Order data = new Order();
    private OrderDAL store = new OrderDAL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Shopping_Cart", "Showing Order Confirm Screen");
        setContentView(R.layout.activity_order_done);

        String isForResult = getIntent().getStringExtra("Order Data");

        if(isForResult != null)
            data = store.read(isForResult);

        Log.d("Shopping_Cart", "Retrieving Data");

        TextView set = findViewById(R.id.show_company_name);
        set.setText("Company Name: "+ data.getcName());

        set = findViewById(R.id.show_category);
        set.setText("Category: "+ data.getCategory());

        set = findViewById(R.id.show_full_name);
        set.setText("Name: "+ data.getfName()+ " "+ data.getlName());

        set = findViewById(R.id.show_email);
        set.setText("Email: "+ data.getEmail());

        set = findViewById(R.id.show_contact);
        set.setText("Contact: "+ data.getNo());

        set = findViewById(R.id.show_address);
        set.setText("Address: "+ data.getCity() +" ("+ data.getZip() +"), "+ data.getState());

        set = findViewById(R.id.show_no_of_boxes);
        set.setText("No. of Boxes: "+ data.getBoxes().replace(" boxes",""));

        Button btn = findViewById(R.id.pre_button_order_detail);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}