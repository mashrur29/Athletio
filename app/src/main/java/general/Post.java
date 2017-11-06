package general;

import java.util.List;

import utility.JsonObjectParser;


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
    public int nLikes;
    public int nComments;
    public List<Comment> cList;

    public Post(String UID, String UDisplayPicURI, String uName, String postId, String body, String photoUri, int type, Day day, int hour, int min, int nLikes, int nComments, List<Comment> cList) {
        this.UID = UID;
        this.UDisplayPicURI = UDisplayPicURI;
        this.uName = uName;
        this.postId = postId;
        this.body = body;
        this.photoUri = photoUri;
        this.type = type;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.nLikes = nLikes;
        this.nComments = nComments;
        this.cList = cList;
    }

    public Post(String UID, String UDisplayPicURI, String uName, String postId, String body, String photoUri, int type, Day day, int hour, int min, int nLikes, int nComments) {
        this.UID = UID;
        this.UDisplayPicURI = UDisplayPicURI;
        this.uName = uName;
        this.postId = postId;
        this.body = body;
        this.photoUri = photoUri;
        this.type = type;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.nLikes = nLikes;
        this.nComments = nComments;
    }

    public Post(String UID, String UDisplayPicURI, String uName, String postId, String body, int type, Day day, int hour, int min) {
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
        this.nLikes=jsonObjectParser.getInt("nLikes");
        if(this.type==Post.PHOTO){
            this.photoUri=jsonObjectParser.getString("photoUri");
        }

/*        HashMap<String, String> mapTemp;
        mapTemp = new JsonObjectParser(jsonObjectParser.getString("commentMap")).getMap();
        for(Map.Entry m:mapTemp.entrySet()){
            cList.add(new Comment(m.getValue().toString()));
        }*/
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

