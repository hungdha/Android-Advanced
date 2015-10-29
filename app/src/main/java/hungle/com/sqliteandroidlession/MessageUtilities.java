package hungle.com.sqliteandroidlession;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Le Hung on 10/29/2015 10:40 PM.
 */
public class MessageUtilities {

    public static void confirm( Context context, String titleAlert, String messageAlert ,  DialogInterface.OnClickListener yesClick,
                             DialogInterface.OnClickListener noClick  ) {
        new AlertDialog.Builder(context)
                .setTitle(titleAlert)
                .setMessage(messageAlert)
                .setPositiveButton(android.R.string.yes, yesClick )
                .setNegativeButton(android.R.string.no, noClick )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public static void help(Context context, String msg) {
        Toast t = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
    public static void alert(Context context, String msg) {
        Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}
