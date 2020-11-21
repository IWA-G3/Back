package polytech.group3.iwa.alert_contact_case.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class Registration {

    @Email(message = "Votre adresse email est invalide")
    @NotEmpty(message = "Votre adresse email est invalide")
    private String email;

    @Size(min = 6, message = "Votre mot de passe doit faire au moins 6 caract\u00e8res")
    private  String password;

    @NotEmpty(message = "Veuillez confirmer votre mot de passe")
    private String password_confirm;

    public Registration(String email, String password, String password_confirm) {
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

    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password_confirm='" + password_confirm + '\'' +
                '}';
    }
}

