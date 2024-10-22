package by.project.service.EntertainmentService.services;

import by.project.service.EntertainmentService.models.other.Place;
import by.project.service.EntertainmentService.repositories.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;


    public Place findPlaceByName(String name) {
        Optional<Place> place = placeRepository.findByName(name);
        return place.orElse(null);
    }

}
