package au.com.touch.app;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * This approach is memory efficient and process intensive
 *
 * @author Shivaji Byrapaneni */
@Slf4j
public class FraudDetectionAnotherApproach {

  private final Map<String, BigDecimal> creditCardTotals = new HashMap<>();
  private final Collection<String> identifiedFrauds = new CopyOnWriteArrayList<>();

  public static void main(String[] args) throws Exception {
    FraudDetectionAnotherApproach fraudDetectionAnotherApproach = new FraudDetectionAnotherApproach();
    URL resource =
        fraudDetectionAnotherApproach.getClass().getClassLoader().getResource("transactions.csv");
    long start = System.nanoTime();
    fraudDetectionAnotherApproach.detectFrauds(Paths.get(resource.toURI()));
    long end = System.nanoTime();
    long ms = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    log.debug("{}", fraudDetectionAnotherApproach.identifiedFrauds);
    log.debug("Took [{}] ms", ms);
  }

  public void detectFrauds(Path filePath) throws IOException {
    log.debug("Credit card transaction source file based parsing");
    LocalDate localDate = LocalDate.parse("2014-04-29");
    BigDecimal priceThreshold = BigDecimal.valueOf(10.0);
    try (Stream<String> lines = Files.lines(filePath, UTF_8)) {
      lines.forEach(line -> parse(line, localDate, priceThreshold));
    }
  }

  public void parse(
      final String transaction, LocalDate transactionDate, BigDecimal priceThreshold) {
    log.debug("Trying to Parsing transaction String [{}]", transaction);
    String[] transactionData = transaction.split(",");
    if (transactionData.length != 3) {
      invalidTransaction(transaction);
      return;
    }
    try {
      String creditCardNumberHash = transactionData[0].trim();
      if (identifiedFrauds.contains(creditCardNumberHash)) { // Already identified as fraud
        return;
      }
      try {
        LocalDateTime transactionDateTime = LocalDateTime.parse(transactionData[1].trim());
        if (transactionDateTime
            .toLocalDate()
            .equals(transactionDate)) { // Only consider date supplied
          double transactionAmount = Double.parseDouble(transactionData[2].trim());
          if (creditCardTotals.containsKey(creditCardNumberHash)) {
            BigDecimal bigDecimal = creditCardTotals.get(creditCardNumberHash);
            BigDecimal total = bigDecimal.add(BigDecimal.valueOf(transactionAmount));
            if (total.compareTo(priceThreshold) > 0) {
              identifiedFrauds.add(creditCardNumberHash);
              creditCardTotals.remove(creditCardNumberHash);
            } else {
              creditCardTotals.put(creditCardNumberHash, total);
            }
          } else {
            creditCardTotals.put(creditCardNumberHash, BigDecimal.valueOf(transactionAmount));
          }
        }
      } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
        invalidTransaction(transaction);
        return;
      }
      log.debug("Parsing transaction is successful");
    } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
      invalidTransaction(transaction);
    }
  }

  private void invalidTransaction(String transaction) {
    log.error(
        "Arid transaction record [{}] supplied; Unable to process elements from transaction record",
        transaction);
  }
}
