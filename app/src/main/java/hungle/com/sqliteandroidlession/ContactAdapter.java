package hungle.com.sqliteandroidlession;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hungle.com.sqliteandroidlession.classes.Contact;
import hungle.com.sqliteandroidlession.classes.ContactDAO;
import hungle.com.sqliteandroidlession.classes.ContactDaoImpl;

/**
 * Created by Le Hung on 10/14/2015 9:10 PM.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    static class ContactViewHolder {
        TextView tvName;
        TextView tvID;
        ImageView ivPhoto;
        TextView tvPhone;
        ImageButton btnDel;
        int ID;
    }
    private int mSelectedItem;
    private Context context;
    private ArrayList<Contact> data = null;
    public ContactAdapter(Context _context, ArrayList<Contact> _contacts) {
        super(_context,0, _contacts);
        context = _context;
        this.data = _contacts;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if( convertView == null){
            convertView  = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
        }
        final ContactViewHolder holder = new ContactViewHolder();
        holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        holder.tvID = (TextView) convertView.findViewById(R.id.tvContactID);
        holder.btnDel = (ImageButton)convertView.findViewById(R.id.btn_del_contact);

        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhoneNumber());
        holder.tvID.setText(contact.getID() + "");
        /*
        File imgFile = new File( Environment.getExternalStorageDirectory() + File.separator + "HungImages" +File.separator + contact.getImage() );
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.ivPhoto.setImageBitmap(myBitmap);
        }
       */
        holder.ID = contact.getID();
        convertView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(holder.tvName.getText().toString());
                int contactID = holder.ID;
                Log.d("ViewID", contactID + "");
                menu.add(0, contactID, 1, "Delete");
                menu.add(0,contactID, 2 , "Edit");
                menu.add(0,contactID, 3 , "Details");
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            private int pos = position;
            public void onClick(View v) {
                    final int contactID = Integer.parseInt(holder.tvID.getText().toString());
                    //final DatabaseHelper db = new DatabaseHelper(context);
                    final ContactDAO db = new ContactDaoImpl(context);
                    MessageUtilities.confirm(context, "Delete Item", "Do you want to delete this item ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    if (db.delete(contactID)) {
                        data.remove(pos);
                        notifyDataSetChanged(); //  remove item list
                        MessageUtilities.alert(context, "Contact deleted. #" + holder.tvID.getText().toString());
                    } else {
                        MessageUtilities.alert(context,"Contact deleted. #" + holder.tvID.getText().toString() );
                    }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
        convertView.setTag(holder);
        return convertView; // Return the completed view to render on screen
    }
}
