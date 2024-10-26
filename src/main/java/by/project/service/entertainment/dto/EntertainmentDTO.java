package by.project.service.entertainment.dto;

import by.project.service.entertainment.models.domain.GenreType;
import by.project.service.entertainment.models.domain.Type;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min=1, max=50, message = "Name should be between 1 and 50 characters")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(min=10, max=300, message = "Description should be between 10 and 300 characters")
    private String description;

    @Min(value=1, message = "Duration should be greater than 0 minutes")
    private double duration;

    @PositiveOrZero(message = "Price should be zero or more rubles")
    private BigDecimal price;

    @Min(value=1895, message = "Year should be greater than 1895")
    @Max(value=2024, message = "Year should not be greater than 2024")
    private int year;

    @NotNull
    private Type type;

    @NotNull
    private GenreType genreType;

    @NotNull
    private ObjectClassDTO director;

    private List<ObjectClassDTO> performers;

}