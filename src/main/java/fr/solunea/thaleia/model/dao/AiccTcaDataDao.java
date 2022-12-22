package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.AiccTcaData;
import org.apache.cayenne.ObjectContext;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class AiccTcaDataDao extends CayenneDao<AiccTcaData> {

    public AiccTcaDataDao(ObjectContext context) {
        super(context);
    }

    public List<AiccTcaData> findByStatementRef(String statementRef) {
        return findMatchByProperty(AiccTcaData.STATEMENT_REF.getName(), statementRef);
    }

    @Override
    public String getDisplayName(AiccTcaData object, Locale locale) {
        return object.getStatementRef();
    }

}
