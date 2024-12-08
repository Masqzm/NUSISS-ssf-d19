package ssf.day19.models;

public class User {
    private String fullname;
    private int age;

    public User() {}
    public User(String fullname, int age) {
        this.fullname = fullname;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [fullname=" + fullname + ", age=" + age + "]";
    }
    
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}