package au.com.touch.app.transaction;

import au.com.touch.app.vo.CreditCardTransactionVo;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link Transaction} implementation for credit card csv transaction .
 *
 * <p>This class processes individual credit card csv transaction string
 *
 * @author Shivaji Byrapaneni
 */
@Slf4j
public class CreditCardCsvTransactionImpl implements Transaction<String, CreditCardTransactionVo> {
  private static final String ELEMENT_SEPARATOR = ",";

  /**
   * {@inheritDoc}
   *
   * <p>A credit card transaction is comprised of the following elements; hashed credit card number
   * timestamp - of format 'year-month-dayThour:minute:second' price - of format 'dollars.cents'
   *
   * <p>Parses individual transaction string and returns Optional Credit card transaction vo.<b>If
   * unable to parse, it returns empty optional</b>
   *
   * @param transaction csv transaction string
   * @return
   */
  @SuppressWarnings("SpellCheckingInspection")
  @Override
  public Optional<CreditCardTransactionVo> parse(final String transaction) {
    log.debug("Trying to Parsing transaction String [{}]", transaction);
    String[] transactionData = transaction.split(ELEMENT_SEPARATOR);
    if (transactionData.length != 3) {
      log.error("Arid transaction record [{}] supplied", transaction);
      return Optional.empty();
    }
    try {
      String creditCardNumberHash = transactionData[0].trim();
      LocalDateTime transactionDateTime = LocalDateTime.parse(transactionData[1].trim());
      double transactionAmount = Double.parseDouble(transactionData[2].trim());
      CreditCardTransactionVo creditCardTransactionVo =
          new CreditCardTransactionVo(creditCardNumberHash, transactionDateTime, transactionAmount);
      log.debug("Parsing transaction is successful");
      return Optional.of(creditCardTransactionVo);
    } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
      log.error(
          "Arid transaction record [{}] supplied; Unable to process elements from transaction record",
          transaction);
      return Optional.empty();
    }
  }
}
