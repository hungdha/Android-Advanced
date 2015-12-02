package hungle.com.sqliteandroidlession;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import hungle.com.sqliteandroidlession.classes.Contact;
import hungle.com.sqliteandroidlession.classes.ContactDAO;
import hungle.com.sqliteandroidlession.classes.ContactDaoImpl;

public class ContactActivity extends Activity implements View.OnClickListener {
    private Uri imageCaptureUri;
    private static  final  int PICK_FROM_CAMERA = 1;
    private static  final  int PICK_FROM_FILE = 2;
    //private DatabaseHelper db = null;
    private EditText edName;
    private EditText edPhone;
    private Button btnSelectImage;
    private ImageView ivContact;
    private ContactDAO db;
    private int contactID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MTAG", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        contactID = Integer.parseInt(getIntent().getStringExtra("contact_id"));
        //db = new DatabaseHelper(getApplicationContext());
        db = new ContactDaoImpl(this);
        Contact contact = db.getContact( contactID );
        edName = (EditText)findViewById(R.id.edName);
        edName.setText(contact.getName());
        edPhone = (EditText)findViewById(R.id.edPhone);
        edPhone.setText(contact.getPhoneNumber());
        btnSelectImage = (Button)findViewById(R.id.btnSelectImage);
        ivContact = (ImageView)findViewById(R.id.ivContact);

        final String[] items = new String[]{"From Cam", "From SD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setTitle("Select image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    // init intent
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // create file from camera take picutre
                    File file = new File(Environment.getExternalStorageDirectory(),"tmp_avatar" + String.valueOf(System.currentTimeMillis()));
                    // convert file to Uri
                    imageCaptureUri = Uri.fromFile(file);
                    try {

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        intent.putExtra("return-data", true );
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                        Log.d("TAG", "setAdapter()" );
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MTAG", "onClick()");
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK) return;
        Bitmap bitmap = null;
        String path = "";
        if( requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri);
            if( path == null )
                path = imageCaptureUri.getPath();
            if( path != null )
                bitmap = BitmapFactory.decodeFile(path);
        }else{
            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        ivContact.setImageBitmap(bitmap); // set bitmap for ImagaeView
    }

    /*private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image*//*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/

    private String getRealPathFromURI( Uri contentURI ) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, proj, null, null, null );
        if( cursor == null ) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnCancel){
            this.finish();
        }else if( v.getId() == R.id.btnSave){ // Save contact
            Contact contact = new Contact();
            contact.setID(contactID);
            contact.setName(edName.getText().toString());
            contact.setPhoneNumber(edPhone.getText().toString());
            String image =  saveImage();
            Log.e("IMAGE_DEBUG",  image);
            //contact.setImage( saveImage() );
            //contact.setImage("aaaaaaaaaaaaa");
            boolean result = db.update(contact);
            if( result ) {
                this.finish();
                Toast.makeText( ContactActivity.this, "Update complete !", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(ContactActivity.this, "Update error !", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImage(){
        BitmapDrawable btmpDr = (BitmapDrawable) ivContact.getDrawable();
        Bitmap bmp = btmpDr.getBitmap();
        String imageNameForSDCard = "";
        ///File sdCardDirectory = Environment.getExternalStorageDirectory();
        try
        {
            File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "HungImages");
            sdCardDirectory.mkdirs();

            imageNameForSDCard = "image_" + System.currentTimeMillis() + ".jpg";
            File image = new File(sdCardDirectory, imageNameForSDCard);
            FileOutputStream outStream;
            outStream = new FileOutputStream(image);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            // 100 to keep full quality of the image
            outStream.flush();
            outStream.close();
            //Refreshing SD card
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
        }
        catch (Exception e)
        {
            imageNameForSDCard = "";
            e.printStackTrace();
            Toast.makeText(ContactActivity.this, "Image could not be saved : Please ensure you have SD card installed " +
                    "properly", Toast.LENGTH_LONG).show();

        }
        return imageNameForSDCard;
    }
}
