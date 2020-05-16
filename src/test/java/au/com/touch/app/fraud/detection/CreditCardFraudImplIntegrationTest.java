package au.com.touch.app.fraud.detection;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;


import au.com.touch.app.transaction.CreditCardCsvTransactionImpl;
import au.com.touch.app.transaction.CreditCardCsvTransactionSourceImpl;
import au.com.touch.app.transaction.Transaction;
import au.com.touch.app.transaction.TransactionSource;
import au.com.touch.app.vo.CreditCardTransactionVo;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.Test;

/**
 * @author Shivaji Byrapaneni
 */
class CreditCardFraudImplIntegrationTest {

  private final Transaction<String, CreditCardTransactionVo> transaction =
      new CreditCardCsvTransactionImpl();
  private final TransactionSource<CreditCardTransactionVo> transactionSource =
      new CreditCardCsvTransactionSourceImpl(transaction);
  private final Fraud<String, String> objectUnderTest = new CreditCardFraudImpl(transactionSource);

  @Test
  void detectBasedOnFileSource_Test() throws URISyntaxException {
    URL resource = getClass().getClassLoader().getResource("test_transactions.csv");
    if (resource != null) {
      Path filePath = Paths.get(resource.toURI());
      Collection<String> detect =
          objectUnderTest.detect(filePath, LocalDate.parse("2014-04-29"), 10.0);
      assertEquals("abc,def,xyz,ghi,jkl", detect.stream().collect(joining(",")));
    }
  }

  @Test
  void detectBasedOnCollectionSource_Test() {
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
    Collection<String> detect =
        objectUnderTest.detect(transactions, LocalDate.parse("2014-04-29"), 10.0);
    assertEquals("abc,def,xyz,ghi,jkl", detect.stream().collect(joining(",")));
  }
}
