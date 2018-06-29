package au.com.touch.app.transaction;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import au.com.touch.app.vo.CreditCardTransactionVo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link au.com.touch.app.transaction.TransactionSource} implementation for credit card csv
 * transaction source.
 *
 * <p><b>NOTE::: Invalid Transaction records are being ignored;</b>
 *
 * @author Shivaji Byrapaneni
 */
@Slf4j
public class CreditCardCsvTransactionSourceImpl
    implements TransactionSource<CreditCardTransactionVo> {

  private final Function<String, Optional<CreditCardTransactionVo>> stringToTransactionVoFunc;

  public CreditCardCsvTransactionSourceImpl(
      Transaction<String, CreditCardTransactionVo> transaction) {
    this.stringToTransactionVoFunc = transaction::parse;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Parses the given file path and returns a collection of valid Credit card transaction vos
   *
   * @param filePath file path to load transactions from
   * @return
   * @throws IOException
   */
  @Override
  public Collection<CreditCardTransactionVo> parse(Path filePath) throws IOException {
    log.debug("Credit card transaction source file based parsing");
    try (Stream<String> lines = Files.lines(filePath, UTF_8)) {
      return parse(lines.collect(toList()));
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Parses the given collection of transaction entries and returns a collection of valid Credit
   * card transaction vos
   *
   * @param inputs collection of transactions
   * @return
   */
  @Override
  public Collection<CreditCardTransactionVo> parse(Collection<String> inputs) {
    log.debug("Credit card transaction source collection based parsing");
    return inputs
        .stream() // Use Parallel Stream based on input load
        .map(stringToTransactionVoFunc)
        // Ignoring invalid transaction records
        .filter(Optional::isPresent) // May be send the line to a error processing topic or ... ?
        .map(Optional::get) // unwrap from optionals
        .collect(toList());
  }
}
