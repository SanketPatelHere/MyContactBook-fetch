package com.example.mycontactbook;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListPhoneContactsActivity extends AppCompatActivity {
    private List<String> phoneContactsList = new ArrayList<>();
    private List<Contacts> list = new ArrayList<>();
    List<Contacts> filterList = new ArrayList<>();

    private ArrayAdapter<String> contactsListDataAdapter;
    CustomAdapter customAdapter;
    ListView contactsListView = null;
    MenuItem search;
    SearchView searchView;
    Contacts pojo;
    private int PERMISSION_REQUEST_CODE_READ_CONTACTS = 1;
    private int PERMISSION_REQUEST_CODE_WRITE_CONTACTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_phone_contacts);
        contactsListView = (ListView)findViewById(R.id.lv);

        try {
            //list.add(new Contacts("aaa","aaa",R.drawable.ic_launcher_background+""));
            customAdapter = new CustomAdapter(this, list);
            contactsListView.setAdapter(customAdapter);
        }
        catch (Exception e)
        {
            Log.i("My Error5 = ",e+"");
        }
        //contactsListDataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, phoneContactsList);
        //contactsListView.setAdapter(contactsListDataAdapter);

        Button addPhoneContactsButton = (Button)findViewById(R.id.add_phone_contacts_button);
        addPhoneContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListPhoneContactsActivity.this, AddPhoneContactActivity.class);
                startActivity(i);
                Log.i("My Add1 = ","go to add contact");


                /////comment this
                if(!hasPhoneContactsPermission(Manifest.permission.WRITE_CONTACTS))
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSION_REQUEST_CODE_WRITE_CONTACTS);
                    }
                    else
                    {
                        //AddPhoneContactActivity.start(getApplicationContext());
                        Intent i2 = new Intent(ListPhoneContactsActivity.this, AddPhoneContactActivity.class);
                        startActivity(i2);
                        Log.i("My Add2 = ","go to add contact");
                    }
                }
            }
        });

        Button readPhoneContactsButton = (Button)findViewById(R.id.read_phone_contacts_button);
        readPhoneContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
                {
                    requestPermission(Manifest.permission.READ_CONTACTS, PERMISSION_REQUEST_CODE_READ_CONTACTS);
                }else
                {
                    try
                    {
                        Log.i("My readPhoneContacts", " 1");
                        readPhoneContacts();
                    }
                    catch (Exception e)
                    {
                        Log.i("My Error = ",e+"");
                    }
                }
            }
        });

    }


    private boolean hasPhoneContactsPermission(String permission)
    {
        boolean ret = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if(hasPermission == PackageManager.PERMISSION_GRANTED)
            {
                ret = true;
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }
    private void requestPermission(String permission, int requestCode)
    {
        String requestPermissionArray[] = {permission};
        ActivityCompat.requestPermissions(this, requestPermissionArray, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int length = grantResults.length;
        if(length>0)
        {
            int grantResult = grantResults[0];
            if(grantResult == PackageManager.PERMISSION_GRANTED) {

                if(requestCode==PERMISSION_REQUEST_CODE_READ_CONTACTS)
                {
                    // If user grant read contacts permission.
                    try
                    {
                        Log.i("My readPhoneContacts", " 2");
                        readPhoneContacts();
                    }
                    catch (Exception e)
                    {
                        Log.i("My Error2 = ",e+"");
                    }
                }else if(requestCode==PERMISSION_REQUEST_CODE_WRITE_CONTACTS)
                {
                    // If user grant write contacts permission then start add phone contact activity.
                    //AddPhoneContactActivity.start(getApplicationContext());
                    try
                    {
                        Intent i = new Intent(ListPhoneContactsActivity.this, AddPhoneContactActivity.class);
                        startActivity(i);
                    }
                    catch (Exception e)
                    {
                        Log.i("My Error3 = ",e+"");
                    }

                }
            }else
            {
                Toast.makeText(getApplicationContext(), "You denied permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void readPhoneContacts()
    {
        //int size = phoneContactsList.size();
        try {

            int size = list.size();
            for (int i = 0; i < size; i++) {
                //phoneContactsList.remove(i);
                list.remove(i);
                i--;
                //size = phoneContactsList.size();
                size = list.size();
            }
            Uri readContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = getContentResolver().query(readContactsUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                try {

                    do {
                        int dislpayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String userDisplayName = cursor.getString(dislpayNameIndex);

                        int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = cursor.getString(phoneNumberIndex);
                        int image = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
                        String userImage = cursor.getString(image);

                        String phoneTypeStr = "Mobile";
                        int phoneTypeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                        int phoneTypeInt = cursor.getInt(phoneTypeColumnIndex);
                        if (phoneTypeInt == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                            phoneTypeStr = "Home";
                        } else if (phoneTypeInt == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            phoneTypeStr = "Mobile";
                        } else if (phoneTypeInt == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                            phoneTypeStr = "Work";
                        }
                        StringBuffer contactStringBuf = new StringBuffer();
                        contactStringBuf.append(userDisplayName);
                        contactStringBuf.append("\r\n");
                        contactStringBuf.append(phoneNumber);
                        contactStringBuf.append("\r\n");
                        contactStringBuf.append(phoneTypeStr);
                        contactStringBuf.append("\r\n");
                        contactStringBuf.append(userImage);

                        //phoneContactsList.add(contactStringBuf.toString());
                        list.add(new Contacts(userDisplayName, phoneNumber, userImage));
                        //list.add(userDisplayName, phoneNumber, phoneTypeStr, userImage);

                    }
                    while (cursor.moveToNext());
                }
                catch (Exception e)
                {
                    Log.i("My Error6 = ",e+"");
                }
                //contactsListDataAdapter.notifyDataSetChanged();
                //customAdapter = new CustomAdapter(this, list);
                //contactsListView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();

            }
        }
        catch (Exception e)
        {
            Log.i("My Error4 = ",e+"");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.search_menu, menu);
        search = menu.findItem(R.id.search);
        searchView  = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(ListPhoneContactsActivity.this, "Searching for = "+newText, Toast.LENGTH_SHORT).show();
                if(newText.isEmpty())
                {
                    filterList =  list;
                    Log.i("My filterList size = ",filterList.size()+"");
                }
                else
                {
                    List<Contacts> filteredList = new ArrayList<>();
                    /*List<Contacts> rlist = new ArrayList<>();
                    rlist.add(new Contacts("ss","22","ff"));
                    rlist.add(new Contacts("aa","33","ff"));
                    rlist.add(new Contacts("bb","44","ff"));
                    for(Contacts row:rlist)*/

                    for(Contacts row:list)
                    {
                        if(row.getName()!=null && !row.getName().equalsIgnoreCase(""))
                        {
                            Log.i("My row.getName()1 = ",row.getName()+"");
                            Log.i("My row.getphone()1 = ",row.getPhone()+"");
                            if(row.getName().toLowerCase().contains(newText.toLowerCase()) || row.getPhone().toLowerCase().contains(newText.toLowerCase()))
                            {
                                filteredList.add(row);
                            }
                            filterList = filteredList;
                        }

                    }
                }
                customAdapter.setFilter(filterList);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            //int id = item.getItemId();
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

