package general;

/**
 * Created by zero639 on 9/30/17.
 */

/*
    Object of Chat for a Chat Bot Message
 */

public class ChatBotMessage {
    private boolean isImage, isMine;
    private String chatContent;

    public ChatBotMessage(String chatContent, boolean isMine, boolean isImage) {
        this.chatContent = chatContent;
        this.isMine = isMine;
        this.isImage = isImage;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }
}

