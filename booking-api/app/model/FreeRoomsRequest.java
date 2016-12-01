package model;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 01-Dec-16.
 */
public class FreeRoomsRequest {
    private String hotelName;
    private String city;
    private Date startReserveTime;
    private Date endReserveTime;

    public FreeRoomsRequest(JsonNode node) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");
        startReserveTime = format.parse(node.get("startReserveTime").asText());
        endReserveTime = format.parse(node.get("endReserveTime").asText());
        hotelName = node.get("hotelName").asText();
        city = node.get("city").asText();
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

    public Date getStartReserveTime() {
        return startReserveTime;
    }

    public void setStartReserveTime(Date startReserveTime) {
        this.startReserveTime = startReserveTime;
    }

    public Date getEndReserveTime() {
        return endReserveTime;
    }

    public void setEndReserveTime(Date endReserveTime) {
        this.endReserveTime = endReserveTime;
    }
}
