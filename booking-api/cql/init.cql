CREATE KEYSPACE Booking WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 2 };
CREATE TABLE Booking.users (
  user_name varchar PRIMARY KEY,
  password varchar,
  gender varchar,
  session_token varchar,
  state varchar,
  birth_year bigint
);


INSERT INTO Booking.users (
  user_name,
  password,
  gender,
  session_token,
  state,
  birth_year
) VALUES (
  'Admin',
  'Password',
  'M',
  'token',
  'Alabama',
  1977
);