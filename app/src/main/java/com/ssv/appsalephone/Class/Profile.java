package com.ssv.appsalephone.Class;

public class Profile {
    private String name;
    private String email;
    private String phone;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Profile(String name, String email, String address, String phone){
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public Profile(){
        
    }
}
