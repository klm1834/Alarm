package com.example.kim.realapplication;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-02-10.
 */
public class ContactsListActivity extends ListActivity
{
    private String searchKeyword;

    private List<Contact> getContactsList () throws Exception
    {

        List<Contact> contactsList = new ArrayList<Contact>();

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);

        while (cursor.moveToNext())
        {
            addContact(contactsList, cursor.getString(1), cursor.getString(2));
        }

        return contactsList;
    }

    private void addContact (
            List<Contact> contactsList, String name,
            String number
    ) throws Exception
    {
        if (contactsList == null)
        {
            throw new NullPointerException("contactList가 null 입니다.");
        }

        boolean isAdd = false;

        if (searchKeyword != null && "".equals(searchKeyword.trim()) == false)
        {
            String iniName = HangulUtils.getHangulInitialSound(name, searchKeyword);
            if (iniName.indexOf(searchKeyword) >= 0)
            {
                isAdd = true;
            }
        }
        else
        {
            isAdd = true;
        }

        if (isAdd)
        {
            contactsList.add(new Contact(name, number));
        }
    }

    private void displayList () throws Exception
    {

        List<Contact> contactsList = null;
        contactsList = getContactsList();
        ContactsListAdapter<Contact> adapter = new ContactsListAdapter<Contact>(this, R.layout.contacts_list_row, contactsList);
        setListAdapter(adapter);
    }

    private class ContactsListAdapter<T extends Contact> extends ArrayAdapter<T>
    {
        private List<T> contactsList;

        public ContactsListAdapter (Context context, int textViewResourceId, List<T> items)
        {
            super(context, textViewResourceId, items);
            contactsList = items;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent)
        {
            View view = convertView;
            if (view == null)
            {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.contacts_list_row, null);
            }

            T contacts = contactsList.get(position);
            if (contacts != null)
            {
                TextView viewName = (TextView) view.findViewById(R.id.toptext);
                if (viewName != null)
                {
                    viewName.setText(contacts.getName());
                }

                TextView viewNumber = (TextView) view
                        .findViewById(R.id.bottomtext);
                if (viewNumber != null)
                {
                    viewNumber.setText("전화번호 : " + contacts.getNumber());
                }
            }
            return view;
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

        EditText searchBox = (EditText) findViewById(R.id.EditText01);

        try
        {
            searchBox.addTextChangedListener(
                    new TextWatcher()
                    {
                        public void afterTextChanged (Editable arg0)
                        {
                        }

                        public void beforeTextChanged (CharSequence s, int start, int count, int after)
                        {
                        }

                        public void onTextChanged (CharSequence s, int start, int before, int count)
                        {
                            try
                            {
                                searchKeyword = s.toString();
                                displayList();
                            } catch (Exception e)
                            {
                                Log.e("", e.getMessage(), e);
                            }
                        }
                    }
            );
            displayList();
        } catch (Exception e)
        {
            Log.e("", e.getMessage(), e);
        }
    }
}
