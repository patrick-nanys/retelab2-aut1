package hu.bme.aut.retelab2;

import hu.bme.aut.retelab2.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class Application {

    @Autowired
    private AdRepository adRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(fixedDelay = 6000)
    public void clearExpiredAds() {
        adRepository.deleteAdsIfOverExpirationDate(LocalDateTime.now());
    }

}