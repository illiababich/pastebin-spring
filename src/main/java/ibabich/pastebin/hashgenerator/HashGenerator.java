package ibabich.pastebin.hashgenerator;

import ibabich.pastebin.paste.repository.PasteRepository;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


// example: sdfD9Oi43n
// (26 * 2) + 10 = 839.299.365.868.340.200 unique hashes
// TODO: add logging in case of a collision

@AllArgsConstructor
public class HashGenerator {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public static Hash generateUniqueHash(PasteRepository pasteRepository) throws NoSuchAlgorithmException {
        Hash hash = new Hash();
        String generatedHash;

        do {
            generatedHash = generateHash(); // Your existing method to generate a hash
            System.out.println("HASH: " + generatedHash);
        } while (pasteRepository.findByHash(generatedHash).isPresent()); // Check uniqueness
        hash.setHash(generatedHash);

        return hash;
    }

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
