package ibabich.pastebin.paste.repository;

import ibabich.pastebin.paste.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasteRepository extends JpaRepository<Paste, String> {
    @Query("SELECT p FROM Paste p WHERE p.hash.hash = ?1")
    Optional<Paste> findByHash(String hashValue);
    List<Paste> findByCreator_Id(Long creatorId);
}
