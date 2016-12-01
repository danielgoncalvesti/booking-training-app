package services;

import com.datastax.driver.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Booking;
import model.FreeRoomsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles booking operations
 * Created by Gleb Popov on 30-Nov-16.
 */
@Singleton
public class BookingService {

    // insert into Booking.hotels (city, hotel_name, description) values ('Odessa', 'BM2', 'ONE-1') if not exists;
    private final Session session;
    private final BoundStatement insert;
    private final BoundStatement listBooking;
    private final CityService cityService;
    @Inject
    public BookingService(Session session, CityService cityService) {
        this.session = session;
        this.cityService = cityService;
        PreparedStatement insert = session.prepare("insert into booking (hotel_name, city, room, booking_day, guest_name)" +
                " values (:hotel_name, :city, :room, :booking_day, :guest_name) if not exists;");
        this.insert = new BoundStatement(insert);
        PreparedStatement listBooking = session.prepare("select room from booking " +
                "where city = :city and hotel_name = :hotel_name " +
                "and booking_day >= :booking_start and booking_day <= :booking_end");
        this.listBooking = new BoundStatement(listBooking);
    }

    public Boolean addBookings(List<Booking> bookings) {
        List<Boolean> results = new ArrayList<>();
        BatchStatement batchStatement = new BatchStatement();
        batchStatement.setConsistencyLevel(ConsistencyLevel.ALL);

        for (Booking booking : bookings) {
            insert.setString("hotel_name", booking.getHotelName());
            insert.setString("city", booking.getCity());
            insert.setString("room", booking.getRoom());
            insert.setTimestamp("booking_day", booking.getBookingDay());
            insert.setString("guest_name", booking.getGuestName());
            batchStatement.add(insert);
        }
        ResultSet rs = session.execute(batchStatement);
        results.add(rs.wasApplied());
        return  !results.contains(Boolean.FALSE);
    }

    public Set<String> getFreeRooms(FreeRoomsRequest request) {
        Set<String> rooms = cityService.getHotel(request.getCity(), request.getHotelName()).getRooms();
        listBooking.setString("city", request.getCity());
        listBooking.setString("hotel_name", request.getHotelName());
        listBooking.setTimestamp("booking_start", request.getStartReserveTime());
        listBooking.setTimestamp("booking_end", request.getEndReserveTime());
        List<Row> rs = session.execute(listBooking).all();
        for (Row row : rs) {
            rooms.remove(row.getString("room"));
        }
        return rooms;
    }
}
