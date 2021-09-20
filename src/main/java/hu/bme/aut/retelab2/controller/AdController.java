package hu.bme.aut.retelab2.controller;

import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.repository.AdRepository;
import hu.bme.aut.retelab2.utils.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private AdRepository adRepository;

    @PostMapping
    public Ad create(@RequestBody Ad ad) {
        ad.setId(null);
        ad.setCreationDate(new Date());
        ad.setSecret(SecretGenerator.generate());
        return adRepository.save(ad);
    }

    @GetMapping
    public List<Ad> searchByMinMax(@RequestParam(required = false, defaultValue = "0") String min, @RequestParam(required = false, defaultValue = "10000000") String max) {
        List<Ad> ads = adRepository.searchByMinMax(Integer.parseInt(min), Integer.parseInt(max));
        ads.forEach(ad -> ad.setSecret(null));
        return ads;
    }

    @PutMapping
    public ResponseEntity<Ad> edit(@RequestBody Ad ad) {
        System.out.println("Called edit in controller!");
        Ad edited_ad;
        try {
            edited_ad = adRepository.edit(ad);
            System.out.println("After edit in controller!");
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        System.out.println("Edited in controller!");
        return ResponseEntity.ok(edited_ad);
    }

    @GetMapping("{tag}")
    public List<Ad> getAdsByTag(@PathVariable String tag) {
        List<Ad> ads = adRepository.getAdsByTag(tag);
        ads.forEach(ad -> ad.setSecret(null));
        return ads;
    }
}
