package ru.ssugt.drones.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.ssugt.drones.api.dto.response.file.FileResponse;

import java.util.List;

public interface FileStorageService {

    List<FileResponse> getFiles(String dockStationId, String droneId);

    InputStreamResource getResource(String id);

}
