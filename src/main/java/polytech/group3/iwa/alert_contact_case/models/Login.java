package polytech.group3.iwa.alert_contact_case.models;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Login {

    @Email(message = "Votre adresse email est invalide")
    @NotEmpty(message = "Votre adresse email est invalide")
    private String email;

    @Size(min = 6, message = "Votre mot de passe doit faire au moins 6 caract\u00e8res")
    private  String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
