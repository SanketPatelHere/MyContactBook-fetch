package com.example.mycontactbook;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<Contacts> {
    private List<Contacts> phoneContactsList = new ArrayList<>();

    private List<Contacts> mOriginalValues;
    private ArrayList<Contacts> arraylist;

    Activity context;
    int layout;
    public CustomAdapter(Activity context, List<Contacts> lst)
    {
        super(context, R.layout.contact_list, lst);
        this.context = context;
        this.layout = R.layout.contact_list;
        this.phoneContactsList = lst;

        this.mOriginalValues = lst;

        this.arraylist = new ArrayList<Contacts>();
        this.arraylist.addAll(lst);
        Log.i("My  List2 = ",phoneContactsList+"");
        Log.i("My arraylist2 = ",arraylist+"");

    }

    @Override
    public int getCount() {
        return phoneContactsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(R.layout.contact_list, null, false);

        TextView phone_number;
        ImageView imgv;
        //if(convertView==null) {
            TextView name = (TextView)v.findViewById(R.id.name);
            phone_number = (TextView) v.findViewById(R.id.phone_number);
            imgv = (ImageView) v.findViewById(R.id.imgv);

            Contacts dp = phoneContactsList.get(position);

            name.setText(dp.getName());
            //Log.i("My Name = ",dp.getName());
            //Log.i("My Phone Number = ",dp.getPhone());
            //Log.i("My Image = ",dp.getImg()+"");
            phone_number.setText(dp.getPhone());
            //imgv.setImageResource(dp.getImage());
        //}
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("My constraint = ",constraint+"");

                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Contacts> FilteredArrList = new ArrayList<Contacts>();
                if(mOriginalValues==null)
                {
                    mOriginalValues = new ArrayList<Contacts>(phoneContactsList);
                    //save original data in mOriginalValues
                }
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                    Log.i("My count = ",results.count+"");
                    Log.i("My values = ",results.values+"");

                } else {
                    constraint = constraint.toString().toLowerCase();
                    Log.i("My constraint2 = ",constraint+"");
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        Log.i("My mOriginalValues = ",mOriginalValues+"");
                        String data = mOriginalValues.get(i).name;
                        Log.i("My data = ",data+"");
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Contacts(mOriginalValues.get(i).name,mOriginalValues.get(i).phone, mOriginalValues.get(i).getImg()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                phoneContactsList = (List<Contacts>) results.values; // has the filtered values
                Log.i("New constraint = ",constraint+"");
                Log.i("New results = ",results+"");
                Log.i("New values = ",results.values+"");
                Log.i("New List = ",phoneContactsList+"");
                //notifyDataSetChanged();
            }
        };
        return filter;
    }


    @Override
    public Contacts getItem(int position) {
        return phoneContactsList.get(position);
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        phoneContactsList.clear();
        if (charText.length() == 0) {
            phoneContactsList.addAll(arraylist);
        }
        else
        {
            for (Contacts wp : arraylist)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    phoneContactsList.add(wp);
                }
            }
        }
        Log.i("My filter = ",charText+"");
        Log.i("My New arraylist = ",arraylist+"");
        Log.i("My New List2 = ",phoneContactsList+"");
        notifyDataSetChanged();
    }
}
