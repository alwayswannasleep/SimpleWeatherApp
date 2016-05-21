package vkraevskiy.com.simpleweatherapp.db.model;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    private long syncTimestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(long syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }
}
