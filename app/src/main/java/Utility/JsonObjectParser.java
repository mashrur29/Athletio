package Utility;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanvir on 8/24/17.
 */

public class JsonObjectParser {

    HashMap<String, String> map = new HashMap<String, String>();
    String jsonData;

    public JsonObjectParser(String jsonStr) {
        if(jsonStr==null)jsonStr="";
        jsonStr=jsonStr.replace(", ", ",");
        jsonStr=jsonStr.replace(" ,", ",");
        this.jsonData = jsonStr;
        int b = 0;
        boolean bKey = true;
        String key = "", value = "";
        for (int i = 1; i < jsonData.length() - 1; i++) {
            if (jsonData.charAt(i) == ',' && b == 0) {
                map.put(key, value);
                bKey = true;
                key = "";
                value = "";
            } else if (jsonData.charAt(i) == '=' && bKey == true&&b==0) {
                bKey = false;
            } else {
                if (jsonData.charAt(i) == '{') {
                    b++;
                }
                if (jsonData.charAt(i) == '}') {
                    b--;
                }
                if (bKey == true) {
                    key = key + jsonData.charAt(i);
                } else {
                    value = value + jsonData.charAt(i);
                }
            }
        }
        map.put(key, value);
    }

    public String getString(String str){
        return map.get(str);
    }
    public int getInt(String str){
        return Integer.parseInt(getString(str));
    }
    public double getDouble(String str){return Double.parseDouble(getString(str));}
    public long getLong(String str){return Long.parseLong(getString(str));}
    public HashMap<String, String> getMap(){return map;}
    public HashMap<String,Integer> getIntMap(){
        HashMap<String,Integer> ret=new HashMap<String,Integer>();
        for(Map.Entry m:map.entrySet()){
            ret.put(m.getKey().toString(),Integer.parseInt(m.getValue().toString()));
        }
        return ret;
    }

    public void printJsonObject(){
        for(Map.Entry m:map.entrySet()){
            String log=m.getKey()+"=!="+m.getValue();
            Log.d("printJsonobject",log);
        }
    }
}
