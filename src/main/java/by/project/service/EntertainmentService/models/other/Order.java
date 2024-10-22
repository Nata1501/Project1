package by.project.service.EntertainmentService.models.other;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class Order {
    @NotNull
    private Integer userId;

    @NotNull
    private Integer eventId;

    @NotNull
    private Integer seat;

}
