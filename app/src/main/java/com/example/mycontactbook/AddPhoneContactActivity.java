package com.example.mycontactbook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPhoneContactActivity extends AppCompatActivity {
    private EditText displayNameEditor;
    private EditText phoneNumberEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_contact);
        displayNameEditor = (EditText)findViewById(R.id.add_phone_contact_display_name);
        phoneNumberEditor = (EditText)findViewById(R.id.add_phone_contact_number);
        final Spinner phoneTypeSpinner = (Spinner)findViewById(R.id.add_phone_contact_type);
        final String phoneTypeArr[] = {"Mobile", "Home", "Work"};

        ArrayAdapter<String> phoneTypeSpinnerAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phoneTypeArr);
        phoneTypeSpinner.setAdapter(phoneTypeSpinnerAdaptor);

        Button savePhoneContactButton = (Button)findViewById(R.id.add_phone_contact_save_button);
        savePhoneContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                long rowContactId = getRawContactId();  //generate id

                String displayName = displayNameEditor.getText().toString();
                insertContactDisplayName(addContactsUri, rowContactId, displayName);

                String phoneNumber = phoneNumberEditor.getText().toString();
                String phoneTypeStr  = (String)phoneTypeSpinner.getSelectedItem();
                insertContactPhoneNumber(addContactsUri, rowContactId, phoneNumber, phoneTypeStr);

                Toast.makeText(getApplicationContext(),"New contact has been added, go back to previous page to see it in contacts list." , Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private long getRawContactId()
    {
        ContentValues cv = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, cv);
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }
    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName)
    {
        ContentValues cv = new ContentValues();
        cv.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        cv.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        getContentResolver().insert(addContactsUri, cv);
    }
    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr)
    {
        ContentValues cv = new ContentValues();
        cv.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        cv.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;

        if("home".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if("mobile".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if("work".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        cv.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);
        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, cv);

    }
}
