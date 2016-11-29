package services;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Hotel;
import play.inject.ApplicationLifecycle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles API request for City
 * Created by Gleb Popov on 29-Nov-16.
 */
@Singleton
public class CityService {

    private final ApplicationLifecycle appLifecycle;
    private final Session session;
    private final BoundStatement selectAllBs;
//    private final BoundStatement insertBs;

    @Inject
    public CityService(ApplicationLifecycle appLifecycle, Session session) {
        this.appLifecycle = appLifecycle;
        this.session = session;
        PreparedStatement selectAll = session.prepare("select * from hotels where city = ?;");
        selectAllBs = new BoundStatement(selectAll);
//        PreparedStatement insert = session.prepare("insert into hotel (user_name, password, gender, " +
//                "session_token, state, birth_year) VALUES (?, ?, ?, ?, ?, ?);");
//        insertBs = new BoundStatement(insert);
    }

    public List<Hotel> getHotels(String city) {
        selectAllBs.setString(0, city);
        List<Row> rows = session.execute(selectAllBs).all();
        return rows.stream().map(Hotel::new).collect(Collectors.toList());
    }

}
