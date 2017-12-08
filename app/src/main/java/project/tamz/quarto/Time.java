package project.tamz.quarto;

/**
 * Created by Richard Zvonek on 08/12/2017.
 */

public class Time {
    private long h, m, s;
    private long secondsInMilli = 1000;
    private long minutesInMilli = secondsInMilli * 60;
    private long hoursInMilli = minutesInMilli * 60;

    Time(long millis) {
        set(millis);
    }

    Time() {
        this(0);
    }

    public long getH() {
        return h;
    }

    public long getM() {
        return m;
    }

    public long getS() {
        return s;
    }

    void set(long millis) {

        h = millis / hoursInMilli;
        millis %= hoursInMilli;

        m = millis / minutesInMilli;
        millis %= minutesInMilli;

        s = millis / secondsInMilli;
    }

    @Override
    public String toString() {

        if (h == 0) {
            if (m == 0) {
                return s + "s";
            }
            return m + "m " + s + "s";
        }
        return h + "h " + m + "m " + s + "s";
    }
}
