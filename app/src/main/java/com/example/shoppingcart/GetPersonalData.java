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

import com.example.shoppingcart.DAL.OrderDAL;

import java.util.regex.Pattern;

public class GetPersonalData extends AppCompatActivity {
    private Order data = new Order();
    private OrderDAL store = new OrderDAL();
    private Boolean nextScreen = false;
    private String blockCharacterSet = "-_()?<>.:;~#^|$%&*!@+=`\\/{}[]',\"";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private String emailBlockCharacterSet = "()-?<>:;~#^|$%&*!+=`\\/{}[]',\"";

    private InputFilter emailFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && emailBlockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Shopping_Cart", "Show Form of Get Personal Data");
        setContentView(R.layout.activity_get_personal_data);

        final String isForResult = getIntent().getStringExtra("Order Data");
        final String isForResultPre = getIntent().getStringExtra("Order Data Pre");

        final SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sub_heading_personal_details), MODE_PRIVATE);

        preferences.edit().remove(getResources().getString(R.string.f_name)).commit();
        preferences.edit().remove(getResources().getString(R.string.l_name)).commit();
        preferences.edit().remove(getResources().getString(R.string.email)).commit();
        preferences.edit().remove(getResources().getString(R.string.no)).commit();
        preferences.edit().remove(getResources().getString(R.string.city)).commit();
        preferences.edit().remove(getResources().getString(R.string.zip)).commit();
        preferences.edit().remove(getResources().getString(R.string.state)).commit();
        preferences.edit().remove(getResources().getString(R.string.possible_boxes)).commit();
        preferences.edit().remove(getResources().getString(R.string.order_company_name)).commit();
        preferences.edit().clear();
        preferences.edit().commit();

        if(isForResult != null) {
            data = store.read(isForResult);
            Log.d("Shopping_Cart", "Check Category get " +data.getCategory());
            preferences.edit().putString(getResources().getString(R.string.sub_heading_select_item), data.getCategory()).apply();
        }

        if(isForResultPre != null) {
            data = store.read(isForResultPre);
            Log.d("Shopping_Cart", "Check Category get " +data.getCategory());
            preferences.edit().putString(getResources().getString(R.string.sub_heading_select_item), data.getCategory()).apply();
        }

        final EditText fName = findViewById(R.id.input_f_name);
        fName.setFilters(new InputFilter[] { filter });
        fName.setText(data.getfName());

        final EditText lName = findViewById(R.id.input_l_name);
        lName.setFilters(new InputFilter[] { filter });
        lName.setText(data.getlName());

        final EditText email = findViewById(R.id.input_email);
        email.setFilters(new InputFilter[] { emailFilter });
        email.setText(data.getEmail());

        final EditText no = findViewById(R.id.input_no);
        no.setFilters(new InputFilter[] { filter });
        no.setText(data.getNo());

        Button next = findViewById(R.id.next_button_personal_data);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Next trying to show Address form");
                boolean flag = true;

                if(fName.getText().toString().length()>0)
                {
                    preferences.edit().putString(getResources().getString(R.string.f_name), fName.getText().toString()).apply();
                    Log.d("Shopping_Cart", "First Name Saved");
                }
                else
                {
                    flag = false;
                    Log.d("Shopping_Cart", "First Name NOT Saved");
                    showErrorDialog("First Name Field is Empty. Please Enter First Name...!");
                }
                if(lName.getText().toString().length()>0)
                {
                    preferences.edit().putString(getResources().getString(R.string.l_name), lName.getText().toString()).apply();
                    Log.d("Shopping_Cart", "Last Name Saved");
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "Last Name NOT Saved");
                        showErrorDialog("Last Name Field is Empty. Please Enter Last Name...!");
                    }
                }
                if(email.getText().toString().length()>0)
                {
                    String emailStr = email.getText().toString();
                    if(isValid(emailStr)) {
                        preferences.edit().putString(getResources().getString(R.string.email), email.getText().toString()).apply();
                        Log.d("Shopping_Cart", "Email Saved");
                    }
                    else
                    {
                        if(flag) {
                            flag = false;
                            Log.d("Shopping_Cart", "Email INVALID Saved");
                            showErrorDialog("Email Field is InValid. Please Enter Correct Email...!");
                        }
                    }
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "Email NOT Saved");
                        showErrorDialog("Email Field is Empty. Please Enter Email...!");
                    }
                }
                if(no.getText().toString().length()>0)
                {
                    preferences.edit().putString(getResources().getString(R.string.no), no.getText().toString()).apply();
                    Log.d("Shopping_Cart", "Number Saved");
                }
                else
                {
                    if(flag) {
                        flag = false;
                        Log.d("Shopping_Cart", "Number NOT Saved");
                        showErrorDialog("Contact No Field is Empty. Please Enter Contact No...!");
                    }
                }
                if(flag) {
                    Log.d("Shopping_Cart", "Almost there to show Address form");
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), GetAddress.class);
                    if(isForResult != null)
                        intent.putExtra("Order Data", isForResult);
                    nextScreen = true;
                    startActivity(intent);
                }
            }
        });

        Button pre = findViewById(R.id.pre_button_personal_data);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Shopping_Cart", "Back to Select Category Screen");
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(), SelectCategory.class);
//                intent.putExtra("Order Data", preferences.getString(getResources().getString(R.string.sub_heading_select_item), null));
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

    private static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}