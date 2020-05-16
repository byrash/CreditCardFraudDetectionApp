package au.com.shivaji.app;

import static java.util.stream.Collectors.toList;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FraudDetection {

  private static final Map<LocalDate, Map<String, Double>> transactions = new HashMap<>();

  //Map <TransactionDate, Map<Sum of transactions in the slots of 100, Map<Transaction Amount, Credit Card Number>>>
  public static void main(String[] args) {
    List<String> entries = new ArrayList<>() {{
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 1,2017-04-29T13:15:54,10.00");
      add("CC 1,2017-04-29T13:15:54,20.30");
      add("CC 1,2017-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.10");
      add("CC 3,2019-04-29T13:15:54,20.00");
      add("CC 2,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 9,2017-04-29T13:15:54,10.00");
      add("CC 9,2017-04-29T13:15:54,20.30");
      add("CC 9,2017-04-29T13:15:54,20.00");
      add("CC 8,2018-04-29T13:15:54,20.00");
      add("CC 7,2018-04-29T13:15:54,20.00");
      add("CC 6,2018-04-29T13:15:54,20.10");
      add("CC 6,2019-04-29T13:15:54,20.00");
      add("CC 5,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 1,2017-04-29T13:15:54,10.00");
      add("CC 1,2017-04-29T13:15:54,20.30");
      add("CC 1,2017-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.10");
      add("CC 3,2019-04-29T13:15:54,20.00");
      add("CC 2,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 9,2017-04-29T13:15:54,10.00");
      add("CC 9,2017-04-29T13:15:54,20.30");
      add("CC 9,2017-04-29T13:15:54,20.00");
      add("CC 8,2018-04-29T13:15:54,20.00");
      add("CC 7,2018-04-29T13:15:54,20.00");
      add("CC 6,2018-04-29T13:15:54,20.10");
      add("CC 6,2019-04-29T13:15:54,20.00");
      add("CC 5,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 1,2017-04-29T13:15:54,10.00");
      add("CC 1,2017-04-29T13:15:54,20.30");
      add("CC 1,2017-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.10");
      add("CC 3,2019-04-29T13:15:54,20.00");
      add("CC 2,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 9,2017-04-29T13:15:54,10.00");
      add("CC 9,2017-04-29T13:15:54,20.30");
      add("CC 9,2017-04-29T13:15:54,20.00");
      add("CC 8,2018-04-29T13:15:54,20.00");
      add("CC 7,2018-04-29T13:15:54,20.00");
      add("CC 6,2018-04-29T13:15:54,20.10");
      add("CC 6,2019-04-29T13:15:54,20.00");
      add("CC 5,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-04-29T13:15:54,10.00");
      add("CC 1,2014-04-29T13:15:54,20.30");
      add("CC 1,2014-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-04-29T13:15:54,20.00");
      add("CC 1,2017-05-29T13:15:54,10.00");
      add("CC 1,2017-06-29T13:15:54,20.30");
      add("CC 1,2017-04-29T13:15:54,20.00");
      add("CC 1,2018-07-29T13:15:54,20.00");
      add("CC 1,2018-08-29T13:15:54,20.00");
      add("CC 1,2018-04-29T13:15:54,20.10");
      add("CC 3,2019-05-29T13:15:54,20.00");
      add("CC 2,2019-04-29T13:15:54,20.00");
      add("CC 1,2014-05-29T13:15:54,10.00");
      add("CC 1,2014-05-29T13:15:54,20.30");
      add("CC 1,2014-08-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-04-29T13:15:54,20.00");
      add("CC 1,2013-09-29T13:15:54,20.10");
      add("CC 3,2013-04-29T13:15:54,20.00");
      add("CC 2,2013-01-29T13:15:54,20.00");
      add("CC 9,2017-04-29T13:15:54,10.00");
      add("CC 9,2017-03-29T13:15:54,20.30");
      add("CC 9,2017-04-29T13:15:54,20.00");
      add("CC 8,2018-04-29T13:15:54,20.00");
      add("CC 7,2018-05-29T13:15:54,20.00");
      add("CC 6,2018-04-29T13:15:54,20.10");
      add("CC 6,2019-04-29T13:15:54,20.00");
      add("CC 5,2019-07-29T13:15:54,20.00");
    }};
    var start = System.nanoTime();
    entries.forEach(FraudDetection::parse);
    System.out.println("Parsing took::" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milli seconds");
    LocalDate date = LocalDate.parse("2019-04-29");
    double amount = Double.parseDouble("15.23");
//    System.out.println(transactions);
    if (transactions.containsKey(date)) {
      var ccs = transactions.get(date).entrySet().stream().filter(e -> e.getValue() >= amount).map(Map.Entry::getKey).collect(toList());
      System.out.println(ccs);
    } else {
      System.out.println("No Transactions");
    }
    System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milli seconds");
  }

  private static void parse(String line) {
    String[] split = line.split(",");
    if (split.length != 3) {
      return;
    }
    String creditCardNumberHash = split[0].trim();
    LocalDateTime transactionDateTime = LocalDateTime.parse(split[1].trim());
    double transactionAmount = Double.parseDouble(split[2].trim());
    if (!transactions.containsKey(transactionDateTime.toLocalDate())) {
      Map<String, Double> entries = new HashMap<>();
      entries.put(creditCardNumberHash, (double) 0);
      transactions.put(transactionDateTime.toLocalDate(), entries);
    }
    Map<String, Double> entries = transactions.get(transactionDateTime.toLocalDate());
    if (!entries.containsKey(creditCardNumberHash)) {
      entries.put(creditCardNumberHash, (double) 0);
    }
    entries.put(creditCardNumberHash, entries.get(creditCardNumberHash) + transactionAmount);
  }

}
