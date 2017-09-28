package com.blogspot.athletio;

/**
 * Created by tanvir on 9/27/17.
 */

public class Post {
    public static final int TEXT=0;
    public static final int PHOTO=1;

    String UID;
    String uName;
    String postId;
    String body;
    String photoUri;
    int type;

    public Post(String UID, String uName, String postId, String body, int type) {
        this.UID = UID;
        this.uName = uName;
        this.postId = postId;
        this.body = body;
        this.type = type;
    }

    public Post(String UID, String uName, String postId, String body, String photoUri, int type) {
        this.UID = UID;
        this.uName = uName;
        this.postId = postId;
        this.body = body;
        this.photoUri = photoUri;
        this.type=type;
    }

    public Post(String jsonStr) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.UID=jsonObjectParser.getString("UID");
        this.uName=jsonObjectParser.getString("uName");
        this.postId=jsonObjectParser.getString("postId");
        this.body=jsonObjectParser.getString("body");
        this.type=jsonObjectParser.getInt("type");
        if(this.type==Post.PHOTO){
            this.photoUri=jsonObjectParser.getString("photoUri");
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "UID='" + UID + '\'' +
                ", uName='" + uName + '\'' +
                ", postId='" + postId + '\'' +
                ", body='" + body + '\'' +
                ", photoUri='" + photoUri + '\'' +
                ", type=" + type +
                '}';
    }
}
