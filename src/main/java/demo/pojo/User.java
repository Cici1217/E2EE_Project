package demo.pojo;


public class User {
    private int id;
    private String username;
    private String password;
    private String registrationID;


    public User(int id, String username, String password, String registrationID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registrationID = registrationID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registrationID='" + registrationID + '\'' +
                '}';
    }
}
