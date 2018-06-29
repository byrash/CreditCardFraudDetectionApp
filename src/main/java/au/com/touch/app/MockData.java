package au.com.touch.app;

import au.com.touch.app.exception.CreditCardFraudDetectionException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

/** @author Shivaji Byrapaneni */
class MockData {

  Collection<String> mockTransactionsCollection() {
    return Arrays.asList(
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
  }

  Path mockTransactionsCsvFile() throws URISyntaxException {
    URL csvFileResource = ExampleUsage.class.getClassLoader().getResource("transactions.csv");
    if (csvFileResource == null) {
      throw new CreditCardFraudDetectionException("Unable to load demo transactions.csv file");
    }
    return Paths.get(csvFileResource.toURI());
  }
}
