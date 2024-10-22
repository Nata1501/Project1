package by.project.service.EntertainmentService.dto;

import by.project.service.EntertainmentService.models.other.GenreType;
import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.models.other.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;



public class EntertainmentDTO {
    @NotEmpty(message = "Name should not be empty")
    @Size(min=1, max=50, message = "Name should be between 1 and 50 characters")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(min=10, max=300, message = "Description should be between 10 and 300 characters")
    private String description;

    @Min(value=1, message = "Duration should be greater than 0 minutes")
    private double duration;

    @Min(value=0, message = "Price should be zero or more rubles")
    private double price;

    @Min(value=1895, message = "Year should be greater than 1895")
    @Max(value=2024, message = "Year should not be greater than 2024")
    private int year;

    @NotNull
    @ValueOfEnum(enumClass = Type.class)
    private String type;

    @NotNull
    @Valid
    @ValueOfEnum(enumClass = GenreType.class)
    private String genretype;

    @NotNull
    private ObjectClassDTO director;

    private List<ObjectClassDTO> performers;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ObjectClassDTO> getPerformers() {
        return performers;
    }

    public void setPerformers(List<ObjectClassDTO> performers) {
        this.performers = performers;
    }

    public ObjectClassDTO getDirector() {
        return director;
    }

    public void setDirector(ObjectClassDTO director) {
        this.director = director;
    }

    public String getGenretype() {
        return genretype;
    }

    public void setGenretype(String genretype) {
        this.genretype = genretype;
    }

}
