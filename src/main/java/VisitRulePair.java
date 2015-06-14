import java.util.List;

public class VisitRulePair {

    Visit visit;
    List<String> rules;

    VisitRulePair(Visit visit, List<String> rules){
        this.rules = rules;
        this.visit = visit;
    }

    public List<String> getRules() {
        return rules;
    }
    public void addRule(String element) {
        this.rules.add(element);
    }
    public Visit getVisit() {
        return visit;
    }
}
