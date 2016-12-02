package model;

import com.datastax.driver.core.Row;

import java.text.SimpleDateFormat;

/**
 * Guest booking data
 * Created by Gleb Popov on 02-Dec-16.
 */
public class GuestBooking {
    private String city;
    private String hotelName;
    private String guestName;
    private String room;
    private String bookingDay;

    public GuestBooking(Row row) {
        city = row.getString("city");
        hotelName = row.getString("hotel_name");
        guestName = row.getString("guest_name");
        room = row.getString("room");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");
        bookingDay = format.format(row.getTimestamp("booking_day"));
    }

    public String getCity() {
        return city;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoom() {
        return room;
    }

    public String getBookingDay() {
        return bookingDay;
    }
}
