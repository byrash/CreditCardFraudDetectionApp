package au.com.touch.app.fraud.detection;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Fraud detection strategy.
 *
 * @author Shivaji Byrapaneni
 */
public interface Fraud<I, O> {

  /** Detects frauds based on collection of inputs */
  Collection<O> detect(Collection<I> inputs, LocalDate transactionDate, Double priceThreshold);

  /** Detects frauds based on file path */
  Collection<O> detect(Path filePath, LocalDate transactionDate, Double priceThreshold);
}
