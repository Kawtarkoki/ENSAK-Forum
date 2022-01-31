package com.forum.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forum.model.role.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class User {
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
    private String password;

    @NotNull(message = "pick a role")
    private UserRole role;

    private Boolean enabled = true;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Post> posts;

    public User(RegistrationRequest registrationRequest){
        this.username = registrationRequest.getUsername();
        this.role = registrationRequest.getRole();
        this.enabled = registrationRequest.getEnabled();
        this.firstName =registrationRequest.getFirstName();
        this.familyName = registrationRequest.getFamilyName();
        this.password = registrationRequest.getPassword();
    }

    public User(String username,String firstName ,String familyName, String password, UserRole role, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.firstName =firstName;
        this.familyName = familyName;
    }

    public User(Long id, String firstName ,String familyName, String username) {
        this.id = id;
        this.firstName =firstName;
        this.familyName = familyName;
        this.username = username;
        this.password = null;
        this.role = null;
        this.enabled = true;
        this.comments =null;
        this.likes =null;
        this.posts = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", enabled=" + enabled +
                "}";
    }
}