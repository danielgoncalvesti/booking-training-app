CREATE KEYSPACE Booking WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE Booking.hotels (
  hotel_name text,
  city text,
  description text,
  rooms SET<text>,
  PRIMARY KEY ((city), hotel_name)
);

INSERT INTO Booking.hotels(hotel_name, city, description, rooms)
                    VALUES('Bolshaya Moskva', 'Odessa', 'Bolshaya Moskva hotel', {'1', '2', '11', '12'});

CREATE TABLE Booking.booking (
  hotel_name text,
  room text,
  city text,
  booking_day timestamp,
  guest_name text,
  PRIMARY KEY ((city), hotel_name, booking_day, room)
);

CREATE TABLE Booking.guests (
  city text,
  hotel_name text,
  guest_name text,
  PRIMARY KEY ((city), hotel_name, guest_name)
);


CREATE TABLE Booking.guest_booking (
  city text,
  hotel_name text,
  guest_name text,
  room text,
  booking_day timestamp,
  PRIMARY KEY ((city), hotel_name, guest_name, booking_day, room)
);


