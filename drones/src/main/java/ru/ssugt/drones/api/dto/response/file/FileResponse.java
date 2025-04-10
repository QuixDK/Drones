package ru.ssugt.drones.api.dto.response.file;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL, valueFilter = RepresentationModel.class)
public class FileResponse extends RepresentationModel<FileResponse> implements Serializable {
    String id;
    String filename;
    MediaType contentType;
    Long fileSize;
    LocalDateTime createdTime;
    String content;
}