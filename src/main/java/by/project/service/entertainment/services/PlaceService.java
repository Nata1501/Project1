package by.project.service.entertainment.services;

import by.project.service.entertainment.models.domain.Place;
import by.project.service.entertainment.repositories.PlaceRepository;
import by.project.service.entertainment.util.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;


    public Place findPlaceByName(String name) {
        return placeRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Place not found by name: " + name));
    }

}
