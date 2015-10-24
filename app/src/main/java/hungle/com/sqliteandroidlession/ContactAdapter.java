package hungle.com.sqliteandroidlession;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Button btnDel;
    }
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
        holder.btnDel = (Button)convertView.findViewById(R.id.btn_del_contact);

        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhoneNumber());
        holder.tvID.setText(contact.getID() + "");
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
                Log.d("___MY_ID", v.getId() + "" );
                menu.add(0,v.getId(),0,"Delete" );

            }
        });
        //Register view event click listener
        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            private int pos = position;

            public void onClick(View v) {
                int contactID = Integer.parseInt(holder.tvID.getText().toString());
                Log.v("CONVER_VIEW_", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + contactID);
            }
        });
        */
        //Đăng ký lắng nghe khi click vào icon của view này
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            private int pos = position;
            public void onClick(View v) {
                int contactID = Integer.parseInt(holder.tvID.getText().toString());
                DatabaseHelper db = new DatabaseHelper(context);
                if(db.delContact(contactID)){
                    data.remove(pos);
                    notifyDataSetChanged(); //  remove item list
                    Toast.makeText(context, "Contact deleted. #" + holder.tvID.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Contact failed. #" + holder.tvID.getText().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        convertView.setTag(holder);
        // Return the completed view to render on screen
        return convertView;
    }
}