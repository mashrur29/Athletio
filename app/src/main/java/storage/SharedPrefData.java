package storage;

import android.content.Context;
import android.content.SharedPreferences;

import general.User;
import general.UserData;
import general.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tanvir on 8/29/17.
 */

public class SharedPrefData {
    public Context context;

    public static final String SAVED="saved";
    public static final String USERINFO="Userinfo";
    public static final String DISPLAYNAME="displayName";
    public static final String BIRTHDATE="birthDate";
    public static final String BIRTHMONTH="birthMonth";
    public static final String BIRTHYEAR="birthYear";
    public static final String GENDER="gender";
    public static final String EMAIL="email";
    public static final String HEIGHT="height";
    public static final String WEIGHT= "weight";
    public static final String WEIGHTMAP="weightMap";
    public static final String STEPCOUNTMAP="stepCountMap";
    public static final String CALORIEMAP="calorieMap";
    public static final String EVENTS="events";
    public static final String LATEST="latest";

    public SharedPrefData(Context context) {
        this.context = context;
    }
    public void saveEventReminderKey(String key,int reqId){
        SharedPreferences pref = context.getSharedPreferences(EVENTS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, reqId);
        editor.commit();
    }
    public Vector<String> getEventReminderKeys(){

        SharedPreferences pref = context.getSharedPreferences(EVENTS, MODE_PRIVATE);
        Map<String,?> mp= pref.getAll();
        Vector<String> ret=new Vector<String>();
        for(Map.Entry<String,?> entry : mp.entrySet()){
            ret.add(entry.getKey().toString());

        }
        ret.remove(SharedPrefData.LATEST);
        return ret;
    }
    public int getEventReminderKey(String key){
        SharedPreferences pref = context.getSharedPreferences(EVENTS, MODE_PRIVATE);
        return pref.getInt(key,-1);
    }
    public boolean hasEventReminderKey(String key){
        return getEventReminderKey(key)!=-1;
    }
    public void removeEventKey(String key){

        SharedPreferences pref = context.getSharedPreferences(EVENTS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }
    public void saveUser(User user){
        clear();

        SharedPreferences pref = context.getSharedPreferences(USERINFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(DISPLAYNAME,user.getUserInfo().getDisplayName());
        editor.putInt(BIRTHDATE,user.getUserInfo().getBirthDate());
        editor.putInt(BIRTHMONTH,user.getUserInfo().getBirthMonth());
        editor.putInt(BIRTHYEAR,user.getUserInfo().getBirthYear());
        editor.putString(GENDER,user.getUserInfo().getGender());
        editor.putString(EMAIL,user.getUserInfo().getEmail());
        editor.putInt(HEIGHT,user.getUserData().getHeight());
        editor.putInt(WEIGHT,user.getUserData().getWeight());
        editor.commit();

        SharedPreferences weightMapPref = context.getSharedPreferences(WEIGHTMAP, MODE_PRIVATE);
        SharedPreferences.Editor weightMapEditor = weightMapPref.edit();
        for(Map.Entry m:user.userData.getWeightMap().entrySet()){
            weightMapEditor.putInt(m.getKey().toString(),(int)m.getValue());
        }
        weightMapEditor.commit();
        SharedPreferences stepCountMapPref = context.getSharedPreferences(STEPCOUNTMAP, MODE_PRIVATE);
        SharedPreferences.Editor stepCountMapeditor =  stepCountMapPref.edit();
        for(Map.Entry m:user.userData.getStepCountMap().entrySet()){
            stepCountMapeditor.putInt(m.getKey().toString(),(int)m.getValue());
        }
        stepCountMapeditor.commit();

        SharedPreferences calorieMapPref = context.getSharedPreferences(CALORIEMAP, MODE_PRIVATE);
        SharedPreferences.Editor calorieMapeditor =  calorieMapPref.edit();
        for(Map.Entry m:user.userData.getCalorieMap().entrySet()){
            calorieMapeditor.putInt(m.getKey().toString(),(int)m.getValue());
        }
        calorieMapeditor.commit();




        SharedPreferences prefSaved = context.getSharedPreferences(SAVED, MODE_PRIVATE);
        SharedPreferences.Editor editorSaved = prefSaved.edit();
        editorSaved.putInt(SAVED,1);
        editorSaved.commit();
    }
    public void clear(){
        SharedPreferences pref = context.getSharedPreferences(USERINFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        SharedPreferences weightMapPref = context.getSharedPreferences(WEIGHTMAP, MODE_PRIVATE);
        SharedPreferences.Editor weightMapEditor = weightMapPref.edit();
        weightMapEditor.clear();
        weightMapEditor.commit();


        SharedPreferences stepCountMapPref = context.getSharedPreferences(STEPCOUNTMAP, MODE_PRIVATE);
        SharedPreferences.Editor stepCountMapeditor =  stepCountMapPref.edit();
        stepCountMapeditor.clear();
        stepCountMapeditor.commit();

        SharedPreferences calorieMapPref = context.getSharedPreferences(CALORIEMAP, MODE_PRIVATE);
        SharedPreferences.Editor calorieMapeditor =  calorieMapPref.edit();
        calorieMapeditor.clear();
        calorieMapeditor.commit();


        SharedPreferences prefSaved = context.getSharedPreferences(SAVED, MODE_PRIVATE);
        SharedPreferences.Editor editorSaved = prefSaved.edit();
        editorSaved.putInt(SAVED,0);
        editorSaved.commit();
    }
    public boolean getSaved(){
        SharedPreferences prefSaved = context.getSharedPreferences(SAVED, MODE_PRIVATE);
        return prefSaved.getInt(SAVED,0)==1;
    }
    public User getUser(){
        if(!getSaved())return null;
        SharedPreferences pref = context.getSharedPreferences(USERINFO, MODE_PRIVATE);
        User ret=new User();
        ret.userInfo=new UserInfo();
        ret.userData=new UserData();
        ret.userInfo.setDisplayName(pref.getString(DISPLAYNAME,""));
        ret.userInfo.setBirthDate(pref.getInt(BIRTHDATE,0));
        ret.userInfo.setBirthMonth(pref.getInt(BIRTHMONTH,0));
        ret.userInfo.setBirthYear(pref.getInt(BIRTHYEAR,0));
        ret.userInfo.setEmail(pref.getString(EMAIL,""));
        ret.userInfo.setGender(pref.getString(GENDER,""));
        ret.userData.setHeight(pref.getInt(HEIGHT,0));
        ret.userData.setWeight(pref.getInt(WEIGHT,0));


        SharedPreferences weightMapPref = context.getSharedPreferences(WEIGHTMAP, MODE_PRIVATE);
        HashMap<String, Integer> weightMap=new HashMap<String,Integer>();
        Map<String,?> keys = weightMapPref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            weightMap.put(entry.getKey().toString(),weightMapPref.getInt(entry.getKey().toString(),0));
        }

        SharedPreferences stepMapPref = context.getSharedPreferences(STEPCOUNTMAP, MODE_PRIVATE);
        HashMap<String, Integer> stepCountMap=new HashMap<String,Integer>();
        keys = stepMapPref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            stepCountMap.put(entry.getKey().toString(),stepMapPref.getInt(entry.getKey().toString(),0));
        }

        SharedPreferences callorieMapPref = context.getSharedPreferences(CALORIEMAP, MODE_PRIVATE);
        HashMap<String, Integer> calorieMap=new HashMap<String,Integer>();
        keys = callorieMapPref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            calorieMap.put(entry.getKey().toString(),callorieMapPref.getInt(entry.getKey().toString(),0));
        }
        ret.userData.setWeightMap(weightMap);
        ret.userData.setStepCountMap(stepCountMap);
        ret.userData.setCalorieMap(calorieMap);
        return ret;
    }


}
