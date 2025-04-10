package ru.ssugt.drones.service;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.ssugt.drones.api.dto.response.file.FileResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${drone.minio.bucketName}")
    String bucketName = "drones";

    MinioClient minioClient;

    @Autowired
    public FileStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public List<FileResponse> getFiles(String dockStationId, String droneId) {
        try {
            List<FileResponse> fileResponses = new ArrayList<>();
            String objectPrefix = dockStationId + "/" + droneId + "/";

            Iterable<Result<Item>> items = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(objectPrefix)
                            .build());

            for (Result<Item> result : items) {
                Item item = result.get();
                if (!item.isDir()) {
                    String filename = item.objectName().substring(objectPrefix.length());
                    MediaType contentType = determineMediaType(filename);

                    try (InputStream stream = minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(item.objectName())
                                    .build())) {

                        byte[] bytes = IOUtils.toByteArray(stream);
                        String base64Content = Base64.getEncoder().encodeToString(bytes);

                        fileResponses.add(new FileResponse(
                                item.objectName(),
                                filename,
                                contentType,
                                item.size(),
                                item.lastModified().toLocalDateTime(),
                                base64Content
                        ));
                    }
                }
            }
            return fileResponses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InputStreamResource getResource(String id) {
        try {
            return new InputStreamResource(
                    minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(id)
                                    .build()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MediaType determineMediaType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".mp4")) {
            return MediaType.valueOf("video/mp4");
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
