package au.com.shivaji.app.approach3.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Holds parsed individual transaction.
 *
 * @author Shivaji Byrapaneni
 */
@Data
@AllArgsConstructor
public class CreditCardTransactionVo {

  private String creditCardNumberHash;
  private LocalDateTime transactionDateTime;
  // Double should be able to hold the maximum amount possible for a credit
  // card per day, if you don't think so start using Big Decimal
  private Double transactionAmount;
}
