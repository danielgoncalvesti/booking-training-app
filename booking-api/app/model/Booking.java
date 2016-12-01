package model;

import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 30-Nov-16.
 */
public class Booking {

    private static final long DAY = 24*60*60*1000;

    private String hotelName;
    private String city;
    private String room;
    private Date bookingDay;
    private String guestName;

    public Booking(Row dataRow) {
        hotelName = dataRow.getString("hotel_name");
        city = dataRow.getString("city");
        room = dataRow.getString("room");
        bookingDay = dataRow.getTimestamp("booking_day");
        guestName = dataRow.getString("guest_name");
    }

    public Booking(String hotelName, String city, String room, Date bookingDay, String guestName) {
        this.hotelName = hotelName;
        this.city = city;
        this.room = room;
        this.bookingDay = bookingDay;
        this.guestName = guestName;
    }

    public static List<Booking> parse(JsonNode node) throws ParseException {
        List<Booking> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");
        Date startDate = format.parse(node.get("startReserveTime").asText());
        Date endDate = format.parse(node.get("endReserveTime").asText());
        String hotelName = node.get("hotelName").asText();
        String city = node.get("city").asText();
        String room = node.get("room").asText();
        String guestName = node.get("guestName").asText();
        for (long time = startDate.getTime(); time <= endDate.getTime(); time += DAY) {
            result.add(new Booking(hotelName, city, room, new Date(time), guestName));
        }
        return result;
    }

    public static long getDAY() {
        return DAY;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getBookingDay() {
        return bookingDay;
    }

    public void setBookingDay(Date bookingDay) {
        this.bookingDay = bookingDay;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "hotelName='" + hotelName + '\'' +
                ", city='" + city + '\'' +
                ", room='" + room + '\'' +
                ", bookingDay=" + bookingDay +
                ", guestName='" + guestName + '\'' +
                '}';
    }
}
