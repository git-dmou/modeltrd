package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._LicenceHolding;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>LicenceHolding class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class LicenceHolding extends _LicenceHolding implements Serializable {

    protected static final Logger logger = Logger.getLogger(LicenceHolding.class);

    public LicenceHolding() {
        //logger.debug("Classe appelante : " + LogUtils.getCallerInfo(3));
    }

    @Override
    public Integer getIncludedPublicationsCredits() {
        Integer value = super.getIncludedPublicationsCredits();
        if (value == null) {
            return getLicence().getIncludedPublicationCredits();
        } else {
            return value;
        }
    }

    @Override
    public Boolean getIsDemo() {
        Boolean value = super.getIsDemo();
        if (value == null) {
            return getLicence().getIsDemo();
        } else {
            return value;
        }
    }

    @Override
    public Integer getMaxPublications() {
        Integer value = super.getMaxPublications();
        if (value == null) {
            return getLicence().getMaxPublications();
        } else {
            return value;
        }
    }

    @Override
    public Integer getMaxSizeMo() {
        Integer value = super.getMaxSizeMo();
        if (value == null) {
            return getLicence().getMaxSizeMo();
        } else {
            return value;
        }
    }

    /**
     * Si on est le 2 et l'expiration est le 7, alors il reste 5 jours.
     */
    public long getExpirationInDays() {
        // Si problème, utiliser le code de LicenceService.getDaysBeforeLicenceExpiration
        Date end = getEndDate();
        Date now = Calendar.getInstance().getTime();
        return ChronoUnit.DAYS.between(now.toInstant(), end.toInstant()) + 1;
    }
}
