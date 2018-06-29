package au.com.touch.app;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/** @author Shivaji Byrapaneni */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CreditCardFraudDetectionAppContext.class)
class CreditCardFraudDetectionAppContextTest {

  @Test
  void loadContext() {
    // If test pass context is loaded Okay
    assertTrue(true);
  }
}
