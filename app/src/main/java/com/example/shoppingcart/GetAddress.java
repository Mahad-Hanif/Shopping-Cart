package com.example.shoppingcart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.shoppingcart.DAL.OrderDAL;

public class GetAddress extends AppCompatActivity {
    private Order data = new Order();
    private OrderDAL store = new OrderDAL();
    private Boolean nextScreen = false;

    private String blockCharacterSet = "()-_?<>.:;~#^|$%&*!@+=`\\/{}[]',\"";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Shopping_Cart", "Show Address Form");
        setContentView(R.layout.activity_get_address);

        final String isForResult = getIntent().getStringExtra("Order Data");

        if(isForResult != null)
            data = store.read(isForResult);

        final SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sub_heading_personal_details), MODE_PRIVATE);

        preferences.edit().remove(getResources().getString(R.string.city)).commit();
        preferences.edit().remove(getResources().getString(R.string.zip)).commit();
        preferences.edit().remove(getResources().getString(R.string.state)).commit();
        preferences.edit().remove(getResources().getString(R.string.possible_boxes)).commit();
        preferences.edit().remove(getResources().getString(R.string.order_company_name)).commit();
        preferences.edit().clear();
        preferences.edit().commit();

        final EditText cName = findViewById(R.id.input_company_name);
        cName.setFilters(new InputFilter[] { filter });
        cName.setText(data.getcName());

        final EditText zip = findViewById(R.id.input_zip_code);
        zip.setFilters(new InputFilter[] { filter });
        zip.setText(data.getZip());

        final EditText state = findViewById(R.id.input_state);
        state.setFilters(new InputFilter[] { filter });
        state.setText(data.getState());

        final EditText city = findViewById(R.id.input_city);
        city.setFilters(new InputFilter[] { filter });
        city.setText(data.getCity());

        final Spinner spinner = findViewById(R.id.no_of_boxes);
        if(isForResult != null) {
            switch (data.getBoxes()) {
                case "1–20 boxes":
                    spinner.setSelection(1);
                    break;
                case "50–100 boxes":
                    spinner.setSelection(2);
                    break;
                case "500–1000 boxes":
                    spinner.setSelection(3);
                    break;
                case "5000–10000 boxes":
                    spinner.setSelection(4);
                    break;
                default:
                    spinner.setSelection(0);
                    break;
            }
        }

        Button next = findViewById(R.id.next_button_done);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Trying to leave Address form Screen");
                boolean flag = true;
                if(cName.getText().toString().length()>0)
                {
                    if(!cName.getText().toString().contains("'")) {
                        preferences.edit().putString(getResources().getString(R.string.order_company_name), cName.getText().toString()).apply();
                        Log.d("Shopping_Cart", "Company Name Saved");
                    }
                    else
                    {
                        flag = false;
                        Log.d("Shopping_Cart", "Company Name NOT Saved due to ' charactor");
                        showErrorDialog("Don't use ' in Company Name!");
                    }
                }
                else
                {
                    flag = false;
                    Log.d("Shopping_Cart", "Company Name NOT Saved");
                    showErrorDialog("Company Name Field is Empty. Please Enter Company Name...!");
                }
                if(zip.getText().toString().length()>0)
                {
                    preferences.edit().putString(getResources().getString(R.string.zip), zip.getText().toString()).apply();
                    Log.d("Shopping_Cart", "ZIP Code Saved");
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "ZIP Code NOT Saved");
                        showErrorDialog("Zip Code Field is Empty. Please Enter Zip Code...!");
                    }
                }
                if(state.getText().toString().length()>0)
                {
                    preferences.edit().putString(getResources().getString(R.string.state), state.getText().toString()).apply();
                    Log.d("Shopping_Cart", "State Saved");
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "State NOT Saved");
                        showErrorDialog("State is Empty. Please Enter State...!");
                    }
                }
                if(city.getText().toString().length()>0)
                {
                    preferences.edit().putString(getResources().getString(R.string.city), city.getText().toString()).apply();
                    Log.d("Shopping_Cart", "City Saved");
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "City NOT Saved");
                        showErrorDialog("City No Field is Empty. Please Enter City...!");
                    }
                }

                String comp = "NA";
                String compData = spinner.getSelectedItem().toString();

                if(!compData.equals(comp))
                {
                    preferences.edit().putString(getResources().getString(R.string.possible_boxes), compData).apply();
                    Log.d("Shopping_Cart", "Boxes Saved");
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "Boxes NOT Saved");
                        showErrorDialog("No of boxes Field is Not Selected. Please Select No of Boxes...!");
                    }
                }
                if(flag) {
                    Log.d("Shopping_Cart", "Leaving the Address Form Screen");
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), ListOfOrders.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    nextScreen = true;
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button pre = findViewById(R.id.pre_button_not_done);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Back to Personal Data Screen");
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(), GetPersonalData.class);
//                data.setfName(preferences.getString(getResources().getString(R.string.f_name), null));
//                data.setlName(preferences.getString(getResources().getString(R.string.l_name), null));
//                data.setEmail(preferences.getString(getResources().getString(R.string.email), null));
//                data.setNo(preferences.getString(getResources().getString(R.string.no), null));
//                intent.putExtra("Order Data Pre", store.write(data));
//                nextScreen = true;
//                startActivity(intent);
                finish();
            }
        });
    }

    private void showErrorDialog(String msg)
    {
        Log.d("Shopping_Cart", "Trying to show Dialog of "+msg);
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(msg)
                .setPositiveButton("Ok",null)
                .setIcon(R.drawable.error)
                .show();
    }
}