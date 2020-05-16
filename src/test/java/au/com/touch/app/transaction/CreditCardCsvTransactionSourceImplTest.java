package au.com.touch.app.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;


import au.com.touch.app.vo.CreditCardTransactionVo;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Shivaji Byrapaneni
 */
class CreditCardCsvTransactionSourceImplTest {

  @Mock
  private CreditCardCsvTransactionImpl transaction;

  @InjectMocks
  private CreditCardCsvTransactionSourceImpl objectUnderTest;

  /**
   * Given a file with transactions, transaction date, price threshold expect
   * findFraudulentTransactions operation to process all the transaction strings supplied, consider
   * only valid transaction strings, consider only transactions for the transaction date given and
   * summing all their transaction amounts grouped by credit card number hash. Filter transaction
   * above price threshold supplied and return their credit card number hash.
   */
  @Test
  void parse_file() throws Exception {
    URL resource = getClass().getClassLoader().getResource("test_transactions.csv");
    if (resource != null) {
      Collection<CreditCardTransactionVo> creditCardTransactionVos =
          objectUnderTest.parse(Paths.get(resource.toURI()));
      assertFalse(creditCardTransactionVos.isEmpty());
      assertEquals(11, creditCardTransactionVos.size()); // 1 Invalid ignored
    }
  }

  /**
   * Given a set of transaction records, transaction date, price threshold expect
   * findFraudulentTransactions operation to process all the transaction strings supplied, consider
   * only valid transaction strings, consider only transactions for the transaction date given and
   * summing all their transaction amounts grouped by credit card number hash. Filter transaction
   * above price threshold supplied and return their credit card number hash.
   */
  @Test
  void parse_collection() {
    Collection<String> transactions =
        Arrays.asList(
            "10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00",
            "abc, 2014-04-29T13:15:54, 25.00",
            "xyz, 2014-04-29T13:15:54, 5.00",
            "def, 2014-04-29T13:15:54, 25.00",
            "ghi, 2014-04-29T13:15:54, 25.00",
            "jkl, 2014-04-29T13:15:54, 25.00",
            "xyz, 2014-04-28T13:15:54, 5.00",
            "mno, 2014-04-29T13:15:54, 5.00",
            "pqr, 2014-04-28T13:15:54, 25.00",
            "stu, 2014-04-2913:15:54, 25.00",
            "xyz, 2014-04-29T13:15:54, 5.00",
            "xyz, 2014-04-29T13:15:54, 1.00");
    Collection<CreditCardTransactionVo> creditCardTransactionVos =
        objectUnderTest.parse(transactions);
    assertFalse(creditCardTransactionVos.isEmpty());
    assertEquals(11, creditCardTransactionVos.size()); // 1 Invalid ignored
  }

  @BeforeEach
  void mockCalls() {
    MockitoAnnotations.initMocks(this);
    CreditCardTransactionVo creditCardTransactionVo =
        new CreditCardTransactionVo("1", LocalDateTime.now(), 1.0);
    when(transaction.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("abc, 2014-04-29T13:15:54, 25.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("xyz, 2014-04-29T13:15:54, 5.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("def, 2014-04-29T13:15:54, 25.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("ghi, 2014-04-29T13:15:54, 25.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("jkl, 2014-04-29T13:15:54, 25.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("xyz, 2014-04-28T13:15:54, 5.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("mno, 2014-04-29T13:15:54, 5.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("pqr, 2014-04-28T13:15:54, 25.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("stu, 2014-04-2913:15:54, 25.00")).thenReturn(Optional.empty());
    when(transaction.parse("xyz, 2014-04-29T13:15:54, 5.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
    when(transaction.parse("xyz, 2014-04-29T13:15:54, 1.00"))
        .thenReturn(Optional.of(creditCardTransactionVo));
  }
}
