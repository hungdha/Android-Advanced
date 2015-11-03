package hungle.com.sqliteandroidlession;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

        /*if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#BCF7F0"));
        }*/
        final ContactViewHolder holder = new ContactViewHolder();
        holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        holder.tvID = (TextView) convertView.findViewById(R.id.tvContactID);
        holder.btnDel = (ImageButton)convertView.findViewById(R.id.btn_del_contact);

        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhoneNumber());
        holder.tvID.setText(contact.getID() + "");
        holder.ID = contact.getID();
        /*
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            private int pos = position;
            public boolean onLongClick(View v) {
                int contactID = Integer.parseInt(holder.tvID.getText().toString());
                Log.v("TAG_HUNG_LE", "Long Click" + contactID);
                return true;
            }
        });

        */
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




        //Register view event click listener

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.e("CLICK_CONVERT_VIEW", v.toString());
                // v.setSelected(true);
                //v.setBackgroundColor(Color.parseColor("#222222"));

            }
        });

        //Đăng ký lắng nghe khi click vào icon của view này
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            private int pos = position;
            public void onClick(View v) {
                    final int contactID = Integer.parseInt(holder.tvID.getText().toString());
                    final DatabaseHelper db = new DatabaseHelper(context);
                    MessageUtilities.confirm(context, "Delete Item", "Do you want to delete this item ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    if (db.delContact(contactID)) {
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
        // Return the completed view to render on screen
        return convertView;
    }
}
