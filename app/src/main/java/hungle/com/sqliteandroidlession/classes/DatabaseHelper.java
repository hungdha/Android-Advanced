package hungle.com.sqliteandroidlession.classes;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static Context context;

    public DatabaseHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION, null);
        context = _context;
    }
    public SQLiteDatabase DB(){
        return this.getWritableDatabase();
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
