package pl.czupryn.rob.chat;


public class ChatMessage {
    private String value;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChatMessage(String value) {
        this.value = value;
    }
    public ChatMessage(){

    }
}
