package com.blogspot.athletio;

/**
 * Created by tanvir on 9/27/17.
 */

public class SmallUser {
    String UID;
    String name;
    String photo;

    public SmallUser(String UID, String name, String photo) {
        this.UID = UID;
        this.name = name;
        this.photo = photo;
    }

    public SmallUser(String jsonStr) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.name=jsonObjectParser.getString("name");
        this.photo=jsonObjectParser.getString("photo");
        this.UID=jsonObjectParser.getString("UID");
    }

    @Override
    public String toString() {
        return "SmallUser{" +
                "name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
