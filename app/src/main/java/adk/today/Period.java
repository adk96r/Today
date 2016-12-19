package adk.today;

/**
 * Created by Adu on 6/23/2016.
 */
public class Period implements Comparable<Period> {

    String Subject;
    String SubTime;
    String SubStatus;
    String SubProf;
    String Room;


    public Period(String s_name, String s_time, String s_status, String s_subProf, String s_room) {
        this.setSubject(s_name);
        this.setSubTime(s_time);
        this.setSubStatus(s_status);
        this.setSubProf(s_subProf);
        this.setRoom(s_room);
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSubTime() {
        return SubTime;
    }

    public void setSubTime(String subTime) {
        SubTime = subTime;
    }

    public String getSubStatus() {
        return SubStatus;
    }

    public void setSubStatus(String subStatus) {
        SubStatus = subStatus;
    }

    public String getSubProf() {
        return SubProf;
    }

    public void setSubProf(String subProf) {
        SubProf = subProf;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    @Override
    public int compareTo(Period rhs) {

        /*
        *   This is to sort the various periods according
        *   to the time they start. So the card of the
        *   period starting at 10:00 will be
        *   placed before 10:50 .
        *
        *   RHS is one period and this object is a period.
        *
        *   The time is denoted as ::::
        *   Hour:Minute - Hour:Minute
        *   Thus we are splitting it down into parts.
        *
        *   <7 is used to calculate the 24Hr equivalent of
        *   the 12 Hour clock.
        */

        String t1 = this.SubTime.split(" - ")[0];
        int t1h = new Integer(t1.split(":")[0]);
        int t1r = new Integer(t1.split(":")[1]);

        if (t1h < 7)
            t1h = 12 + t1h;

        String t2 = rhs.SubTime.split(" - ")[0];
        int t2h = new Integer(t2.split(":")[0]);
        int t2r = new Integer(t2.split(":")[1]);

        if (t2h < 7)
            t2h = 12 + t2h;
        Double a = t1h + t1r * 0.01;
        Double b = t2h + t2r * 0.01;


        if (a < b) return -1;
        if (a > b) return 1;
        return 0;

    }
}

