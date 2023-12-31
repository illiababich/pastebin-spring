package ibabich.pastebin.hashgenerator.repository;

import ibabich.pastebin.hashgenerator.model.Hash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashRepository extends JpaRepository<Hash, Long> {
    Hash findFirstByPasteIsNull(); // TODO: should it check the last 10 records? there is no need to scan old records that already have a paste assigned
    long countByPasteIsNull();
}
