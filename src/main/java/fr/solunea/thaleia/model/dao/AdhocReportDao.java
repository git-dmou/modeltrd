package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.AdhocReport;
import fr.solunea.thaleia.utils.DetailedException;
import org.apache.cayenne.ObjectContext;

import java.io.Serializable;
import java.util.Locale;

@SuppressWarnings("serial")
public class AdhocReportDao extends CayenneDao<AdhocReport> implements Serializable {

    public AdhocReportDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(AdhocReport object, Locale locale) {
        if (object.getName() != null) {
            return object.getName();
        } else {
            return Integer.toString(getPK(object));
        }
    }

    @Override
    public void save(AdhocReport object) throws DetailedException {

        // Plus besoin : la sécurité se fait par les droits du compte d'accès à la base, et en plus on veut pouvoir
        // faire des requêtes sur des objets qui contiennent ces mots-clé, sans qu'ils soient des instructions SQL.
        //        // On nettoie la requête SQL
        //        String sql = object.getContent();
        //
        //        sql = sql.replaceAll("(?i)insert into", "");
        //        sql = sql.replaceAll("(?i)drop", "");
        //        sql = sql.replaceAll("(?i)alter table", "");
        //        sql = sql.replaceAll("(?i)create table", "");
        //        sql = sql.replaceAll("(?i)update", "");
        //
        //        object.setContent(sql);

        super.save(object);
    }

}
