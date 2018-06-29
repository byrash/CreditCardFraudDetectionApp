package au.com.touch.app.fraud.detection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;

import au.com.touch.app.transaction.TransactionSource;
import au.com.touch.app.vo.CreditCardTransactionVo;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
/** @author Shivaji Byrapaneni */
class CreditCardFraudImplTest {
  @Mock private TransactionSource transactionSource;

  @InjectMocks private CreditCardFraudImpl objectUnderTest;

  @BeforeEach
  void mockCalls() throws IOException {
    MockitoAnnotations.initMocks(this);
    LocalDateTime localDateTime = LocalDateTime.now();
    List<CreditCardTransactionVo> parsedCreditCardTransactions =
        Arrays.asList(
            new CreditCardTransactionVo("1", localDateTime, 1.0),
            new CreditCardTransactionVo("2", localDateTime, 1.0),
            new CreditCardTransactionVo("2", localDateTime, 3.0),
            new CreditCardTransactionVo("3", localDateTime, 1.0));
    Mockito.when(transactionSource.parse(any(Path.class))).thenReturn(parsedCreditCardTransactions);
    Mockito.when(transactionSource.parse(anyCollectionOf(String.class)))
        .thenReturn(parsedCreditCardTransactions);
  }

  @Test
  void detectBasedOnFileSource_Test() {
    Collection<String> detect =
        objectUnderTest.detect(Paths.get("something.txt"), LocalDate.now(), 2.0);
    assertFalse(detect.isEmpty());
    assertEquals(1, detect.size());
    assertEquals("2", detect.iterator().next());
  }

  @Test
  void detectBasedOnCollectionSource_Test() {
    Collection<String> detect =
        objectUnderTest.detect(Collections.singletonList("1,2,3"), LocalDate.now(), 2.0);
    assertFalse(detect.isEmpty());
    assertEquals(1, detect.size());
    assertEquals("2", detect.iterator().next());
  }
}
