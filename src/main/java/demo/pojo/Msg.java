package demo.pojo;

public class Msg {
    private String toID;
    private String toRegistrationID;
    private String msg;

    public String getToID() {
        return toID;
    }

    public String getMsg() {
        return msg;
    }

    public String getToRegistrationID() {
        return toRegistrationID;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public void setToRegistrationID(String toRegistrationID) {
        this.toRegistrationID = toRegistrationID;
    }
}
