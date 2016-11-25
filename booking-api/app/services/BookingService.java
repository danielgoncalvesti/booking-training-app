package services;

import com.datastax.driver.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import play.inject.ApplicationLifecycle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gleb Popov on 25-Nov-16.
 */
@Singleton
public class BookingService {

    private final ApplicationLifecycle appLifecycle;
    private final Session session;
    private final BoundStatement selectAllBs;
    private final BoundStatement insertBs;

    @Inject
    public BookingService(ApplicationLifecycle appLifecycle, Session session) {
        this.appLifecycle = appLifecycle;
        this.session = session;
        PreparedStatement selectAll = session.prepare("select * from users;");
        selectAllBs = new BoundStatement(selectAll);
        PreparedStatement insert = session.prepare("INSERT INTO users (user_name, password, gender, " +
                "session_token, state, birth_year) VALUES (?, ?, ?, ?, ?, ?);");
        insertBs = new BoundStatement(insert);
    }

    public List<User> getUsers() {
        List<Row> rows = session.execute(selectAllBs).all();
        return rows.stream().map(User::new).collect(Collectors.toList());
    }

    public void addUser(User user) {
        Object[] params = new Object[]{
                user.getUserName(),
                user.getPassword(),
                user.getGender(),
                user.getSessionToken(),
                user.getState(),
                user.getBirthYear()
        };
//        insertBs.setString(1, user.getUserName());
//        insertBs.setString(2, user.getPassword());
//        insertBs.setString(3, user.getGender());
//        insertBs.setString(4, user.getSessionToken());
//        insertBs.setString(5, user.getState());
//        insertBs.setLong(6, user.getBirthYear());
        insertBs.bind(params);
        session.execute(insertBs);
    }
}
