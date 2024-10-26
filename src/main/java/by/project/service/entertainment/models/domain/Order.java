package by.project.service.entertainment.models.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @NotNull
    private Long userId;

    @NotNull
    private Long eventId;

    @NotNull
    private Integer seat;

}
