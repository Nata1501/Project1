package by.project.service.EntertainmentService.dto;

import jakarta.validation.constraints.NotNull;

public class ObjectClassDTO {
    @NotNull
    private String name;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
