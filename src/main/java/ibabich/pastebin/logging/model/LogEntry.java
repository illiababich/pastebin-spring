package ibabich.pastebin.logging.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "logs")
@Getter
@Setter
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateTime;
    private ErrorLevel errorLevel;
    private String errorMessage;
}
