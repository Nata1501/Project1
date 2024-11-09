package by.project.service.entertainment.dto;

import by.project.service.entertainment.models.domain.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    @NotNull
    private Type type;

    @NotNull
    private EntertainmentDTO entertainment;

    @NotNull
    private ObjectClassDTO place;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private Boolean relevance;

}

