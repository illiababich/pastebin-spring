package ibabich.pastebin.s3.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
//import software.amazon.awssdk.core.ResponseInputStream;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//@Component
public class StorageConfig {
//    private final S3Client s3Client;
//
//    StorageConfig(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    void readFile() throws IOException {
//        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
//                request -> request.bucket("bucket-name").key("file-name.txt"));
//
//        String fileContent = StreamUtils.copyToString(response, StandardCharsets.UTF_8);
//
//        System.out.println(fileContent);
//    }
}
