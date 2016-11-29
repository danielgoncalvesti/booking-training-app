package services;

import com.datastax.driver.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import play.inject.ApplicationLifecycle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Booking service handles API requests.
 * Created by Gleb Popov on 25-Nov-16.
 */
@Singleton
public class UserService {

    private final ApplicationLifecycle appLifecycle;
    private final Session session;
    private final BoundStatement selectAllBs;
    private final BoundStatement insertBs;

    @Inject
    public UserService(ApplicationLifecycle appLifecycle, Session session) {
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

    public User addUser(User user) {
        insertBs.setString(0, user.getUserName());
        insertBs.setString(1, user.getPassword());
        insertBs.setString(2, user.getGender());
        insertBs.setString(3, user.getSessionToken());
        insertBs.setString(4, user.getState());
        insertBs.setLong(5, user.getBirthYear());
        session.execute(insertBs);
        return user;
    }
}
