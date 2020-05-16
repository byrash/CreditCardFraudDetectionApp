package au.com.shivaji.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

public class RandomDataGenerator {

  public static List<String> fixedRecords() {
    List<String> entries = new ArrayList<>() {{
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 2,2014-04-29T13:15:54,20.00");
      add("CC 3,2014-04-29T13:15:54,30.00");
    }};
    return Collections.unmodifiableList(entries);
  }

  public static List<String> generate10MillionRecords() {
    int noOfRecordsToSample = 10000000; // 10 Million records
    List<String> entries = Collections.synchronizedList(new ArrayList<>());
    IntStream.range(0, noOfRecordsToSample).parallel().forEach((i) -> {
      entries.add(StringUtils.joinWith(",", RandomStringUtils.randomAlphanumeric(10), randomDateTime(), RandomUtils.nextDouble(0, 20000)));
    });
    return Collections.unmodifiableList(entries);
  }

  public static String randomDateTime() {
    LocalDateTime tmp = LocalDateTime.of(LocalDate.of(randBetween(1985, 2020), randBetween(1, 12), randBetween(1, 28)), LocalTime.now());
    return tmp.toString();
  }

  private static int randBetween(int start, int end) {
    return start + (int) Math.round(Math.random() * (end - start));
  }
}
