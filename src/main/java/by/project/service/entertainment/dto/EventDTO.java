package by.project.service.entertainment.dto;

import by.project.service.entertainment.models.domain.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    @NotNull
    private Type type;

    @NotNull
    private EntertainmentDTO enterteinment;

    @NotNull
    private ObjectClassDTO place;

    @NotNull
    private Date dateTime;

    @NotNull
    private Boolean relevance;

}
