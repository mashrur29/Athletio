package general;

import utility.JsonObjectParser;

/**
 * Created by tanvir on 9/27/17.
 */

public class Post {
    public static final int TEXT=0;
    public static final int PHOTO=1;

    public String UID;
    public String UDisplayPicURI;
    public String uName;
    public String postId;
    public String body;
    public String photoUri;
    public int type;
    public Day day;
    public int hour;
    public int min;

    public Post(String UID,String UDisplayPicURI, String uName, String postId, String body, int type,Day day,int hour,int min) {
        this.UID = UID;
        this.UDisplayPicURI=UDisplayPicURI;
        this.uName = uName;
        this.postId = postId;
        this.body = body;
        this.type = type;
        this.day=day;
        this.hour=hour;
        this.min=min;
    }

    public Post(String UID,String UDisplayPicURI, String uName, String postId, String body, String photoUri, int type,Day day,int hour,int min) {
        this.UID = UID;
        this.UDisplayPicURI=UDisplayPicURI;
        this.uName = uName;
        this.postId = postId;
        this.body = body;
        this.photoUri = photoUri;
        this.type=type;
        this.day=day;
        this.hour=hour;
        this.min=min;
    }

    public Post(String jsonStr) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.UID=jsonObjectParser.getString("UID");
        this.UDisplayPicURI=jsonObjectParser.getString("UDisplayPicURI");
        this.uName=jsonObjectParser.getString("uName");
        this.postId=jsonObjectParser.getString("postId");
        this.body=jsonObjectParser.getString("body");
        this.type=jsonObjectParser.getInt("type");
        this.day=new Day(jsonObjectParser.getString("day"));
        this.hour=jsonObjectParser.getInt("hour");
        this.min=jsonObjectParser.getInt("min");
        if(this.type==Post.PHOTO){
            this.photoUri=jsonObjectParser.getString("photoUri");
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "UID='" + UID + '\'' +
                ", UDisplayPicURI='" + UDisplayPicURI + '\'' +
                ", uName='" + uName + '\'' +
                ", postId='" + postId + '\'' +
                ", body='" + body + '\'' +
                ", photoUri='" + photoUri + '\'' +
                ", type=" + type +
                ", day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                '}';
    }
}
