package services;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Hotel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles API request for City
 * Created by Gleb Popov on 29-Nov-16.
 */
@Singleton
public class CityService {

    private final Session session;
    private final BoundStatement selectAllBs;
    private final BoundStatement select;
    private final BoundStatement insertBs;

    @Inject
    public CityService(Session session) {
        this.session = session;
        PreparedStatement selectAll = session.prepare(
                        "select hotel_name, city, description, rooms from hotels where city = :city;");
        selectAllBs = new BoundStatement(selectAll);
        PreparedStatement insert = session.prepare("insert into hotels (hotel_name, city, description, rooms)" +
                " VALUES (:hotel_name, :city, :description, :rooms);");
        insertBs = new BoundStatement(insert);
        PreparedStatement select = session.prepare(
                "select hotel_name, city, description, rooms from hotels where city = :city and hotel_name = :hotel_name;");
        this.select = new BoundStatement(select);
    }

    public List<Hotel> getHotels(String city) {
        selectAllBs.setString("city", city);
        List<Row> rows = session.execute(selectAllBs).all();
        return rows.stream().map(Hotel::new).collect(Collectors.toList());
    }

    public Hotel getHotel(String city, String hotelName) {
        select.setString("city", city);
        select.setString("hotel_name", hotelName);
        Row row = session.execute(select).one();
        return new Hotel(row);
    }

    public Hotel saveHotel(Hotel hotel) {
        insertBs.setString("hotel_name", hotel.getName());
        insertBs.setString("city", hotel.getCity());
        insertBs.setString("description", hotel.getDescription());
        insertBs.setSet("rooms", hotel.getRooms(), String.class);
        session.execute(insertBs);
        return hotel;
    }
}
