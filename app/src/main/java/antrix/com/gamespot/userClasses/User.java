package antrix.com.gamespot.userClasses;

/**
 * Created by aangjnr on 15/12/2016.
 */

public class User {
    String name, email, phone, age;

    public User(){


    }

    public User(String name, String email, String phone, String age){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;

    }


    public void setUserName (String name){
        this.name = name;

    }

    public void setUserEmail (String email){
        this.email = email;

    }

    public void setUserAge (String age){
        this.age = age;

    }

    public void setUserPhone (String phone){
        this.phone = phone;

    }
}
