package com.blogspot.athletio;



import java.util.HashMap;
/**
 * Created by tanvir on 8/24/17.
 */

public class UserData {
    int height;
    int weight;
    HashMap<String, Integer> weightMap;
    HashMap<String, Integer> stepCountMap;
    HashMap<String, Integer> calorieMap;
    HashMap<String,String> friends;
    HashMap<String,String> workouts;
    HashMap<String,String> posts;

    public UserData() {
    }

    public UserData(int height, int weight) {
        this.height = height;
        this.weight = weight;
        this.weightMap = new HashMap<String, Integer>();
        this.stepCountMap = new HashMap<String, Integer>();
        this.calorieMap = new HashMap<String, Integer>();
        this.friends=new HashMap<String,String>();
        this.workouts=new HashMap<String,String>();
        this.posts=new HashMap<String,String>();
        this.weightMap.put(new Day().toString(),weight);
        this.stepCountMap.put(new Day().toString(),0);
        this.calorieMap.put(new Day().toString(),0);
    }

    public UserData(int height, int weight, HashMap<String, Integer> weightMap, HashMap<String, Integer> stepCountMap, HashMap<String, Integer> calorieMap, HashMap<String, String> friends, HashMap<String, String> workouts, HashMap<String, String> posts) {
        this.height = height;
        this.weight = weight;
        this.weightMap = weightMap;
        this.stepCountMap = stepCountMap;
        this.calorieMap = calorieMap;
        this.friends = friends;
        this.workouts = workouts;
        this.posts = posts;
    }

    public UserData(String str) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(str);
        this.height=jsonObjectParser.getInt("height");
        this.weight=jsonObjectParser.getInt("weight");
        JsonObjectParser jsonCalorieMap=new JsonObjectParser(jsonObjectParser.getString("calorieMap"));
        this.calorieMap=jsonCalorieMap.getIntMap();
        JsonObjectParser jsonStepCountMap=new JsonObjectParser(jsonObjectParser.getString("stepCountMap"));
        this.stepCountMap=jsonStepCountMap.getIntMap();
        JsonObjectParser jsonWeightMap=new JsonObjectParser(jsonObjectParser.getString("weightMap"));
        this.weightMap=jsonWeightMap.getIntMap();
        if(jsonObjectParser.getString("friends")!=null){
            JsonObjectParser jsonFriends=new JsonObjectParser(jsonObjectParser.getString("friends"));
            this.friends=jsonFriends.getMap();
        }
        else
            this.friends=new HashMap<String,String>();
        if(jsonObjectParser.getString("workouts")!=null){
            JsonObjectParser jsonWorkouts=new JsonObjectParser(jsonObjectParser.getString("workouts"));
            this.workouts=jsonWorkouts.getMap();
        }
        else
            this.workouts=new HashMap<String,String>();
        if(jsonObjectParser.getString("posts")!=null){
            JsonObjectParser jsonPosts=new JsonObjectParser(jsonObjectParser.getString("posts"));
            this.posts=jsonPosts.getMap();
        }
        else
            this.posts=new HashMap<String,String>();
    }

    @Override
    public String toString() {
        return "{" +
                "height=" + height +
                ", weight=" + weight +
                ", weightMap=" + weightMap +
                ", stepCountMap=" + stepCountMap +
                ", calorieMap=" + calorieMap +
                ", friends=" + friends +
                ", workouts=" + workouts +
                ", posts=" + posts +
                '}';
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public HashMap<String, Integer> getWeightMap() {
        return weightMap;
    }

    public void setWeightMap(HashMap<String, Integer> weightMap) {
        this.weightMap = weightMap;
    }

    public HashMap<String, Integer> getStepCountMap() {
        return stepCountMap;
    }

    public void setStepCountMap(HashMap<String, Integer> stepCountMap) {
        this.stepCountMap = stepCountMap;
    }

    public HashMap<String, Integer> getCalorieMap() {
        return calorieMap;
    }

    public void setCalorieMap(HashMap<String, Integer> calorieMap) {
        this.calorieMap = calorieMap;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, String> friends) {
        this.friends = friends;
    }

    public HashMap<String, String> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(HashMap<String, String> workouts) {
        this.workouts = workouts;
    }

    public HashMap<String, String> getPosts() {
        return posts;
    }

    public void setPosts(HashMap<String, String> posts) {
        this.posts = posts;
    }
}
