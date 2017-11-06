package general;

import utility.JsonObjectParser;

/**
 * Created by tanvir on 10/24/17.
 */

public class SmallStep {
    String name;
    int steps;

    public SmallStep(String name, int steps) {
        this.name = name;
        this.steps = steps;
    }
    public SmallStep(String jsonData){
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonData);
        this.name=jsonObjectParser.getString("name");
        this.steps=jsonObjectParser.getInt("steps");

    }

    @Override
    public String toString() {
        return "SmallStep{" +
                "name='" + name + '\'' +
                ", steps=" + steps +
                '}';
    }
}
