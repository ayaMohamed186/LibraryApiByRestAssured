package pojo;

public class createUserBody {

    String firstName, lastName, email;

    public String getFirstName() {
        return firstName;
    }

    public createUserBody setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public createUserBody setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public createUserBody setEmail(String email) {
        this.email = email;
        return this;
    }


}
