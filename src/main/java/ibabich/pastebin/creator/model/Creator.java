package ibabich.pastebin.creator.model;

import ibabich.pastebin.paste.model.Paste;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "creator")
@NoArgsConstructor
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwordHash;

    @OneToMany(mappedBy = "creator")
    private List<Paste> pasteList;

    public Creator(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
