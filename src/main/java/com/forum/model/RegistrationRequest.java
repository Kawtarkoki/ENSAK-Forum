package com.forum.model;



import com.forum.model.role.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank(message = "first name must not be blank")
    private String firstName;

    @NotNull
    @NotBlank(message = "family name must not be blank")
    private String familyName;

    @Column(unique = true)
    @NotBlank(message = "username must not be blank")
    private String username;

    @NotBlank(message = "password name must not be blank")
    @Size(min = 7, max = 18, message = "password must be between 7 and 18 characters")
    private String password;

    @NotNull(message = "pick a role")
    private UserRole role;

    private Boolean enabled = true;

}