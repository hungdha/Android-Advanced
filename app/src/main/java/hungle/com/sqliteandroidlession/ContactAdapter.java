package hungle.com.sqliteandroidlession;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Le Hung on 10/14/2015 9:10 PM.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context _context, ArrayList<Contact> _contacts) {
        super(_context,0, _contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if( convertView == null){
            convertView  = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
        }
        ContactViewHolder holder = new ContactViewHolder();
        holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        holder.tvID = (TextView) convertView.findViewById(R.id.tvContactID);
        convertView.setTag(holder);
        // tvPhone.setText(contact.getPhoneNumber());
        // tvContactID.setText(contact.getID()+ "" );
        /*
        Button btnDelContact = (Button)convertView.findViewById(R.id.btn_del_contact);
        btnDelContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView textView1 = (TextView)v.findViewById(R.id.tvContactID);
                Log.d("CLICK DEL", "VALUExxx:" + textView1.toString());
            }
        });
        */
        // Return the completed view to render on screen
        return convertView;
    }
}
