package fr.solunea.thaleia.model;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import fr.solunea.thaleia.model.auto._StatementProcessing;
import fr.solunea.thaleia.utils.DetailedException;
import net.minidev.json.JSONArray;

import javax.net.ssl.HttpsURLConnection;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

public class StatementProcessing extends _StatementProcessing implements Serializable {

    private Object patternJsonDocument;
    private ArrayList<Object> modelRules;
    private Object modelConsolidation;
    private String modelDestination;

    public Object getPatternJsonDocument() throws DetailedException {
        if (patternJsonDocument == null) {
            try {
                patternJsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(getFilter());
            } catch (Exception e) {
                throw new DetailedException(e).addMessage("Impossible de parser le filtre : " + getFilter());
            }
        }
        return patternJsonDocument;
    }


    public Object getModelConsolidation() throws DetailedException {
        if (modelConsolidation == null) {
            try {
                Object modelJsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(getProcessing());
                modelRules = JsonPath.read(modelJsonDocument, "$.JSONVARS");
                modelConsolidation = JsonPath.read(modelJsonDocument, "$.JSONOUTPUT");
                modelDestination = JsonPath.read(modelJsonDocument, "$.DESTINATION");
            } catch (Exception e) {
                throw new DetailedException(e).addMessage("Impossible de parser le processus : " + getFilter());
            }
        }
        return modelConsolidation;
    }

    public boolean matchPattern(String statement) throws DetailedException {
        try {
            boolean match = false;
            Object jsonStatement = Configuration.defaultConfiguration().jsonProvider().parse(statement);
            ArrayList<String> rules = JsonPath.read(getPatternJsonDocument(), "$.rules");
            for (String rule : rules) {
                JSONArray value = JsonPath.read(jsonStatement, rule);
                if (!value.isEmpty()) {
                    match = true;
                }
            }
            return match;
        } catch (Exception e) {
            throw new DetailedException(e).addMessage("Impossible de comparer au pattern le statement : " + statement);
        }
    }

    public String consolidate(String statement) throws DetailedException {
        try {
            Gson gsonInstance = new Gson();
            String consolidatedModel = gsonInstance.toJson(getModelConsolidation(), LinkedHashMap.class);
            Object jsonStatement = Configuration.defaultConfiguration().jsonProvider().parse(statement);
            for (Object rule : modelRules) {
                String ruleSrc = JsonPath.read(rule, "$.src");
                if (ruleSrc.equals("decla")) {
                    String ruleSearch = JsonPath.read(rule, "$.search");
                    String ruleName = JsonPath.read(rule, "$.name");
                    Object value = JsonPath.read(jsonStatement, ruleSearch);
                    if (value instanceof String) {
                        String finalValue = value.toString().replace("\n", "");
                        consolidatedModel = consolidatedModel.replace(ruleName, finalValue);
                    } else {
                        String finalValue = gsonInstance.toJson(value).replaceAll("\n", "");
                        consolidatedModel = consolidatedModel.replace('"' + ruleName + '"', finalValue);
                    }
                }
            }
            return consolidatedModel;

        } catch (Exception e) {
            throw new DetailedException(e).addMessage("Impossible de consolider le Statement : " + statement);
        }
    }

    public void forwardToDestination(String consolidatedStatementBody, String rawStatementBody) throws DetailedException {
        try {
            String url = getDestinationUrl();
            if (modelDestination != null && !modelDestination.equals("")){
                String destination = modelDestination;
                Object jsonStatement = Configuration.defaultConfiguration().jsonProvider().parse(rawStatementBody);
                for (Object rule : modelRules) {
                    String ruleSrc = JsonPath.read(rule, "$.src");
                    if (ruleSrc.equals("decla")) {
                        String ruleSearch = JsonPath.read(rule, "$.search");
                        String ruleName = JsonPath.read(rule, "$.name");
                        Object value = JsonPath.read(jsonStatement, ruleSearch);
                        if (value instanceof String) {
                            String finalValue = value.toString().replace("\n", "");
                            destination = destination.replace(ruleName, finalValue);
                        }
                    }
                }
                url += destination;
            } else {
                url += "statements";
            }
            String userCredentials = getDestinationLogin() + ":" + getDestinationPassword();
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("OPTIONS");
            con.addRequestProperty("Content-Type", "application/json");
            con.addRequestProperty("Access-Control-Request-Headers",
                    "authorization,content-type," + "x-experience-api-version");
            con.addRequestProperty("Access-Control-Request-Method", "POST");

            URL obj2 = new URL(url);
            HttpsURLConnection con2 = (HttpsURLConnection) obj2.openConnection();
            con2.setDoOutput(true);
            con2.setDoInput(true);
            con2.setRequestMethod("POST");
            con2.setRequestProperty("Authorization", basicAuth);
            con2.addRequestProperty("Content-Type", "application/json");
            con2.addRequestProperty("X-Experience-API-Version", "1.0.1");
            con2.setRequestProperty("Content-Length", Integer.toString(consolidatedStatementBody.length()));
            con2.getOutputStream().write(consolidatedStatementBody.getBytes(StandardCharsets.UTF_8));

            System.out.println(con2.getResponseCode());

        } catch (Exception e) {
            throw new DetailedException(e).addMessage("Impossible de transmettre le Statement consolidé.");
        }
    }
}