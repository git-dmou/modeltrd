package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.StatementProcessing;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class StatementProcessingDao extends CayenneDao<StatementProcessing> {

    public StatementProcessingDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(StatementProcessing object, Locale locale) {
        return object.getDescription();
    }

    /**
     * @return Les StatementProcessings qui ne sont pas liés à un LrsEndPoint
     */
    public List<StatementProcessing> findByNoLrsEndpoint() {
        return findMatchByProperty(StatementProcessing.SOURCE_LRS.getName(), null);
    }

    /**
     * @return Les StatementProcessings qui sont liés à un LrsEndPoint dont le username est celui demandé.
     */
    public List<StatementProcessing> findByLrsEndpointUsername(String username) {
        List<StatementProcessing> result = new ArrayList<>();
        if (username == null || username.isEmpty()) {
            return result;
        }

        List<StatementProcessing> statementProcessings = find();
        for (StatementProcessing statementProcessing : statementProcessings) {
            if (statementProcessing.getSourceLrs() != null
                    && statementProcessing.getSourceLrs().getUsername().equals(username)) {
                result.add(statementProcessing);
            }
        }

        return statementProcessings;
    }
}
