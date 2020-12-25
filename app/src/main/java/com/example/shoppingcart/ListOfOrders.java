package com.example.shoppingcart;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;

import com.example.shoppingcart.DAL.OrderDAL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class ListOfOrders extends AppCompatActivity implements ListView.OnItemClickListener,
        ListView.OnItemLongClickListener, SearchView.OnQueryTextListener {
    private ListView lstOrders;
    private ArrayList<Order> mOrders;
    private ListAdapter laOrders;
    private ArrayList<Order> selectedOrders;
    private SearchView sv;
    private Order data = new Order();
    private OrderDAL store = new OrderDAL();
    private static final String FILE_NAME = "orders";
    private Boolean nextScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Shopping_Cart", "Save Input Data to File");
        save(null);
        Log.d("Shopping_Cart", "Loading Data from File");

        mOrders = load();

        Log.d("Shopping_Cart", "Show Order List View");
        setContentView(R.layout.activity_list_of_orders);

        selectedOrders = new ArrayList<Order>();
        laOrders = new ListAdapter(this, mOrders, selectedOrders);

        lstOrders = (ListView) findViewById(R.id.lvOrders);
        lstOrders.setAdapter(laOrders);

        lstOrders.setOnItemClickListener(this);
        lstOrders.setOnItemLongClickListener(this);

        sv = (SearchView) findViewById(R.id.search_view);
        sv.setOnQueryTextListener(this);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Shopping_Cart", "Create New Order");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SelectCategory.class);
                nextScreen = true;
                startActivity(intent);
            }
        });
    }

    private String statmentToSend()
    {
        return "Order of Online Shopping"+"\n"+
                "Order Detail:" +"\n"+
                "Company Name: "+ data.getcName()+"\n"+
                "Category: "+ data.getCategory()+"\n"+
                "Name: "+ data.getfName()+ " "+ data.getlName()+"\n"+
                "Email: "+ data.getEmail()+"\n"+
                "Contact: "+ data.getNo()+"\n"+
                "Address: "+ data.getCity() +" ("+ data.getZip() +"), "+ data.getState()+"\n"+
                "No. of Boxes: "+ data.getBoxes()+"\n";
    }

    private void save(Order sOrder) {
        Log.d("Shopping_Cart", "Trying to save Order");
        SQLiteDatabase dbHandler = openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sub_heading_personal_details), MODE_PRIVATE);
        if (sOrder == null) {
            if (preferences.getString(getResources().getString(R.string.f_name), null) == null)
                return;
        }
        if (sOrder == null) {
            Log.d("Shopping_Cart", "Check Category save " +preferences.getString(getResources().getString(R.string.sub_heading_select_item), null));
            Log.d("Shopping_Cart", "First get All Data");
//            if(preferences.getString("Edit_Order_ID", null) != null) {
//                mOrders = load();
//                Log.d("Shopping_Cart", "Edit Order ID "+preferences.getString("Edit_Order_ID", null));
//                for(int i=0;i<mOrders.size();i++)
//                {
//                    if(mOrders.get(i).getId() == Integer.valueOf(preferences.getString("Edit_Order_ID", null)))
//                    {
//                        data = mOrders.get(i);
//                    }
//                }
//            }
            data.setfName(preferences.getString(getResources().getString(R.string.f_name), null));
            data.setlName(preferences.getString(getResources().getString(R.string.l_name), null));
            data.setEmail(preferences.getString(getResources().getString(R.string.email), null));
            data.setNo(preferences.getString(getResources().getString(R.string.no), null));
            data.setCity(preferences.getString(getResources().getString(R.string.city), null));
            data.setZip(preferences.getString(getResources().getString(R.string.zip), null));
            data.setState(preferences.getString(getResources().getString(R.string.state), null));
            data.setBoxes(preferences.getString(getResources().getString(R.string.possible_boxes), null));
            data.setcName(preferences.getString(getResources().getString(R.string.order_company_name), null));
            data.setCategory(preferences.getString(getResources().getString(R.string.sub_heading_select_item), null));
            data.setLocalDateTime(new Date().toString());

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
        }
        dbHandler.execSQL("CREATE TABLE IF NOT EXISTS OrderDetail" +
                "(orderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fName TEXT," +
                "lName TEXT," +
                "email TEXT," +
                "pNo TEXT," +
                "cName TEXT," +
                "zip TEXT," +
                "state TEXT," +
                "city TEXT," +
                "boxes TEXT," +
                "category TEXT," +
                "mLocalDateTime TEXT);");
        try {
            Log.d("Shopping_Cart", "Opened File in append Mode");
            Log.d("Shopping_Cart", "Write in file");
            if(preferences.getString("Edit_Order_ID", null) != null)
            {
                Log.d("Shopping_Cart", "Updating Order + "+store.write(data));
                dbHandler.execSQL("UPDATE OrderDetail SET " +
                        "fName = '"+data.getfName()+"'," +
                        "lName = '"+data.getlName()+"'," +
                        "email = '"+data.getEmail()+"'," +
                        "pNo = '"+data.getNo()+"'," +
                        "cName = '"+data.getcName()+"'," +
                        "zip = '"+data.getZip()+"'," +
                        "state = '"+data.getState()+"'," +
                        "city = '"+data.getCity()+"'," +
                        "boxes = '"+data.getBoxes()+"'," +
                        "category = '"+data.getCategory()+"'," +
                        "mLocalDateTime = '"+data.getLocalDateTime()+"'" +
                        " where orderID = "+Integer.valueOf(preferences.getString("Edit_Order_ID", null))+"");
                preferences.edit().remove("Edit_Order_ID").commit();
                Toast.makeText(this, "Order Updated", Toast.LENGTH_SHORT).show();
            }else {
                if (sOrder == null) {
                    sendSms(data.getNo());
                    sendNotification();
                    sendEmail();

                    Log.d("Shopping_Cart", "Store: " + (store.write(data)));
                    dbHandler.execSQL("INSERT INTO OrderDetail VALUES(NULL, '" + data.getfName() + "'," +
                            "            '" + data.getlName() + "'," +
                            "            '" + data.getEmail() + "'," +
                            "            '" + data.getNo() + "'," +
                            "            '" + data.getcName() + "'," +
                            "            '" + data.getZip() + "'," +
                            "            '" + data.getState() + "'," +
                            "            '" + data.getCity() + "'," +
                            "            '" + data.getBoxes() + "'," +
                            "            '" + data.getCategory() + "'," +
                            "            '" + data.getLocalDateTime() + "');");

                    Toast.makeText(this, "Order Saved", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.execSQL("INSERT INTO OrderDetail VALUES(NULL, '" + sOrder.getfName() + "'," +
                            "            '" + sOrder.getlName() + "'," +
                            "            '" + sOrder.getEmail() + "'," +
                            "            '" + sOrder.getNo() + "'," +
                            "            '" + sOrder.getcName() + "'," +
                            "            '" + sOrder.getZip() + "'," +
                            "            '" + sOrder.getState() + "'," +
                            "            '" + sOrder.getCity() + "'," +
                            "            '" + sOrder.getBoxes() + "'," +
                            "            '" + sOrder.getCategory() + "'," +
                            "            '" + sOrder.getLocalDateTime() + "');");
                }
            }
            preferences.edit().clear();
            preferences.edit().commit();

        }catch (Exception e)
        {
            Log.d("Shopping_Cart", "Error in Order Saved in DB"+ e);
            e.printStackTrace();
        }finally {
            Log.d("Shopping_Cart", "Order Saved in DB");
        }
    }

    private ArrayList<Order> load() {
        SQLiteDatabase dbHandler = openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        ArrayList<Order> loadOrders = new ArrayList<>();
        try {
            Log.d("Shopping_Cart", "Open File to Read");

            Log.d("Shopping_Cart", "Input Stream Reader in Read");

            Log.d("Shopping_Cart", "Buffered Reader in Read");

            Cursor c = dbHandler.rawQuery("SELECT orderID, fName, lName, email, pNo, cName, zip , state, city, boxes, category, mLocalDateTime from OrderDetail", null);
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                Order loadOrdersFromDB = new Order();
                loadOrdersFromDB.setId(c.getInt(0));
                loadOrdersFromDB.setfName(c.getString(1));
                loadOrdersFromDB.setlName(c.getString(2));
                loadOrdersFromDB.setEmail(c.getString(3));
                loadOrdersFromDB.setNo(c.getString(4));
                loadOrdersFromDB.setcName(c.getString(5));
                loadOrdersFromDB.setZip(c.getString(6));
                loadOrdersFromDB.setState(c.getString(7));
                loadOrdersFromDB.setCity(c.getString(8));
                loadOrdersFromDB.setBoxes(c.getString(9));
                loadOrdersFromDB.setCategory(c.getString(10));
                loadOrdersFromDB.setLocalDateTime(c.getString(11));
                loadOrders.add(loadOrdersFromDB);
                c.moveToNext();
            }
        } catch (Exception e)
        {
            Log.d("Shopping_Cart", "Error in load Orders from DB"+ e);
            e.printStackTrace();
        }finally {
            Log.d("Shopping_Cart", "Orders load from DB");
        }
        return loadOrders;
    }

    private void sendSms(String desNo) {
//        ActivityCompat.requestPermissions(ListOfOrders.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        try {
            Intent intent=new Intent(getApplicationContext(),ListOfOrders.class);
            PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
            Log.d("Shopping_Cart", "Trying to send msg");
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(desNo, null, statmentToSend(), pi,null);
        }catch (Exception e)
        {
            Log.d("Shopping_Cart", "Error in msg sending"+ e);
            e.printStackTrace();
        }
    }

    private void sendNotification()
    {
        Log.d("Shopping_Cart", "Trying to push Notification");
        Intent i = new Intent(this, ListOfOrders.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Order")
                .setContentText(statmentToSend())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    private void sendEmail()
    {
        try {
            Log.d("Shopping_Cart", "Trying to Email");
            GMailSender sender = new GMailSender("bcsf17a008@pucit.edu.pk", "BCSF17A008");
            sender.sendMail("New Order",
                    statmentToSend(), // Body
                    "bcsf17a008@pucit.edu.pk",
                    data.getEmail()); // Receiver
        } catch (Exception e) {
            Log.d("Shopping_Cart", "Error in sending Email");
            Log.d("Shopping_Cart", e.getMessage(), e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Shopping_Cart", "Single Click on Item");
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), OrderDone.class);
        intent.putExtra("Order Data", store.write(mOrders.get(position)));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Shopping_Cart", "LONG Click on Item");
        lstOrders.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        final Menu[] customMenu = new Menu[1];
        final boolean[] perfomAction = {false};

        lstOrders.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.d("Shopping_Cart", "Set Menu");
                customMenu[0] = menu;
                mode.getMenuInflater().inflate(R.menu.edit_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Log.d("Shopping_Cart", "on Prepare Action Mode");
                return true;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Log.d("Shopping_Cart", "on Item Checked State Changed");
                if (checked) {
                    selectedOrders.add(mOrders.get(position));
                    Log.d("Shopping_Cart", "While Checking Selected Order list size is "+selectedOrders.size());
                } else {
                    selectedOrders.remove(mOrders.get(position));
                    Log.d("Shopping_Cart", "While UNChecking Selected Order list size is "+selectedOrders.size());
                }

                laOrders.notifyDataSetChanged();

                if(lstOrders.getCheckedItemCount() > 1)
                    customMenu[0].findItem(R.id.edit).setVisible(false);
                else
                    customMenu[0].findItem(R.id.edit).setVisible(true);
                mode.setTitle(lstOrders.getCheckedItemCount() + " Selected");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete) {
                    perfomAction[0] = true;
                    Log.d("Shopping_Cart", "Delete Order");
                    showErrorDialog(getResources().getString(R.string.simple_alert_dialog), "Delete Order's");
                    mode.finish();
                    return true;
                }
                else if (item.getItemId() == R.id.edit)
                {
                    Log.d("Shopping_Cart", "Edit Order");
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), GetPersonalData.class);
                    Log.d("Shopping_Cart", "Check Category sent" +selectedOrders.get(0).getCategory());
                    intent.putExtra("Order Data", store.write(selectedOrders.get(0)));
                    nextScreen = true;
                    //deleteSelectedOrderFromFile();
                    SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sub_heading_personal_details), MODE_PRIVATE);
                    preferences.edit().putString("Edit_Order_ID", String.valueOf(selectedOrders.get(0).getId())).apply();

                    mode.finish();
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.d("Shopping_Cart", "on Destroy Action Mode");
                if(!perfomAction[0]) {
                    deleteSelectedOrderList();
                }
                perfomAction[0] = false;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        laOrders.getFilter().filter(newText);
        return false;
    }

    private void showErrorDialog(String msg, String title)
    {
        Log.d("Shopping_Cart", "Trying to show Dialog of "+msg);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedOrderFromFile();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Shopping_Cart", "NOT Delete Order");
                        deleteSelectedOrderList();
                    }
                })
                .show();
    }

    private void deleteSelectedOrderList()
    {
        for (int i=0; i<selectedOrders.size(); i++) {
            selectedOrders.remove(i);
        }
        laOrders.notifyDataSetChanged();
    }

    private void deleteSelectedOrderFromFile()
    {
        Log.d("Shopping_Cart", "CONFIRMED Delete Order");
        Log.d("Shopping_Cart", "Selected Order list size is "+selectedOrders.size());
        SQLiteDatabase dbHandler = openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        for (int i = 0; i < selectedOrders.size(); i++) {
            Log.d("Shopping_Cart", "Before Deleting Order Order list size is "+mOrders.size());
            dbHandler.execSQL("DELETE FROM OrderDetail WHERE orderID = "+selectedOrders.get(i).getId()+"");
            mOrders.remove(selectedOrders.get(i));
            Log.d("Shopping_Cart", "After Deleting Order Order list size is "+mOrders.size());
        }
        laOrders.notifyDataSetChanged();

        Log.d("Shopping_Cart", "CONFIRMED Delete Order and delete Selected Order List");
        deleteSelectedOrderList();

//        Log.d("Shopping_Cart", "CONFIRMED Delete Order, Create New File and Adding Data");
//        for(int i=0;i<mOrders.size();i++)
//        {
//            save(mOrders.get(i));
//        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d("Shopping_Cart", "Back to Main Activity");
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nextScreen = true;
        startActivity(intent);
        finish();

    }
}