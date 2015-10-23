package hungle.com.sqliteandroidlession;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nam on 10/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DATABASE_HELPER";
    private static final String DATABASE_NAME = "contactsManager";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FILE = "databases/contactsManager.sql";
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static Context context;

    public DatabaseHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION, null);
        context = _context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

         /*Log.d("DATABASE_HELPER", "before create table");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        Log.d("DATABASE_HELPER", "After create table");
        */
        Log.d(TAG, "before create from file");
        executeSQLScript(db);
        Log.d(TAG, "After create from file");

    }

    private void executeSQLScript(SQLiteDatabase database) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open(DATABASE_FILE);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            String[] createScript = outputStream.toString().split(";");
            for (int i = 0; i < createScript.length; i++) {
                String sqlStatement = createScript[i].trim();
                // TODO You may want to parse out comments here
                if (sqlStatement.length() > 0) {
                    database.execSQL(sqlStatement + ";");
                }
            }
        } catch (IOException e) {
            // TODO Handle Script Failed to Load
            Log.e(TAG, e.getMessage());
        } catch (SQLException e) {
            // TODO Handle Script Failed to Execute
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public boolean addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        long i = db.insert(TABLE_CONTACTS, null, values);
        Log.d("Insert = ", "******************* " + i + " ***************");
        db.close(); // Closing database connection
        return i > 0;
    }
    public boolean delContact( int contact_id ){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_CONTACTS,KEY_ID + " = ?", new String[] { String.valueOf(contact_id) } );
        Log.d("Delete = ", "******************* " + i + " ***************");
        return i > 0;
    }
    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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
    public ArrayList<Contact> getAllContacts2() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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
    /*
    private void readDatabaseScript( SQLiteDatabase db) throws IOException, SQLException{
        InputStream script = context.getAssets().open(DATABASE_FILE);
        byte[] buffer = new byte[2048];
        for (int byteRead = script.read(); byteRead != -1 ; byteRead = script.read()) {
            Arrays.fill(buffer,(byte)0);
            for (int i = 0; byteRead != -1 && i != 2048; byteRead = script.read(), i++ ) {
                buffer[i] = (byte)byteRead;
                if( byteRead == ';')
                    break;
            }
            if( byteRead != -1)
                db.execSQL(new String(buffer));
        }
    }
    */
}
