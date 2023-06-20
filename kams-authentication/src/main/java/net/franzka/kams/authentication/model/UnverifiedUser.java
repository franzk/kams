package net.franzka.kams.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.franzka.kams.authentication.service.RandomString;

import java.time.LocalDateTime;

/**
 * Describes an unverified user.                                 <br>
 * An unverified user is created when a new user registers.     <br>
 * It contains his email / password / role and an activation token with its creation time.
 */
@Entity
@Table(name="unverified_user")
@Getter
@Setter
public class UnverifiedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unverified_user_id")
    private Integer id;

    private String email;
    private String password;
    private String role = "USER";
    private String activationToken;
    private LocalDateTime creationTime;
}
