package Models;

import java.util.Objects;

public class Message {
    public String username;
    public String type;
    public String message;
    public String repliedToMessage;
    public String messageBeforeEdit;

    public Message(String username, String type, String message) {
        this.username = username;
        this.type = type;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(username, message1.username) && Objects.equals(type, message1.type) && Objects.equals(repliedToMessage, message1.repliedToMessage) && Objects.equals(message, message1.message);
    }

}
