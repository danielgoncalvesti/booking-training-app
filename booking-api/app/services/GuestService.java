package services;

import com.datastax.driver.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Booking;
import model.GuestBooking;
import model.GuestBookingRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles guest routines
 * Created by Gleb on 01-Dec-16.
 */
@Singleton
public class GuestService {

    private final Session session;
    private final PreparedStatement insertGuestBooking;
    private final PreparedStatement selectGuestBooking;
    private final PreparedStatement selectGuests;
    private final PreparedStatement insertGuest;

    @Inject
    public GuestService(Session session) {
        this.session = session;

        insertGuestBooking = session.prepare("insert into guest_booking (city, hotel_name, guest_name, room, booking_day) " +
                "values (:city, :hotel_name, :guest_name, :room, :booking_day);");

        selectGuestBooking = session.prepare("select city,hotel_name, guest_name, room, booking_day from guest_booking " +
                "where city = :city and hotel_name = :hotel_name " +
                "and guest_name = :guest_name and booking_day >= :booking_start and booking_day <= :booking_end");

        selectGuests = session.prepare("select guest_name from guests where city = :city and hotel_name = :hotel_name");

        insertGuest = session.prepare("insert into guests (city, hotel_name, guest_name) " +
                "values (:city, :hotel_name, :guest_name);");
    }


    public List<GuestBooking> getRoomsByGuest(GuestBookingRequest request) {
        BoundStatement listBooking = new BoundStatement(selectGuestBooking);
        listBooking.setString("city", request.getCity());
        listBooking.setString("hotel_name", request.getHotelName());
        listBooking.setTimestamp("booking_start", request.getStartReserveTime());
        listBooking.setTimestamp("booking_end", request.getEndReserveTime());
        listBooking.setString("guest_name", request.getGuestName());
        List<Row> rs = session.execute(listBooking).all();
        return rs.stream().map(GuestBooking::new).collect(Collectors.toList());
    }

    public List<String> getGuests(String city, String hotelName) {
        BoundStatement listGuests = new BoundStatement(selectGuests);
        listGuests.setString("city", city);
        listGuests.setString("hotel_name", hotelName);
        List<Row> rs = session.execute(listGuests).all();
        return rs.stream().map(row -> row.getString("guest_name")).collect(Collectors.toList());
    }

    public Boolean addGuest(String city, String hotelName, String guestName) {
        BoundStatement addGuest = new BoundStatement(insertGuest);
        addGuest.setString("city", city);
        addGuest.setString("hotel_name", hotelName);
        addGuest.setString("guest_name", guestName);
        ResultSet rs = session.execute(addGuest);
        return rs.wasApplied();
    }

    public void addGuestBookings(List<Booking> bookings) {
        BatchStatement batchStatement = new BatchStatement();
        batchStatement.setConsistencyLevel(ConsistencyLevel.ALL);

        for (Booking booking : bookings) {
            BoundStatement insertGuestBooking = new BoundStatement(this.insertGuestBooking);
            insertGuestBooking.setString("hotel_name", booking.getHotelName());
            insertGuestBooking.setString("city", booking.getCity());
            insertGuestBooking.setString("room", booking.getRoom());
            insertGuestBooking.setTimestamp("booking_day", booking.getBookingDay());
            insertGuestBooking.setString("guest_name", booking.getGuestName());

            batchStatement.add(insertGuestBooking);
        }
        session.execute(batchStatement);
    }
}
