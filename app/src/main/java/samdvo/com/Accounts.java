package samdvo.com;

public class Accounts {

    private String Username;
    private String Password;
    private String Fullname;
    private String Usertype;

    public Accounts() {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getUsertype() { return Usertype; }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }
}
