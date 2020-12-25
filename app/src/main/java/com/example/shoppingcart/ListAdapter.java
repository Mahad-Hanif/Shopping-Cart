package com.example.shoppingcart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdapter extends ArrayAdapter implements Filterable {
    private Context context;
    private ArrayList<Order> orders;
    private ArrayList<Order> selectedOrders;
    private ArrayList<Order> originalData = new ArrayList<>();

    public ListAdapter(Context context, ArrayList<Order> orders, ArrayList<Order> selectedOrders) {
        super(context, R.layout.list_item, orders);
        this.context = context;
        this.orders = orders;
        this.selectedOrders = selectedOrders;
    }

    static class HolderView {
        public TextView pName;
        public TextView cName;
        public TextView stime;
        public ImageView imageView;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Order order = orders.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);

            HolderView listItem = new HolderView();
            listItem.pName = (TextView) convertView.findViewById(R.id.pName);
            listItem.cName = (TextView) convertView.findViewById(R.id.cName);
            listItem.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            listItem.stime = (TextView) convertView.findViewById(R.id.stime);

            convertView.setTag(listItem);
        }

        HolderView listItem = (HolderView) convertView.getTag();

        listItem.pName.setText(order.getfName()+" "+order.getlName());
        listItem.cName.setText(order.getCity() +" ("+ order.getZip() +"), "+ order.getState());

        if(order.getLocalDateTime() != null) {
            Log.d("Shopping_Cart", "Before Parsing: " + order.getLocalDateTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(order.getLocalDateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateFormat = new SimpleDateFormat("hh:mm aa");

            Log.d("Shopping_Cart", "Check error: " + date);

            Log.d("Shopping_Cart", "After Parsing: " + dateFormat.format(date));
            listItem.stime.setText(dateFormat.format(date));
        }

        String firstLetter = String.valueOf(order.getfName().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color = generator.getColor(order.getfName());

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(firstLetter, color);

        listItem.imageView.setImageDrawable(drawable);

        if (selectedOrders.contains(order)) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence nameSubStr) {
            Log.d("Shopping_Cart", "perform Filtering");
            FilterResults results = new FilterResults();
            if(originalData.size()>0) {
                Log.d("Shopping_Cart", "Restore Original Data"+originalData.size());
                orders = originalData;
            }
            else {
                for (int i = 0; i < orders.size(); i++) {
                    Log.d("Shopping_Cart", "Copying Data to Save Original Data" + orders.size());
                    originalData.add(orders.get(i));
                }
            }

            if (nameSubStr != null && nameSubStr.length() > 0) {

                ArrayList<Order> filterList = new ArrayList<>();
                for (int i = 0; i < originalData.size(); i++) {
                    String cFName = (originalData.get(i).getfName()+" "+originalData.get(i).getlName()).toUpperCase();
                    String CAddress = (originalData.get(i).getCity() +" ("+ originalData.get(i).getZip() +"), "+ originalData.get(i).getState()).toUpperCase();

                    nameSubStr= nameSubStr.toString().toUpperCase();

                    if ((cFName).contains(nameSubStr) || (CAddress).contains(nameSubStr)) {
                        filterList.add(originalData.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {
                Log.d("Shopping_Cart", "Search Field is Empty!...");
                results.count = originalData.size();
                results.values = originalData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence nameSubStr, FilterResults results) {
            Log.d("Shopping_Cart", "publish Results");
            orders = (ArrayList<Order>) results.values;
            notifyDataSetChanged();
        }

    };
}
