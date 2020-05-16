package au.com.shivaji.app.approach2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * This approach is memory efficient and process intensive
 *
 * @author Shivaji Byrapaneni
 */
@Slf4j
public class FraudDetection {

  private static final Map<String, Double> creditCardTotals = new ConcurrentHashMap<>();
  private static final List<String> identifiedFrauds = Collections.synchronizedList(new ArrayList<>());

/*  public static void main(String[] args) throws Exception {
    FraudDetectionAnotherApproach fraudDetectionAnotherApproach = new FraudDetectionAnotherApproach();
    URL resource =
        fraudDetectionAnotherApproach.getClass().getClassLoader().getResource("transactions.csv");
    long start = System.nanoTime();
    fraudDetectionAnotherApproach.detectFrauds(Paths.get(resource.toURI()));
    long end = System.nanoTime();
    long ms = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    log.trace("{}", identifiedFrauds);
    log.trace("Took [{}] ms", ms);
  }*/

  public static void detect(Collection<String> entries, LocalDate date, double amount) {
    entries.parallelStream().forEach(line -> parse(line, date, amount));
    log.warn("Frauds {}", identifiedFrauds.size());
  }

/*  public void detectFrauds(Path filePath) throws IOException {
    log.trace("Credit card transaction source file based parsing");
    LocalDate localDate = LocalDate.parse("2014-04-29");
    double priceThreshold = Double.parseDouble("10.0");
    try (Stream<String> lines = Files.lines(filePath, UTF_8)) {
      lines.forEach(line -> parse(line, localDate, priceThreshold));
    }
  }*/

  public static void parse(final String transaction, LocalDate transactionDate, double priceThreshold) {
    log.trace("Trying to Parsing transaction String [{}]", transaction);
    String[] transactionData = transaction.split(",");
    if (transactionData.length != 3) {
      invalidTransaction(transaction);
      return;
    }
    String creditCardNumberHash = transactionData[0].trim();
    if (identifiedFrauds.contains(creditCardNumberHash)) { // Already identified as fraud
      return;
    }
    LocalDateTime transactionDateTime = LocalDateTime.parse(transactionData[1].trim());
    if (transactionDateTime.toLocalDate().equals(transactionDate)) { // Only consider date supplied
      double transactionAmount = Double.parseDouble(transactionData[2].trim());
      if (creditCardTotals.containsKey(creditCardNumberHash)) {
        double total = creditCardTotals.get(creditCardNumberHash) + transactionAmount;
        if (total > priceThreshold) {
          identifiedFrauds.add(creditCardNumberHash);
          creditCardTotals.remove(creditCardNumberHash);
        } else {
          creditCardTotals.put(creditCardNumberHash, total);
        }
      } else if (transactionAmount > priceThreshold) {
        identifiedFrauds.add(creditCardNumberHash);
      } else {
        creditCardTotals.put(creditCardNumberHash, transactionAmount);
      }
    }
    log.trace("Parsing transaction is successful");
  }

  private static void invalidTransaction(String transaction) {
    log.error(
        "Arid transaction record [{}] supplied; Unable to process elements from transaction record",
        transaction);
  }
}
