package au.com.shivaji.app;

import au.com.shivaji.app.approach2.FraudDetection;
import au.com.shivaji.app.approach3.CreditCardFraudDetectionAppContext;
import au.com.shivaji.app.approach3.detection.Fraud;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * I have utilised strategy pattern to implement the transaction string parsing and fraud detection.
 *
 * <p>1) I built the transaction source to be able to either receive a CSV file or as the problem
 * stated a collection of strings <br>
 * 2) Credit card fraud implements the algorithm stated to detect frauds un credit card
 *
 * @author Shivaji Byrapaneni
 */
@Slf4j
@Component
class ExampleUsage {

  private final Fraud<String, String> creditCardFraud;

  public ExampleUsage(Fraud<String, String> creditCardFraud) {
    this.creditCardFraud = creditCardFraud;
  }

  public static void main(String[] args) throws Exception {
    // Init application context of Spring
    AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
    initAppContext(appContext);
    // Trigger sample usage
    ExampleUsage exampleUsage = appContext.getBean(ExampleUsage.class);

    var entries = RandomDataGenerator.generate10MillionRecords();
    log.warn("Total of sample entries:: " + entries.size());
    LocalDate fraudDetectionDate = LocalDateTime.parse(RandomDataGenerator.randomDateTime()).toLocalDate();
    double priceThreshold = RandomUtils.nextDouble(0, 20000);
    log.warn("For Date " + fraudDetectionDate + " Transaction priceThreshold " + priceThreshold);

    log.warn("");
    log.warn("<--------------------------------------->");
    long start = System.nanoTime();
    exampleUsage.detectFraudsFromInputTransactionsCollection(entries, exampleUsage.creditCardFraud, priceThreshold, fraudDetectionDate);
    long end = System.nanoTime();
    long ms = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    log.warn("Approach 1 (Spring & Using Model):: Took [{}] ms", ms);

    log.warn("");
    log.warn("<--------------------------------------->");
    start = System.nanoTime();
    au.com.shivaji.app.approach1.FraudDetection.detect(entries, fraudDetectionDate, priceThreshold);
    end = System.nanoTime();
    ms = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    log.warn("Approach 2 (Pure Java with Parsing & then searching):: Took [{}] ms", ms);

    log.warn("");
    log.warn("<--------------------------------------->");
    start = System.nanoTime();
    FraudDetection.detect(entries, fraudDetectionDate, priceThreshold);
    end = System.nanoTime();
    ms = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    log.warn("Approach 3 (Pure Java Searching while parsing):: Took [{}] ms", ms);
  }

  private static void initAppContext(AnnotationConfigApplicationContext appContext) {
    appContext.register(CreditCardFraudDetectionAppContext.class);
    appContext.refresh();
  }

  private void detectFraudsFromInputTransactionsCollection(
      Collection<String> entries, Fraud<String, String> creditCardFraud, Double priceThreshold, LocalDate dateFromCsvSample) {
    Collection<String> fraudsDetectedFromListOfInputs = creditCardFraud.detect(entries, dateFromCsvSample, priceThreshold);
    log.debug("Frauds -- {}", fraudsDetectedFromListOfInputs.size());
  }

}
