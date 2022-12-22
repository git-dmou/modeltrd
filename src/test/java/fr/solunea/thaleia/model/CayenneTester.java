package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.dao.ICayenneContextService;
import hthurow.tomcatjndi.TomcatJNDI;
import org.apache.cayenne.BaseContext;
import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.log4j.Logger;

import java.io.File;

public class CayenneTester {

    private static final Logger logger = Logger.getLogger(CayenneTester.class);

    protected static TomcatJNDI tomcatJNDI;

    public static void initCayenneContext() throws Exception {
        // Les paramètres nécessaires aux test sont récupérés dans les propriétés du système. C'est pour cela que les tests fonctionnent
        // avec mvn test (car ces propriétés sont générées depuis le profil Maven), mais pas si on les lance directement depuis Intellij
        // par CTRL-ALT-F10 (à moins de les fixer en dur à la main dans la conf des tests, ou alors d'exécuter avec CTRL-ENTER
        // mvn -Dtest=NomDeLaClasseDeTest#nomDelLaMethode test
        // ).
        logger.info("Récupération des paramètres pour le test...");
        String contextXml = System.getProperty("tests.context.xml.location");
        if (contextXml == null) {
             throw new Exception("Vérifiez la configuration des propriétés système du plugin SureFire dans le POM du Thaleia-Parent");
        }
        File contextXmlFile = new File(contextXml);
        if (!contextXmlFile.exists()) {
            throw new Exception("Vérifiez que maven install a été executé sur Thaleia-Parent, et la présence du fichier de contexte de l'application : " + contextXml);
        }
        String webXml = System.getProperty("tests.web.xml.location");
        File webXmlFile = new File(webXml);
        if (!webXmlFile.exists()) {
            throw new Exception("Vérifiez que maven install a été executé sur thaleia-war, et la présence du fichier de déploiement de l'application : " + webXml);
        }
        logger.info("Ok.");

        logger.info("Préparation des ressources JNDI pour les tests...");
        tomcatJNDI = new TomcatJNDI();
        tomcatJNDI.processContextXml(contextXmlFile);
        tomcatJNDI.processWebXml(webXmlFile);
        tomcatJNDI.start();
        logger.info("Ok.");

        logger.info("Association du contexte Cayenne au thread, pour un appel par la ThaleiaSession (Wicket)...");
        // Le nom du fichier de conf du contexte Cayenne est défini dans le web.xml
        ServerRuntime runtime = ServerRuntime.builder()
                .addConfig("cayenne-ThaleiaDomain.xml")
                .build();
        BaseContext.bindThreadObjectContext(runtime.newContext());
        logger.info("Ok.");
    }

    public static void closeCayenneContext() {
        if (tomcatJNDI != null) {
            tomcatJNDI.tearDown();
        }
    }

    static class CayenneContextService implements ICayenneContextService {

        private static final Logger logger = Logger.getLogger(CayenneContextService.class);
        private final ServerRuntime cayenneRuntime;
        private ObjectContext context;

        public CayenneContextService(String configurationLocation) {
            logger.debug("Initialisation du contexte Cayenne décrit dans la ressource " + configurationLocation);
            cayenneRuntime = ServerRuntime.builder().addConfig(configurationLocation).build();
        }

        @Override
        public ObjectContext getContextSingleton() {
            synchronized (this) {
                if (this.context == null) {
                    ObjectContext context = cayenneRuntime.newContext();
                    if (context == null) {
                        logger.error("Contexte Cayenne non initialisé !");
                    } else {
                        this.context = context;
                    }
                }
                return this.context;
            }
        }

        /**
         * @return un nouveau contexte Cayenne, vierge.
         */
        @Override
        public ObjectContext getNewContext() {
            return cayenneRuntime.newContext();
        }

        @Override
        public void close() {
            cayenneRuntime.shutdown();
        }

        @Override
        public ObjectContext getChildContext(ObjectContext parentContext) {
            return cayenneRuntime.newContext(parentContext);
        }

        @Override
        public <T extends BaseDataObject> T getNewInNewContext(Class<T> clazz) {
            return null;
        }

        @Override
        public <T extends BaseDataObject> T getObjectInNewContext(T source) {
            return null;
        }

        @Override
        public <T extends BaseDataObject> void safeDelete(T object) {

        }
    }
}
