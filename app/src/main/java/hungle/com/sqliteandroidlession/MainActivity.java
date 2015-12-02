package hungle.com.sqliteandroidlession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hungle.com.sqliteandroidlession.classes.Contact;
import hungle.com.sqliteandroidlession.classes.ContactDAO;
import hungle.com.sqliteandroidlession.classes.ContactDaoImpl;

public class MainActivity extends Activity implements View.OnClickListener {
    // private DatabaseHelper db;
    private ContactDAO db;
    private ListView listViewContact;
    private Button btnAddContact;

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MY_DEBUG", "onStop()" );
    }

    @Override
    protected void onStart() {
        super.onStart();
        popuplateContacts();
        Log.d("MY_DEBUG", "onStart()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db = new DatabaseHelper(this);
        db = new ContactDaoImpl(this);
        // Find id and set on listener
        findViewById(R.id.btn_add_contact).setOnClickListener(this);
        listViewContact = (ListView) findViewById(R.id.list_view_contact);
        registerForContextMenu(listViewContact);
        popuplateContacts();
        registedClickCallBack();
        createDirIfNotExists("HungImages");

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
        List<Contact> listContact = db.getAllContact();
        ArrayList<Contact> arrList = new ArrayList<Contact>(listContact.size());
        arrList.addAll(listContact);
        ContactAdapter contactAdapter = new ContactAdapter(this, arrList );
        listViewContact.setAdapter(contactAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_listview, menu);
        // Log.d("MY_MENU", "onCreateOptionsMenu()");
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        /*
        if (v.getId() == R.id.list_view_contact) {
            Log.d("MY_MENU", "onCreateContextMenu()");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_listview, menu);
        }
        */
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("MY_MENU", "onContextItemSelected(item)");
        String title = item.getTitle().toString();
        boolean result = false;
        switch (title){
            case "Delete":{
                int id = item.getItemId();
                if (db.delete(id)) {
                    popuplateContacts();
                    MessageUtilities.alert(this, "Delete contact successfully!");
                } else {
                    MessageUtilities.alert(this, "Delete failed");
                }
                result = true;
                break;
            }
            case "Edit":{
                result = true;
                Intent intent = new Intent(this, ContactActivity.class);
                int id = item.getItemId();
                intent.putExtra("contact_id", id + "" );
                startActivity(intent);
                break;
            }
            case "Details":{
                MessageUtilities.alert(this, "" );
                break;
            }
            default:
                result = super.onContextItemSelected(item);
                break;
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("OPT_ITEM_SELECTED", "onOptionsItemSelected()");
        //noinspection SimplifiableIfStatement
        Log.d("__ID_CONTACT", R.id.btn_del_contact + "");
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
    private void addContact() {
        EditText edContactName = (EditText) findViewById(R.id.ed_contact_name);
        EditText edPhoneNumber = (EditText) findViewById(R.id.ed_phone_number);
        String contactName = edContactName.getText().toString();
        String phoneNumber = edPhoneNumber.getText().toString();
        String image = "abc";
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
            //boolean result = db.addContact(new Contact(contactName, phoneNumber));
            Contact contact = new Contact();
            contact.setName( contactName);
            contact.setPhoneNumber( phoneNumber );
            contact.setImage( "abc" );
            boolean result = db.insert(contact);
            if( result ){
                Toast.makeText(MainActivity.this, "Save contact successful!", Toast.LENGTH_SHORT).show();
            }
            // Reading all contacts
            Log.d("Reading: ", "Reading all contacts..");
            popuplateContacts();
        }
    }

    /*
    private String[] getAllContacts2() {
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContact();
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
    */

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }
}
