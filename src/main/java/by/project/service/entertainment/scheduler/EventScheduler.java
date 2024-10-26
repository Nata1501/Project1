package by.project.service.entertainment.scheduler;

import by.project.service.entertainment.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventService eventService;

    @Scheduled(fixedRate = 30L, timeUnit = TimeUnit.MINUTES)
    public void updateRelevance() {
        eventService.checkAndUpdateRelevance(1L);
    }

}
