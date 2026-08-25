import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestDate {
    public static void main(String[] args) {
        System.out.println(calc("12:00PM"));
        System.out.println(calc("02:30PM"));
        System.out.println(calc("10:00 AM"));
    }
    public static String calc(String startTime) {
        try {
            String normalizedTime = startTime.toUpperCase(Locale.US).replaceAll("([AP]M)", " $1").replaceAll("\\s+", " ").trim();
            if (normalizedTime.startsWith("0")) {
                normalizedTime = normalizedTime.substring(1);
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            LocalTime start = LocalTime.parse(normalizedTime, dtf);
            LocalTime end = start.plusMinutes(115);
            return end.format(dtf);
        } catch (Exception e) {
            return "???";
        }
    }
}
