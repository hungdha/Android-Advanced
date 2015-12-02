package hungle.com.sqliteandroidlession.classes;

import android.graphics.Bitmap;

/**
 * Created by nam on 10/13/2015.
 */
public class Contact {

    //private variables
    private int _id;
    private String _name;
    private String _phone_number;
    private String _image;
    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String name, String _phone_number, String _image ){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._image = _image;
    }

    // constructor
    public Contact(String name, String _phone_number){
        this._name = name;
        this._phone_number = _phone_number;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }

    public  void setImage( String image ){
        this._image = image;
    }
    public String getImage(){
        return this._image;
    }

}
