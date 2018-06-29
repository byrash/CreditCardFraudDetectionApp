package au.com.touch.app;

import au.com.touch.app.fraud.detection.Fraud;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
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
class ExampleUsage extends MockData {

  private final Fraud<String, String> creditCardFraud;

  public ExampleUsage(Fraud<String, String> creditCardFraud) {
    this.creditCardFraud = creditCardFraud;
  }

  public static void main(String[] args) throws Exception {
    // Init application context of Spring
    AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
    initAppContext(appContext);
    // Trigger sample usage
    appContext.getBean(ExampleUsage.class).runCreditCardFraudDetectionSample();
  }

  private static void initAppContext(AnnotationConfigApplicationContext appContext) {
    appContext.register(CreditCardFraudDetectionAppContext.class);
    appContext.refresh();
  }

  private void runCreditCardFraudDetectionSample() throws URISyntaxException {
    // Params for detecting frauds
    Double priceThreshold = 10.0;
    LocalDate dateFromCsvSample = LocalDate.parse("2014-04-29");

    // Parsing from csv file
    detectFraudsFromInputTransactionsCsvFile(creditCardFraud, priceThreshold, dateFromCsvSample);
    // Parsing from input string list
    detectFraudsFromInputTransactionsCollection(creditCardFraud, priceThreshold, dateFromCsvSample);
  }

  private void detectFraudsFromInputTransactionsCollection(
      Fraud<String, String> creditCardFraud, Double priceThreshold, LocalDate dateFromCsvSample) {

    // Mock transactions setup
    Collection<String> transactions = mockTransactionsCollection();

    // ************************ Fraud detection *****************
    Collection<String> fraudsDetectedFromListOfInputs =
        creditCardFraud.detect(transactions, dateFromCsvSample, priceThreshold);

    log.debug("Frauds -- {}", fraudsDetectedFromListOfInputs);
  }

  private void detectFraudsFromInputTransactionsCsvFile(
      Fraud<String, String> creditCardFraud, Double priceThreshold, LocalDate dateFromCsvSample)
      throws URISyntaxException {
    // Mock file setup
    Path filePath = mockTransactionsCsvFile();

    // ************************ Fraud detection *****************
    Collection<String> fraudsDetectedFromFileParsing =
        creditCardFraud.detect(filePath, dateFromCsvSample, priceThreshold);

    log.debug("Frauds -- {}", fraudsDetectedFromFileParsing);
  }
}
