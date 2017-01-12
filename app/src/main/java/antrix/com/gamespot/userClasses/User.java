package antrix.com.gamespot.userClasses;

/**
 * Created by aangjnr on 15/12/2016.
 */

public class User {
    String name, email, phone, age, photo;

    public User(){


    }

    public User(String name, String email, String phone, String age, String photo){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.photo = photo;

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

    public void setUserPhoto (String photo){
        this.photo = photo;

    }


    public String getUserName(){
        return name;

    }

    public String getUserAge(){
        return age;

    }

    public String getUserPhone(){
        return phone;

    }

    public String getUserEmail(){
        return email;

    }

    public String getUserPhoto(){
        return photo;

    }
}
