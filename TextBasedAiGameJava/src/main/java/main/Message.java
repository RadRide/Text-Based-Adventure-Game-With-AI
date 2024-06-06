package main;

public class Message {

    private String role;
    private String message;

    public Message(String role, String message) {
        this.role = role;
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "role: " + role + ", message: " + message + "|";
    }
}
