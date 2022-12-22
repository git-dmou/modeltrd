package fr.solunea.thaleia.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class PublicationAnswersStats implements Serializable {

    private String publicationId;
    private String publicationName;
    private String publicationDescription;
    private Integer correct;
    private Integer incorrect;
    private Integer averageTimeSpend;
    private String activityType;
    private ArrayList<String> learnerResponses;
    private LinkedHashMap<String, Integer> sortedCountedLearnerResponses;
    private String[][] mostRecurrentAnswersWithPercent;


    public PublicationAnswersStats(String publicationId, String publicationName, String publicationDescription, String activityType, Integer correct,
                                   Integer incorrect, Integer averageTimeSpend, String learnerResponses ) {
        this.publicationId = publicationId;
        this.publicationName = publicationName;
        this.publicationDescription = publicationDescription;
        this.correct = correct;
        this.incorrect = incorrect;
        this.averageTimeSpend = averageTimeSpend;
        this.activityType = activityType;
        this.setLearnerResponses(learnerResponses);
        this.sortAnswersFrequencies();
        this.setMostRecurrentAnswersPercent();
    }

    public String getPublicationId() {
        return publicationId;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public String getPublicationDescription() {
        return publicationDescription;
    }

    public Integer getCorrect() {
        return correct;
    }

    public Integer getIncorrect() {
        return incorrect;
    }

    public double getScore() {
        if( (getCorrect() + getIncorrect()) > 0) {
            return getCorrect() * 100 / ( getCorrect() + getIncorrect() );
        } else {
            return 0;
        }

    }

    public Integer getAverageTimeSpend() {
        return averageTimeSpend;
    }

    public String getActivityType() {
        return activityType;
    }

    /**
     * @brief Assigne la liste des réponses des utilisateurs.
     * @param learnerResponses
     */
    private void setLearnerResponses(String learnerResponses) {
        String[] items = learnerResponses.split("_;_");
        for( int i = 0; i <= items.length - 1; i++) {
            // Les réponses "vides" sont transformées en "-" afin de garder un élément affiché pour la mise en page
            // (les éléments vides sont effacés par le JS afin de "nettoyer" la mise en page)
            if(items[i].trim().length() == 0) {
                items[i] = "-";
            } else {
                items[i] = items[i].trim();
            }
        }

        this.learnerResponses = new ArrayList<String>(Arrays.asList(items));
    }

    private void sortAnswersFrequencies() {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : this.learnerResponses) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        Map<String, Integer> unSortedMap = new HashMap<>();

        // displaying the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            unSortedMap.put(val.getKey(), val.getValue());
        }

        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        this.sortedCountedLearnerResponses = reverseSortedMap;
    }


    public void setMostRecurrentAnswersPercent() {
        Set<String> keys = this.sortedCountedLearnerResponses.keySet();
        String[] keysArray = keys.toArray(new String[keys.size()]);
        Integer nbAnswers = 0;
        for(int i=0; i<keysArray.length && i<10;i++) {
            nbAnswers +=  this.sortedCountedLearnerResponses.get(keysArray[i]);
        }

        String topAnswers[][] = new String[keysArray.length][];
        for(int i=0; i<keysArray.length && i<10;i++) {
            double percent =  this.sortedCountedLearnerResponses.get(keysArray[i]) * 100 / nbAnswers;
            topAnswers[i] = new String[]{ keysArray[i], Double.toString(percent) };
        }

        this.mostRecurrentAnswersWithPercent = topAnswers;
    }


    public String[][] getMostRecurrentAnswersWithPercent() {
        return this.mostRecurrentAnswersWithPercent;
    }

}
