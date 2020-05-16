package au.com.touch.app.fraud.detection;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;


import au.com.touch.app.exception.CreditCardFraudDetectionException;
import au.com.touch.app.transaction.TransactionSource;
import au.com.touch.app.vo.CreditCardTransactionVo;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shivaji Byrapaneni
 */
@Slf4j
public class CreditCardFraudImpl implements Fraud<String, String> {

  private final BiFunction<LocalDateTime, LocalDate, Integer>
      transactionDateTimeFallingUnderSuppliedDateFunc =
      (transactionDateTime, fraudTransactionDetectionDate) ->
          transactionDateTime.toLocalDate().compareTo(fraudTransactionDetectionDate);

  private final TransactionSource<CreditCardTransactionVo> transactionSource;

  public CreditCardFraudImpl(TransactionSource<CreditCardTransactionVo> transactionSource) {
    this.transactionSource = transactionSource;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Parses collection of credit card csv transactions
   *
   * @param inputTransactions
   * @param transactionDate
   * @param priceThreshold
   * @return
   */
  @Override
  public Collection<String> detect(
      Collection<String> inputTransactions, LocalDate transactionDate, Double priceThreshold) {
    log.debug("Detecting frauds from input transactions.");
    log.debug("Total transaction received [{}]", inputTransactions.size());
    Collection<CreditCardTransactionVo> creditCardTransactionVos =
        this.transactionSource.parse(inputTransactions);
    return this.detectInternal(creditCardTransactionVos, transactionDate, priceThreshold);
  }

  /**
   * {@inheritDoc}
   *
   * <p>parses a CSV file of credit card csv transactions
   *
   * @param filePath        csv content file path which need to be processed to load transactions
   * @param transactionDate transaction date we need to detect frauds
   * @param priceThreshold  price threshold above which is to be considered as fraud
   * @return
   */
  @Override
  public Collection<String> detect(
      Path filePath, LocalDate transactionDate, Double priceThreshold) {
    try {
      log.debug("Detecting frauds from input transactions file");
      log.debug("File Path [{}]", filePath.toAbsolutePath());
      Collection<CreditCardTransactionVo> creditCardTransactionVos =
          this.transactionSource.parse(filePath);
      return this.detectInternal(creditCardTransactionVos, transactionDate, priceThreshold);
    } catch (IOException e) {
      throw new CreditCardFraudDetectionException(e.getMessage(), e);
    }
  }

  /**
   * A credit card will be identified as fraudulent if the sum of prices for a unique hashed credit
   * * card number, for a given day, exceeds the price threshold T
   *
   * @param transactions    transaction strings to process
   * @param transactionDate transaction date we need to detect frauds
   * @param priceThreshold  price threshold above which is to be considered as fraud
   * @return a collection of credit card number hash that are marked as fraud
   */
  private Collection<String> detectInternal(
      Collection<CreditCardTransactionVo> transactions,
      LocalDate transactionDate,
      Double priceThreshold) {

    log.debug("Find frauds from total transactions [{}]", transactions.size());
    log.debug("Find frauds on Date [{}] and price threshold [{}]", transactionDate, priceThreshold);

    final Predicate<CreditCardTransactionVo> transactionDateFilterPredicate =
        creditCardTransactionVo ->
            0
                == transactionDateTimeFallingUnderSuppliedDateFunc.apply(
                creditCardTransactionVo.getTransactionDateTime(), transactionDate);

    final Collector<CreditCardTransactionVo, ?, Map<String, Double>>
        totalTransactionAmountGroupedByCreditCardNumHashCollector =
        groupingBy(
            CreditCardTransactionVo::getCreditCardNumberHash,
            summingDouble(CreditCardTransactionVo::getTransactionAmount));

    final Predicate<Entry<String, Double>> totalAbovePriceThresholdPredicate =
        creditCardHashToTotalTransactionAmountEntry ->
            creditCardHashToTotalTransactionAmountEntry.getValue() > priceThreshold;

    return transactions
        .stream()
        // Filter transaction falling on supplied date
        .filter(transactionDateFilterPredicate)
        // Sum all transaction amount grouped by hashed credit card number
        .collect(totalTransactionAmountGroupedByCreditCardNumHashCollector)
        .entrySet()
        .stream()
        // Filter transaction that are above price threshold
        .filter(totalAbovePriceThresholdPredicate)
        // Only interested in credit card number hashes
        .map(Entry::getKey)
        // Collect credit card numbers and return
        .collect(Collectors.toSet());
  }
}
