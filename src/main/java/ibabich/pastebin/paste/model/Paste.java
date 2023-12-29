package ibabich.pastebin.paste.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ibabich.pastebin.hashgenerator.Hash;
import ibabich.pastebin.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content; // TODO: replace with a link to S3
    private LocalDateTime createdAt; // TODO: add time zones
    private ExpirationOptions expiresAt; // TODO: createdAt + ExpirationOption
    private AccessLevel accessLevel;

    @OneToOne(optional = true)
    @JoinColumn(name = "hash_id")
    private Hash hash;

    @ManyToOne
    @JoinColumn(name = "application_user_id")
    @JsonIgnore
    private User userId;

    private String pasteTitle;
    private String password;
}
