package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.ApiToken;
import fr.solunea.thaleia.model.User;
import org.apache.cayenne.ObjectContext;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class ApiTokenDao extends CayenneDao<ApiToken> {

    public ApiTokenDao(ObjectContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(ApiToken object, Locale locale) {
        return object.getValue();
    }

    /**
     * Génère un token pour ce user, qui expire à la fin de la durée demandée.
     *
     * @param user              peut être null : dans ce cas le token n'est pas lié à un user.
     * @param durationInSeconds la durée en secondes avant que ce token n'expire.
     * @return le token.
     */
    public ApiToken generate(User user, int durationInSeconds) {
        ApiToken result = get();

        // La date d'expiration
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, durationInSeconds);
        result.setExpirationDate(now.getTime());

        // Si non nul, le user
        if (user != null) {
            result.setUser(user);
        }

        // On génère la valeur du token
        result.setValue(RandomStringUtils.randomAlphanumeric(32));

        return result;
    }

    /**
     * @param value             la valeur du token.
     * @param durationInSeconds la durée en secondes avant qu'un token n'expire.
     * @return le token en cours de validité existe avec cette valeur, ou null si aucun token valide n'est trouvé.
     */
    public ApiToken getTokenIfValid(String value, int durationInSeconds) {

        if (value == null || value.isEmpty()) {
            return null;
        }

        // Tous les tokens qui contiennent cette valeur
        List<ApiToken> tokens = findMatchByProperty(ApiToken.VALUE.getName(), value);

        // Pas de token pour cette valeur.
        if (tokens.isEmpty()) {
            return null;
        }

        // La date dans le passé au delà de laquelle on considère un token comme invalide
        Calendar notBefore = Calendar.getInstance();
        notBefore.add(Calendar.SECOND, -durationInSeconds);

        for (ApiToken token : tokens) {
            if (token.getExpirationDate().after(notBefore.getTime())) {
                return token;
            }
        }

        return null;
    }

    public List<ApiToken> findByValue(String token) {
        return findMatchByProperty(ApiToken.VALUE.getName(), token);
    }
}
