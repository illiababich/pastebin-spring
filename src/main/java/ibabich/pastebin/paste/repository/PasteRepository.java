package ibabich.pastebin.paste.repository;

import ibabich.pastebin.hashgenerator.Hash;
import ibabich.pastebin.paste.model.Paste;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasteRepository extends JpaRepository<Paste, String> {
    @Override
    Optional<Paste> findById(@Nullable String id);

    @Query("SELECT p FROM Paste p WHERE p.hash.hash = ?1")
    Optional<Paste> findByHash(String hashValue);
}
