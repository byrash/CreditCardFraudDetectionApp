package au.com.touch.app;

import au.com.touch.app.fraud.detection.CreditCardFraudImpl;
import au.com.touch.app.fraud.detection.Fraud;
import au.com.touch.app.transaction.CreditCardCsvTransactionImpl;
import au.com.touch.app.transaction.CreditCardCsvTransactionSourceImpl;
import au.com.touch.app.transaction.Transaction;
import au.com.touch.app.transaction.TransactionSource;
import au.com.touch.app.vo.CreditCardTransactionVo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** @author Shivaji Byrapaneni */
@ComponentScan(basePackages = "au.com.touch.app")
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
