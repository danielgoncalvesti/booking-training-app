CREATE KEYSPACE Booking WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
CREATE TABLE Booking.users (
  user_name varchar PRIMARY KEY,
  password varchar,
  gender varchar,
  session_token varchar,
  state varchar,
  birth_year bigint
);


INSERT INTO Booking.users (user_name, password, gender, session_token, state, birth_year) VALUES ('Admin', 'Password', 'M', 'token', 'Alabama', 1977);

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


