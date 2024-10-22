package by.project.service.EntertainmentService.dto;

import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.models.other.ValueOfEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class EventDTO {
    @NotNull
    @ValueOfEnum(enumClass = Type.class)
    private String type;

    @NotNull
    private EntertainmentDTO enterteinment;

    @NotNull
    private ObjectClassDTO place;

    @NotNull
    private Date dateTime;

    @NotNull
    private Boolean relevance;





    public EntertainmentDTO getEnterteinment() {
        return enterteinment;
    }

    public void setEnterteinment(EntertainmentDTO enterteinment) {
        this.enterteinment = enterteinment;
    }

    public ObjectClassDTO getPlace() {
        return place;
    }

    public void setPlace(ObjectClassDTO place) {
        this.place = place;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getRelevance() {
        return relevance;
    }

    public void setRelevance(Boolean relevance) {
        this.relevance = relevance;
    }

}
