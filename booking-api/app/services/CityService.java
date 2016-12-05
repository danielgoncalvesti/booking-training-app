package services;

import com.datastax.driver.core.*;
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
    private final PreparedStatement selectAllHotelsByCity;
    private final PreparedStatement selectHotel;
    private final PreparedStatement insertHotel;
    private final PreparedStatement updateHotelRooms;
    private final BoundStatement selectCities;

    @Inject
    public CityService(Session session) {
        this.session = session;
        selectAllHotelsByCity = session.prepare(
                        "select hotel_name, city, description, rooms from hotels where city = :city;");
        insertHotel = session.prepare("insert into hotels (hotel_name, city, description, rooms)" +
                " VALUES (:hotel_name, :city, :description, :rooms);");
        selectHotel = session.prepare(
                "select hotel_name, city, description, rooms from hotels where city = :city and hotel_name = :hotel_name;");
        updateHotelRooms = session.prepare("update hotels set rooms = rooms + :room " +
                "where city = :city and hotel_name = :hotel_name;");
        PreparedStatement selectCities = session.prepare("select distinct city from hotels");
        this.selectCities = new BoundStatement(selectCities);
    }

    public List<Hotel> getHotels(String city) {
        BoundStatement selectAllByCity = new BoundStatement(this.selectAllHotelsByCity);
        selectAllByCity.setString("city", city);
        List<Row> rows = session.execute(selectAllByCity).all();
        return rows.stream().map(Hotel::new).collect(Collectors.toList());
    }

    public Hotel getHotel(String city, String hotelName) {
        BoundStatement selectHotel = new BoundStatement(this.selectHotel);
        selectHotel.setString("city", city);
        selectHotel.setString("hotel_name", hotelName);
        Row row = session.execute(selectHotel).one();
        return new Hotel(row);
    }

    public Hotel saveHotel(Hotel hotel) {
        BoundStatement insertHotel = new BoundStatement(this.insertHotel);
        insertHotel.setString("hotel_name", hotel.getName());
        insertHotel.setString("city", hotel.getCity());
        insertHotel.setString("description", hotel.getDescription());
        insertHotel.setSet("rooms", hotel.getRooms(), String.class);
        session.execute(insertHotel);
        return hotel;
    }

    public List<String> getCities() {
        List<Row> rs = session.execute(selectCities).all();
        return rs.stream().map(row -> row.getString("city")).collect(Collectors.toList());
    }

    public Boolean addRoom(String city, String hotelName, String room) {
        BoundStatement update = new BoundStatement(updateHotelRooms);
        update.setString("room", room);
        update.setString("city", city);
        update.setString("hotel_name", hotelName);
        ResultSet rs = session.execute(update);
        return rs.wasApplied();
    }
}
