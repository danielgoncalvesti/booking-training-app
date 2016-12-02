package model;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;

/**
 * Request object to retrieve Guest booking
 * Created by Gleb Popov on 02-Dec-16.
 */
public class GuestBookingRequest extends FreeRoomsRequest {

    private String guestName;

    public GuestBookingRequest(JsonNode node) throws ParseException {
        super(node);
        guestName = node.get("guestName").asText();
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
}
