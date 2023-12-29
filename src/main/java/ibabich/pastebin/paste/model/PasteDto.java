package ibabich.pastebin.paste.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PasteDto {
    private String content;
    private ExpirationOptions expiresAt;
    private AccessLevel accessLevel;
    private Long creatorId;
    private String pasteTitle;
    private String password;
}
