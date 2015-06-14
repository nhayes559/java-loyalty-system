import java.util.List;

public class LoyaltyInfo {

    private int points;
    private List<VisitRulePair> visitsAndRules;

    public LoyaltyInfo(){}

    public LoyaltyInfo(int points, int startPoints, int endPoints, List<VisitRulePair> visitsAndRules, double value){
        this.points = points;
        this.visitsAndRules = visitsAndRules;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public int getPoints(){
        return points;
    }

    public List<VisitRulePair> getVisitsAndRules() {
        return visitsAndRules;
    }
    public void setVisitsAndRules(List<VisitRulePair> visitsAndRules) {
        this.visitsAndRules = visitsAndRules;
    }

}