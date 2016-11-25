package services;

import com.datastax.driver.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import play.inject.ApplicationLifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<User> getUsers() {
        List<Row> rows = session.execute(bs).all();
        return rows.stream().map(User::new).collect(Collectors.toList());
    }
}
