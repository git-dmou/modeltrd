package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.dao.*;
import fr.solunea.thaleia.utils.DetailedException;
import org.apache.cayenne.ObjectContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LicenceHoldingTest extends CayenneTester {

    private static final Logger logger = Logger.getLogger(LicenceHoldingTest.class);

    protected static ICayenneContextService contextService;

    @BeforeAll
    public static void setUp() throws Exception {
        try {
            logger.getParent().setLevel(Level.ALL);
            initCayenneContext();
            contextService = new CayenneContextService("cayenne-ThaleiaDomain.xml");
            logger.debug("Contexte : " + contextService);
        } catch (Exception e) {
            logger.warn(e);
            logger.warn(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @AfterAll
    public static void tearDown() {
        logger.info("Nettoyage des objets de tests...");
        closeCayenneContext();
        logger.info("Ok.");
    }

    /**
     * On vérifie qu'une LicenceHolding d'un compte supprimé n'est pas supprimée, mais attribuée à un utilisateur
     * null, afin de conserver le hash de son email.
     */
    @Test
    public void deletedLicenceHoldingsAreKeepedAnonymously() throws DetailedException {
        ObjectContext context = contextService.getContextSingleton();
        UserDao userDao = new UserDao(context);
        LicenceHoldingDao licenceHoldingDao = new LicenceHoldingDao(context);

        String newUserEmail = UUID.randomUUID().toString() + "@solunea.fr";
        LicenceHolding licenceHolding = null;
        User user = null;
        try {
            // Création de l'utilisateur au plus simple
            user = userDao.get();
            user.setLogin(newUserEmail);
            user.setName("Test");
            Domain domain = new DomainDao(context).find().get(0);
            user.setDomain(domain);
            context.commitChanges();

            // Affectation d'une licence
            Licence licence = new LicenceDao(context).findByName("v6.demo.thaleiaxl");
            assertNotNull(licence);
            licenceHolding = licenceHoldingDao.attributeLicence(user, licence.getSku(), this.getClass().getName());
            context.commitChanges();

            // Suppression de l'utilisateur
            userDao.delete(user);
            context.commitChanges();

            // Le licence holding doit persister
            List<LicenceHolding> licenceHoldings = licenceHoldingDao.getHoldedByHashedEmail(newUserEmail, true);
            assertEquals(licenceHoldings.size(), 1);
            licenceHolding = licenceHoldings.get(0);
            // Sur un user null
            assertNull(licenceHolding.getUser());
            // Le licence holding a toujours le hash de l'email de l'utilisateur
            assertEquals(licenceHolding.getUserHashedEmail(), LicenceHoldingDao.hashEmail(newUserEmail));

        } finally {
            // Nettoyage des objets
            if (licenceHolding != null) {
                licenceHoldingDao.delete(licenceHolding);
            }
            if (user != null) {
                userDao.delete(user);
            }
            context.commitChanges();
        }

    }
}
