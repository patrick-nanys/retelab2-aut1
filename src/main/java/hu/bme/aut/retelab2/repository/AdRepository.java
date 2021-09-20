package hu.bme.aut.retelab2.repository;

import hu.bme.aut.retelab2.domain.Ad;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AdRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Ad save(Ad ad) {
        return em.merge(ad);
    }

    public List<Ad> searchByMinMax(int min, int max) {
        return em.createQuery("SELECT a FROM Ad a WHERE a.price >= ?1 AND a.price <= ?2", Ad.class).setParameter(1, min).setParameter(2, max).getResultList();
    }

    @Transactional
    public Ad edit(Ad ad) {
        List<Ad> ads = em.createQuery("SELECT a FROM Ad a WHERE a.id = ?1", Ad.class).setParameter(1, ad.getId()).getResultList();

        if (ads.size() == 0) {
            throw new RuntimeException("There is no ad with such an id!");
        }

        Ad res_ad = ads.get(0);
        if (!res_ad.getSecret().equals(ad.getSecret())) {
            throw new RuntimeException("Secrets do not match!");
        }
        em.merge(ad);
        return ad;
    }

    public List<Ad> getAdsByTag(String tag) {
        return em.createQuery("SELECT a FROM Ad a JOIN a.tags t WHERE t = LOWER(?1)", Ad.class).setParameter(1, tag).getResultList();
    }

    @Transactional
    @Modifying
    public void deleteAdsIfOverExpirationDate(LocalDateTime date) {
        em.createQuery("DELETE FROM Ad a WHERE a.expirationDate < ?1").setParameter(1, date).executeUpdate();
    }
}
