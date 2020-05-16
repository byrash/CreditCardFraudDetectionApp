package au.com.shivaji.app.approach3.exception;

/**
 * Application custom exception
 *
 * @author Shivaji Byrapaneni
 */
public class CreditCardFraudDetectionException extends RuntimeException {

  public CreditCardFraudDetectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public CreditCardFraudDetectionException(String message) {
    super(message);
  }
}
