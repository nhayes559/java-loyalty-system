import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by nick on 10/06/2015.
 */
public class LoyaltyUtilTest {

    @Test
    public void testGetBasicPoints() {
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit(LocalDateTime.parse("2015-01-01T20:15:30"), 43));
        visits.add(new Visit(LocalDateTime.parse("2015-02-01T20:15:30"), 27));
        visits.add(new Visit(LocalDateTime.parse("2015-03-01T20:15:30"), 30));
        int points = (int)LoyaltyUtil.getPoints(visits);
        assertEquals(points, 100);
    }

    @Test
    public void testGetThresholdBonusPoints() {
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit(LocalDateTime.parse("2015-01-01T20:15:30"), 110));
        visits.add(new Visit(LocalDateTime.parse("2015-01-02T20:15:30"), 20));
        int points = (int)LoyaltyUtil.getPoints(visits);
        assertEquals(points, 135);
    }

    @Test
    public void testGetFrequentVisitsBonusPoints() {
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit(LocalDateTime.parse("2015-01-01T20:15:30"), 10));
        visits.add(new Visit(LocalDateTime.parse("2015-01-02T20:15:30"), 10));
        visits.add(new Visit(LocalDateTime.parse("2015-01-03T20:15:30"), 10));
        int points = (int)LoyaltyUtil.getPoints(visits);
        assertEquals(points, 35);
    }

    @Test
    public void testGetMultiplierBonusPoints() {
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit(LocalDateTime.parse("2015-06-09T10:15:30"), 10));
        visits.add(new Visit(LocalDateTime.parse("2015-06-10T10:15:30"), 10));
        visits.add(new Visit(LocalDateTime.parse("2015-01-01T20:15:30"), 10));
        int points = (int)LoyaltyUtil.getPoints(visits);
        assertEquals(points, 50);
    }

    @Test
    public void emptyCheck() {
        List<Visit> visits = new ArrayList<>();
        int points = (int)LoyaltyUtil.getPoints(visits);
        assertEquals(points, 0);
    }

    /*
    * Calculate a monthly statement for a visitor, showing visits,
    * date/time, loyalty rules applied, starting and ending loyalty points and value.
     */
    @Test
    public void testStatement() {
        List<Visit> januaryVisits = new ArrayList<>();
        List<Visit> februaryVisits = new ArrayList<>();
        // January visits (to get the starting loyalty points)
        januaryVisits.add(new Visit(LocalDateTime.parse("2015-01-02T10:15:30"), 10));
        januaryVisits.add(new Visit(LocalDateTime.parse("2015-01-04T10:15:30"), 20));
        januaryVisits.add(new Visit(LocalDateTime.parse("2015-01-05T20:15:30"), 30));
        // Basic points
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-01T15:00:00"), 43)); //basic
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-02T20:15:30"), 27)); //basic
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-03T15:00:00"), 130)); //third visit, over 100, off peak
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-04T20:15:30"), 43)); //basic
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-05T20:15:30"), 110)); //over 100
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-06T20:15:30"), 30.50)); //basic
        februaryVisits.add(new Visit(LocalDateTime.parse("2015-02-07T15:00:00"), 30)); //basic

        LoyaltyInfo li = LoyaltyUtil.getLoyaltyInfo(februaryVisits);

        // Visits and rules applied
        List<VisitRulePair> rulesApplied = li.getVisitsAndRules();
        // Starting points
        int januaryPoints = LoyaltyUtil.getPoints(januaryVisits);
        // End points
        int februaryPoints = li.getPoints();
        // Print out our visits and rules
        for(VisitRulePair vrp: rulesApplied){
            System.out.println("-----VISITED-----");
            System.out.print("On: " + vrp.getVisit().getDateAndTime());

            for(String rule: vrp.getRules())
                System.out.print(" --- " + rule);
            System.out.println();
            System.out.println("Spent: £" + vrp.getVisit().getSpend());
        }
        // Print the rest of the information
        System.out.println("-------------------------");
        System.out.println("Starting points: " + januaryPoints);
        System.out.println("Ending points:   " + (januaryPoints + februaryPoints));
        System.out.println("-------------------------");
        System.out.println("Value:           £" + (januaryPoints + februaryPoints) * 0.02);
    }

}