package services;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import play.inject.ApplicationLifecycle;

/**
 * Created by Gleb Popov on 25-Nov-16.
 */
@Singleton
public class BookingService {

    private final ApplicationLifecycle appLifecycle;
    private final Session session;
    private final PreparedStatement insertStatment;
    private final BoundStatement bs;

    @Inject
    public BookingService(ApplicationLifecycle appLifecycle, Session session) {
        this.appLifecycle = appLifecycle;
        this.session = session;
        insertStatment = session.prepare("select * from users;");
        bs = new BoundStatement(insertStatment);
    }

    public ResultSet getUsers() {
        return session.execute(bs);
    }
}
