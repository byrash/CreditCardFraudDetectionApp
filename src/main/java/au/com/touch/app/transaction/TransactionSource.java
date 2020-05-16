package au.com.touch.app.transaction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * This service guides the transactions source. i.e It can be a file or collection.
 *
 * <p>Problem requirement requested to be able to pass a collection.
 *
 * @author Shivaji Byrapaneni
 */
public interface TransactionSource<O> {

  /**
   * Parse the given file and return a collection of parsed results
   */
  Collection<O> parse(Path filePath) throws IOException;

  /**
   * Parse the given collection and return a collection of parsed results
   */
  Collection<O> parse(Collection<String> inputs);
}
