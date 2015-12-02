package hungle.com.sqliteandroidlession.classes;

import java.util.List;

/**
 * Created by Le Hung on 11/27/2015 4:11 PM.
 */
public interface ContactDAO {
    public boolean insert( Contact contact );
    public boolean update( Contact contact  );
    public boolean delete( int id );
    public List<Contact> getAllContact();
    public Contact getContact( int id );
}
