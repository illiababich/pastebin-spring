package ibabich.pastebin.hashgenerator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashRepository extends JpaRepository<Hash, Long> {
    Hash findFirstByPasteIsNull(); // TODO: should it check the last 10 records? there is no need to scan old records that already have a paste assigned
    long countByPasteIsNull();
}
