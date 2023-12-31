package ibabich.pastebin.hashgenerator.service;

import ibabich.pastebin.hashgenerator.model.Hash;
import ibabich.pastebin.logging.service.DatabaseLogger;
import ibabich.pastebin.logging.model.ErrorLevel;
import ibabich.pastebin.logging.repository.LogEntryRepository;
import ibabich.pastebin.paste.repository.PasteRepository;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@AllArgsConstructor
public class HashGenerator {
    private final LogEntryRepository logEntryRepository;
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public Hash generateUniqueHash(PasteRepository pasteRepository) throws NoSuchAlgorithmException {
        Hash hash = new Hash();
        String generatedHash;

        do {
            generatedHash = generateHash();
            if (pasteRepository.findByHash(generatedHash).isPresent()) {
                DatabaseLogger databaseLogger = new DatabaseLogger();
                databaseLogger.createLog(
                        ErrorLevel.COLLISION,
                        "The collision occurred while creating a hash: " + generatedHash,
                        logEntryRepository);
            }
        } while (pasteRepository.findByHash(generatedHash).isPresent());
        hash.setHash(generatedHash);

        return hash;
    }

    /**
     * <p>Example of a hash: sdfD9Oi43n</p>
     *
     * <p>A total of: (26[lowercase English letters] * 2[uppercase English letters]) + 10[digits]</p>
     * <p>= 839.299.365.868.340.200 unique hashes</p>
     */
    private static String generateHash() throws NoSuchAlgorithmException {
        String input = LocalDateTime.now().toString();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, hashBytes);
        StringBuilder encoded = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = number.divideAndRemainder(BigInteger.valueOf(BASE));
            number = divmod[0];
            encoded.insert(0, ALPHABET.charAt(divmod[1].intValue()));
        }

        return encoded.substring(0, Math.min(encoded.length(), 10));
    }
}
