package ibabich.pastebin.hashgenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ibabich.pastebin.paste.model.Paste;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hash")
@NoArgsConstructor
public class Hash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String hash;

    @OneToOne(optional = true)
    @JoinColumn(name = "paste_id")
    @Setter
    @JsonIgnore
    private Paste paste;

    public Hash(String hash) {
        this.hash = hash;
    }
}
