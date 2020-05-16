package au.com.shivaji.app;

import static java.util.stream.Collectors.toList;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FraudDetection {
  private static final Map<LocalDate, Map<String, Double>> transactions = new HashMap<>();

  public static void main(String[] args) {
    int noOfRecordsToSample = 10000000; // 10 Million records
    List<String> entries = Collections.synchronizedList(new ArrayList<>());
    IntStream.range(0, noOfRecordsToSample).parallel().forEach((i) -> {
      entries.add(StringUtils.joinWith(",", RandomStringUtils.randomAlphanumeric(10), randomDateTime(), RandomUtils.nextDouble(0, 20000)));
    });
    log.debug("Total of sample entries:: " + entries.size());
    var start = System.nanoTime();
    entries.forEach(FraudDetection::parse);
    LocalDate date = LocalDateTime.parse(randomDateTime()).toLocalDate();
    double amount = RandomUtils.nextDouble(0, 20000);
    log.debug("For Date " + date + " Transaction amount " + amount);
    if (transactions.containsKey(date)) {
      Stream<Map.Entry<String, Double>> entryStream;
      Map<String, Double> ccToTotalAmtMap = transactions.get(date);
      if (ccToTotalAmtMap.size() > 10000) { // Many records do in parallel
        entryStream = ccToTotalAmtMap.entrySet().parallelStream();
      } else { //  Not enough records to run in parallel
        entryStream = ccToTotalAmtMap.entrySet().stream();
      }
      var ccs = entryStream.filter(e -> e.getValue() >= amount).map(Map.Entry::getKey).collect(toList());
      log.debug(ccs.size() + " in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milli seconds");
      ccs.parallelStream().forEach(cc -> log.debug(cc + " :: " + ccToTotalAmtMap.get(cc)));
    } else {
      log.debug("No Transactions in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milli seconds");
    }
  }

  private static void parse(String line) {
    String[] split = line.split(",");
    if (split.length != 3) {
      return;
    }
    String creditCardNumberHash = split[0].trim();
    LocalDateTime transactionDateTime = LocalDateTime.parse(split[1].trim());
    double transactionAmount = Double.parseDouble(split[2].trim());
    if (!transactions.containsKey(transactionDateTime.toLocalDate())) {
      Map<String, Double> entries = new HashMap<>();
      entries.put(creditCardNumberHash, (double) 0);
      transactions.put(transactionDateTime.toLocalDate(), entries);
    }
    Map<String, Double> entries = transactions.get(transactionDateTime.toLocalDate());
    if (!entries.containsKey(creditCardNumberHash)) {
      entries.put(creditCardNumberHash, (double) 0);
    }
    entries.put(creditCardNumberHash, entries.get(creditCardNumberHash) + transactionAmount);
  }

  public static String randomDateTime() {
    LocalDateTime tmp = LocalDateTime.of(LocalDate.of(randBetween(1985, 2020), randBetween(1, 12), randBetween(1, 28)), LocalTime.now());
    return tmp.toString();
  }

  public static int randBetween(int start, int end) {
    return start + (int) Math.round(Math.random() * (end - start));
  }

}
