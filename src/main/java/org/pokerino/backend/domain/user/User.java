package org.pokerino.backend.domain.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.UserResponse;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique = true, nullable = false)
    String username;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    Date joinDate;
    long chips;
    int gold;
    long experience;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinDate = new Date();
    }

    @NonNull
    public UserResponse toUserResponse() {
        return new UserResponse(
                this.username,
                this.joinDate,
                this.chips,
                this.gold,
                this.experience
        );
    }
}