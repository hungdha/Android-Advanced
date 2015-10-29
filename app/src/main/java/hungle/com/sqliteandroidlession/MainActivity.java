package hungle.com.sqliteandroidlession;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper db;
    private ListView listViewContact;
    private Button btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        // Find id and set on listener
        findViewById(R.id.btn_add_contact).setOnClickListener(this);
        // findViewById(R.id.btn_del_contact).setOnClickListener(this);

        listViewContact = (ListView) findViewById(R.id.list_view_contact);
        popuplateContacts();
        registedClickCallBack();


    }

    private void registedClickCallBack() {
        listViewContact.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TextView textView = (TextView) view;
                        LinearLayout linLayout = (LinearLayout) view;
                        String message = "You clicked #" + position +
                                ", which is string: " /*+ textView.getText().toString() */ +
                                ", ID #" + id;
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void popuplateContacts() {
        // fill data to listview control
        ContactAdapter contactAdapter = new ContactAdapter(this, db.getAllContacts2());
        ListView listViewContact = (ListView) findViewById(R.id.list_view_contact);
        listViewContact.setAdapter(contactAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Log.d("__ID_CONTACT" , R.id.btn_del_contact + "" );
        if (id ==  R.id.btn_del_contact ) {
            //return true;
            Toast.makeText(this, "Button delete invoked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_contact: {
                addContact();
                break;
            }
            default:
                break;
        }
    }

    private void delContact() {
        boolean result = db.delContact(10);
        if(result){
            Toast.makeText(MainActivity.this, "Contact Deleted!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Contact delete fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void addContact() {
        EditText edContactName = (EditText) findViewById(R.id.ed_contact_name);
        EditText edPhoneNumber = (EditText) findViewById(R.id.ed_phone_number);
        String contactName = edContactName.getText().toString();
        String phoneNumber = edPhoneNumber.getText().toString();
        boolean check = true;
        if (contactName.isEmpty()) {
            Toast.makeText(MainActivity.this, "Contact Name Empty", Toast.LENGTH_SHORT).show();
            check = false;
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(MainActivity.this, "Phone Numer Empty", Toast.LENGTH_SHORT).show();
            check = false;
        }
        if (check) {
            Log.d("Insert: ", "Inserting ..");
            boolean result = db.addContact(new Contact(contactName, phoneNumber));
            if( result ){
                Toast.makeText(MainActivity.this, "Save contact successful!", Toast.LENGTH_SHORT).show();
            }
            // Reading all contacts
            Log.d("Reading: ", "Reading all contacts..");
            popuplateContacts();
        }
    }


    private String[] getAllContacts2() {
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();
        int size = contacts.size();
        String[] contactList = new String[size];
        int i = 0;
        for (Contact cn : contacts) {
            contactList[i] = cn.getName();
            i++;
        }
        Arrays.sort(contactList);
        return contactList;
    }
}
