package com.xe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xe.entity.api.Exchange;
import com.xe.validation.FieldMatch;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity(name = "users")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldMatch(first = "password", second = "matchingPassword", message = "Password fields must match")
public class User {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_ex",
            joinColumns = {@JoinColumn(name = "us_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "ex_id", referencedColumnName = "exchange_id")}
    )
    Collection<Exchange> exchanges;

    @NotNull(message = "Full Name cannot be empty")
    @Size(min = 1)
    private String fullName;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 3, message = "Password must be at least three characters long")
    private String password;

    @Transient
    private String matchingPassword;

    @NotNull(message = "Email cannot be empty")
    @Size(min = 1)
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String roles;

    public User(String name, String password, String matchingPassword, String mail, Collection<Exchange> exchanges, String... roles) {
        this.fullName = name;
        this.password = password;
        this.email = mail;
        this.matchingPassword = matchingPassword;
        this.exchanges = exchanges;
        this.roles = "USER";
    }

    public String[] getRoles() {
        return roles == null || roles.isEmpty() ? new String[]{}
                : roles.split(":");
    }

    public void setRoles(String roles) {
        this.roles = String.join(":", roles);
    }
}