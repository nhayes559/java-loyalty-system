import java.time.DayOfWeek;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class LoyaltyUtil {

    private LoyaltyUtil(){}

    // Get a list of the 3rd visit of each month
    private static List<Visit> getFrequentVisits(List<Visit> visits) {

        Collection<List<Visit>> visitsByMonth;
        List<Visit> nthVisits;

        // Group the visits by their months
        visitsByMonth = visits.stream().collect(
                Collectors.groupingBy(
                        visit -> visit.getDateAndTime().getMonth())).values();

        // Filter each month by its third visit, and flatten to a single list
        nthVisits = visitsByMonth.stream().flatMap(
                visitMonth -> visitMonth.stream().filter(
                        visit -> (visitMonth.indexOf(visit) + 1) == 3))
                .collect(Collectors.toList());

        return nthVisits;
    }

    // Off peak test
    private static boolean offPeak(Visit visit){
        return   ((visit.getDateAndTime().getDayOfWeek() == DayOfWeek.TUESDAY
                || visit.getDateAndTime().getDayOfWeek() == DayOfWeek.WEDNESDAY)
                && visit.getDateAndTime().getHour() > 0
                && visit.getDateAndTime().getHour() < 18);
    }

    // Get the points for a given list of visits
    public static int getPoints(List<Visit> visits){
        return (int)getLoyaltyInfo(visits).getPoints();
    }

    // Get points and additional loyalty information e.g. deals applied
    public static LoyaltyInfo getLoyaltyInfo(List<Visit> visits){
        assert(visits != null);
        List<Visit> frequentVisits = getFrequentVisits(visits);
        LoyaltyInfo info = new LoyaltyInfo();
        List<VisitRulePair> visitsAndRules = new ArrayList<VisitRulePair>();

        // Map each visit to its point value and sum the result
        int points = visits.stream().mapToInt(visit -> {
            // Default point values with no bonus
            int basicPoints = round((float) floor(visit.getSpend()));
            int thresholdPoints = 0;
            int frequentVisitPoints = 0;
            int offPeakMultiplier = 1;

            // Capture each the visit and its rules in a pair
            List<String> rules = new ArrayList<>();
            VisitRulePair vrp = new VisitRulePair(visit, rules);
            visitsAndRules.add(vrp);

            // Apply bonus points: more rules can be added here
            if (visit.getSpend() > 100) {
                thresholdPoints = 5;
                vrp.addRule("Over £100 pounds spent!");
            }
            if (frequentVisits.contains(visit)) {
                frequentVisitPoints = 5;
                vrp.addRule("Your third visit this month!");
            }
            if (offPeak(visit)) {
                offPeakMultiplier = 2;
                vrp.addRule("Off peak visit!");
            }

            // Return the points for this visit
            return offPeakMultiplier * (basicPoints + thresholdPoints + frequentVisitPoints);
        }).sum();

        info.setPoints(points);
        info.setVisitsAndRules(visitsAndRules);

        return info;
    }

}
