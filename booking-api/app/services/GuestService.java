package services;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Booking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles guest routines
 * Created by Gleb on 01-Dec-16.
 */
@Singleton
public class GuestService {

    private final Session session;
    //private final PreparedStatement selectGuestBooking;

    @Inject
    public GuestService(Session session) {
        this.session = session;
        //selectGuestBooking = session.prepare("");
    }


    public List<Booking> getRoomsByGuest(String guest, Date bookingDate) {
        return new ArrayList<>();
    }
}
