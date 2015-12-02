package hungle.com.sqliteandroidlession.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Hung on 11/27/2015 4:14 PM.
 */
public class ContactDaoImpl implements ContactDAO {
    DatabaseHelper dbHelper;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_IMAGE = "image";
    public ContactDaoImpl( Context _context ){
        dbHelper = new DatabaseHelper(_context );
    }
    @Override
    public boolean insert(Contact contact) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        values.put(KEY_IMAGE, "aaaa"); // Contact Phone
        // Inserting Row
        long i = dbHelper.DB().insert(TABLE_CONTACTS, null, values);
        Log.d("Insert = ", "******************* " + i + " ***************");
        dbHelper.DB().close(); // Closing database connection
        return i > 0;
    }

    @Override
    public boolean update(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
        values.put(KEY_IMAGE, contact.getImage());
        int contact_id = contact.getID();
        long result = dbHelper.DB().update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[]{String.valueOf(contact_id)});
        return result > 0;
    }

    @Override
    public boolean delete(int id) {
        int i = dbHelper.DB().delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d("Delete = ", "******************* " + i + " ***************");
        return i > 0;
    }

    @Override
    public List<Contact> getAllContact() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        Cursor cursor = dbHelper.DB().rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    @Override
    public Contact getContact(int id) {
        if( id > 0){
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE id = " + id;
            Cursor cursor = dbHelper.DB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            Contact contact = new Contact();
            if (cursor.moveToFirst()) {
                do {
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                } while (cursor.moveToNext());
            }
            return contact;
        }else{
            return null;
        }
    }
    public ArrayList<Contact> getAllContact2() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        Cursor cursor = dbHelper.DB().rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
}
