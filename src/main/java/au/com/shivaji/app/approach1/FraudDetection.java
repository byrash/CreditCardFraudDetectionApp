package au.com.shivaji.app.approach1;

import static java.util.stream.Collectors.toList;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * Complete parsing of file and searcing on the parsed results on demand basis while holding teh records parsed
 */
@Slf4j
public class FraudDetection {
  private static final Map<LocalDate, Map<String, Double>> transactions = new HashMap<>();

  /*public static void main(String[] args) {
    var entries = RandomDataGenerator.generate10MillionRecords();
    log.debug("Total of sample entries:: " + entries.size());
    LocalDate date = LocalDateTime.parse(RandomDataGenerator.randomDateTime()).toLocalDate();
    double amount = RandomUtils.nextDouble(0, 20000);
    log.debug("For Date " + date + " Transaction amount " + amount);
    detect(entries, date, amount);
  }*/

  public static void detect(Collection<String> entries, LocalDate date, double amount) {
    var start = System.nanoTime();
    entries.forEach(FraudDetection::parse);
    if (transactions.containsKey(date)) {
      Stream<Map.Entry<String, Double>> entryStream;
      Map<String, Double> ccToTotalAmtMap = transactions.get(date);
      if (ccToTotalAmtMap.size() > 10000) { // Many records do in parallel
        entryStream = ccToTotalAmtMap.entrySet().parallelStream();
      } else { //  Not enough records to run in parallel
        entryStream = ccToTotalAmtMap.entrySet().stream();
      }
      var ccs = entryStream.filter(e -> e.getValue() >= amount).map(Map.Entry::getKey).collect(toList());
      log.debug("Frauds " + ccs.size() + " in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milli seconds");
      ccs.parallelStream().forEach(cc -> log.trace(cc + " :: " + ccToTotalAmtMap.get(cc)));
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
}
