package model;

import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

/**
 * Hotel entity
 * Created by Gleb Popov on 29-Nov-16.
 */
public class Hotel {
    private String name;
    private String city;
    private String description;
    private Set<String> rooms;

    public Hotel(Row dataRow) {
        name = dataRow.getString("hotel_name");
        city = dataRow.getString("city");
        description = dataRow.getString("description");
        rooms = dataRow.getSet("rooms", String.class);
    }
    public Hotel(JsonNode json) {
        System.out.println(json);
        name = json.get("name").asText();
        city = json.get("city").asText();
        description = json.get("description").asText();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getRooms() {
        return rooms;
    }

    public void setRooms(Set<String> rooms) {
        this.rooms = rooms;
    }
}
