package model;

import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Pojo for users table
 * Created by Gleb Popov on 25-Nov-16.
 */
public class User {
    private String userName;
    private String password;
    private String gender;
    private String sessionToken;
    private String state;
    private Long birthYear;

    public User() {}

    public User(JsonNode json) {
        userName = json.get("userName").asText();
        password = json.get("password").asText();
        gender = json.get("gender").asText();
        sessionToken = json.get("sessionToken").asText();
        state = json.get("state").asText();
        birthYear = json.get("birthYear").asLong();
    }
    public User(Row dataRow) {
        userName = dataRow.getString("user_name");
        password = dataRow.getString("password");
        gender = dataRow.getString("gender");
        sessionToken = dataRow.getString("session_token");
        state = dataRow.getString("state");
        birthYear = dataRow.getLong("birth_year");
    }

    public User(String userName, String password, String gender, String sessionToken, String state, Long birthYear) {
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.sessionToken = sessionToken;
        this.state = state;
        this.birthYear = birthYear;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Long birthYear) {
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", state='" + state + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }
}
