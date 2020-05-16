package au.com.shivaji.app.approach3;

import au.com.shivaji.app.approach3.detection.CreditCardFraudImpl;
import au.com.shivaji.app.approach3.detection.Fraud;
import au.com.shivaji.app.approach3.transaction.CreditCardCsvTransactionImpl;
import au.com.shivaji.app.approach3.transaction.CreditCardCsvTransactionSourceImpl;
import au.com.shivaji.app.approach3.transaction.Transaction;
import au.com.shivaji.app.approach3.transaction.TransactionSource;
import au.com.shivaji.app.approach3.vo.CreditCardTransactionVo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shivaji Byrapaneni
 */
@ComponentScan(basePackages = "au.com.shivaji.app")
@Configuration
public class CreditCardFraudDetectionAppContext {
  @Bean
  Transaction<String, CreditCardTransactionVo> creditCardCsvTransactionBean() {
    return new CreditCardCsvTransactionImpl();
  }

  @Bean
  TransactionSource<CreditCardTransactionVo> creditCardCsvTransactionSourceBean() {
    return new CreditCardCsvTransactionSourceImpl(creditCardCsvTransactionBean());
  }

  @Bean
  Fraud<String, String> creditCardFraudBean() {
    return new CreditCardFraudImpl(creditCardCsvTransactionSourceBean());
  }
}
