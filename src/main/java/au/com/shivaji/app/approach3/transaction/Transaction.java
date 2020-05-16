package au.com.shivaji.app.approach3.transaction;

import java.util.Optional;

/**
 * Service to process an individual transaction.
 *
 * <p>i.e. this service deals with parsing the transaction string supplied and convert it to the
 * required type
 *
 * <p>I have utilised generics to be able to use what ever the input type you wanted for a
 * transaction to be i.e. may be a stream of data in future or many be a JMS message or what ever
 * situation demands and parse should return an output type as advised by generic type;
 *
 * <p>The out put is optional, i.e. if I cant parse a transaction string, than just throwing or
 * halting the system, I just return empty optional. Don't we all hate Null Pointer Errors ??
 *
 * @author Shivaji Byrapaneni
 */
public interface Transaction<I, O> {

  /**
   * For the given input transaction type parse and if valid as per implementor strategy return the
   * Optional result
   */
  Optional<O> parse(final I transaction);
}
