package services;

import com.datastax.driver.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Booking;
import model.FreeRoomsRequest;

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
    private final PreparedStatement insertBooking;
    private final PreparedStatement listBooking;
    private final CityService cityService;
    private final GuestService guestService;

    @Inject
    public BookingService(Session session, CityService cityService, GuestService guestService) {
        this.session = session;
        this.cityService = cityService;
        this.guestService = guestService;

        insertBooking = session.prepare("insert into booking (hotel_name, city, room, booking_day, guest_name)" +
                " values (:hotel_name, :city, :room, :booking_day, :guest_name) if not exists;");

        listBooking = session.prepare("select room from booking " +
                "where city = :city and hotel_name = :hotel_name " +
                "and booking_day >= :booking_start and booking_day <= :booking_end");

    }

    public Boolean addBookings(List<Booking> bookings) {

        BatchStatement batchStatement = new BatchStatement();
        batchStatement.setConsistencyLevel(ConsistencyLevel.ALL);
        for (Booking booking : bookings) {
            BoundStatement insertBooking = new BoundStatement(this.insertBooking);
            insertBooking.setString("hotel_name", booking.getHotelName());
            insertBooking.setString("city", booking.getCity());
            insertBooking.setString("room", booking.getRoom());
            insertBooking.setTimestamp("booking_day", booking.getBookingDay());
            insertBooking.setString("guest_name", booking.getGuestName());

            batchStatement.add(insertBooking);
        }
        ResultSet rs = session.execute(batchStatement);
        if (rs.wasApplied()) {
            guestService.addGuestBookings(bookings);
            return true;
        } else {
            return false;
        }
    }

    public Set<String> getFreeRooms(FreeRoomsRequest request) {
        Set<String> rooms = cityService.getHotel(request.getCity(), request.getHotelName()).getRooms();
        BoundStatement listBooking = new BoundStatement(this.listBooking);
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
