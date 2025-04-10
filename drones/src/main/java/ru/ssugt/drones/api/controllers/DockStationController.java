package ru.ssugt.drones.api.controllers;

import io.minio.GetObjectArgs;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssugt.drones.api.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.api.dto.response.dockstation.DockStationResponse;
import ru.ssugt.drones.api.dto.response.dockstation.DockStationsResponse;
import ru.ssugt.drones.api.dto.response.file.FileResponse;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.service.DockStationService;
import ru.ssugt.drones.service.FileStorageService;
import ru.ssugt.drones.service.FileStorageServiceImpl;

import java.util.List;

@RestController
@RequestMapping("api/v1/dock-station")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DockStationController {

    DockStationService dockStationService;
    FileStorageService fileStorageService;



    @Autowired
    public DockStationController(DockStationService dockStationService, FileStorageService fileStorageService) {
        this.dockStationService = dockStationService;
        this.fileStorageService = fileStorageService;
    }

    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<DockStationResponse> registerDockStation(@RequestBody DockStationRegisterRequest dockStationRegisterRequest) {
        DockStation dockStation = dockStationService.register(dockStationRegisterRequest);
        return ResponseEntity.ok(DockStationResponse.builder()
                .id(dockStation.getId())
                .build());
    }


    @GetMapping("/get-all")
    public ResponseEntity<DockStationsResponse> getAllDockStations() {
        return ResponseEntity.ok(DockStationsResponse.builder().dockStations(dockStationService.getAll()).build());
    }

    @GetMapping("/{dockStationId}/{droneId}/media")
    public ResponseEntity<List<FileResponse>> getDroneMediaById(@PathVariable String dockStationId, @PathVariable String droneId) {
        return ResponseEntity.ok(fileStorageService.getFiles(dockStationId, droneId));
    }

    @GetMapping("/media/{stationId}/{droneId}/{mediaId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String stationId,
            @PathVariable String droneId,
            @PathVariable String mediaId) {

        String objectPath = String.format("%s/%s/%s",
                stationId, droneId, mediaId);
        InputStreamResource resource = fileStorageService.getResource(objectPath);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(resource);
    }
}
