package general;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import utility.JsonObjectParser;

/**
 * Created by zero639 on 2017/02/05.
 */

public class Comment implements Serializable {
    private SocialUser user;
    private FirebaseUser firebaseUser;
    private String commentId;
    private long timeCreated;
    private String comment;

    public Comment() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new SocialUser(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid());
    }

    public Comment(SocialUser user, String commentId, long timeCreated, String comment) {

        this.user = user;
        this.commentId = commentId;
        this.timeCreated = timeCreated;
        this.comment = comment;
    }

    public SocialUser getUser() {

        return user;
    }

    public void setUser(SocialUser user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
