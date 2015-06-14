import java.time.LocalDateTime;

public class Visit {
    private LocalDateTime dateTime;
    private double spend;

    public Visit(){}

    public Visit(LocalDateTime dateTime, double spend){
        this.dateTime = dateTime;
        this.spend = spend;
    }

    LocalDateTime getDateAndTime(){
        return dateTime;
    }
    void setDateAndTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

    double getSpend(){
        return spend;
    }
    void setSpend(double spend){
        this.spend = spend;
    }
}

