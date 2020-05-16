package au.com.shivaji.app.approach3.transaction;

import static org.apache.commons.lang3.StringUtils.join;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import au.com.shivaji.app.approach3.vo.CreditCardTransactionVo;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Shivaji Byrapaneni
 */
class CreditCardCsvTransactionImplTest {

  private final Transaction<String, CreditCardTransactionVo> objectUnderTest =
      new CreditCardCsvTransactionImpl();
  private final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

  /**
   * Given a transaction string expect the parse transaction string to parse the string and convert
   * it into our custom Credit Card Transaction Vo adn present the vo wrapped in an optional
   *
   * @param creditCardHash      credit card transaction hash
   * @param transactionDateTime transaction date time
   * @param transactionAmount   transaction amount
   */
  @ParameterizedTest
  @CsvSource({
      "10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00",
      "abc, 2014-04-29T13:15:54, 25.00"
  })
  void parse_Valid(String creditCardHash, String transactionDateTime, String transactionAmount) {
    Optional<CreditCardTransactionVo> optionalCreditCardTransactionVo =
        objectUnderTest.parse(
            join(Arrays.asList(creditCardHash, transactionDateTime, transactionAmount), ','));
    assertNotNull(optionalCreditCardTransactionVo);
    assertTrue(optionalCreditCardTransactionVo.isPresent());
    CreditCardTransactionVo CreditCardTransactionVo = optionalCreditCardTransactionVo.get();
    assertEquals(creditCardHash, CreditCardTransactionVo.getCreditCardNumberHash());
    LocalDateTime dateTime = LocalDateTime.parse(transactionDateTime);
    assertEquals(0, dateTime.compareTo(CreditCardTransactionVo.getTransactionDateTime()));
    assertEquals(
        transactionAmount, decimalFormat.format(CreditCardTransactionVo.getTransactionAmount()));
  }

  /**
   * if invalid string supplied expected to get back empty optional
   *
   * @param transactionStr transaction string that is composed of credit card number hash,
   *                       transaction date time, transaction amount
   */
  @ParameterizedTest
  @ValueSource(
      strings = {
          "10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-2913:15:54, 10.00",
          "10d7ce2f43e35fa57d1bbf8b1e2, , "
      })
  void parse_InValid(String transactionStr) {
    Optional<CreditCardTransactionVo> optionalCreditCardTransactionVo =
        objectUnderTest.parse(transactionStr);
    assertNotNull(optionalCreditCardTransactionVo);
    assertFalse(optionalCreditCardTransactionVo.isPresent());
  }
}
