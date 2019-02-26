import entity.*;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Implementation {

    public static void main(String[] args) {
        /**
         * Customers
         */
        Customer peter = new Customer("Peter");
        Customer john = new Customer("John");
        Customer mary = new Customer("Mary");


        /**
         *  Products
         */
        Product killBill = new Product(
                "Kill Bill Vol. 1",
                MediaFormat.MOVIE,
                Paths.get("/home/media/killBill_vol1.mkv"),
                new BigDecimal(50)
        );

        Product pulpFiction = new Product(
                "Pulp Fiction",
                MediaFormat.MOVIE,
                Paths.get("/home/media/pulpFiction.mkv"),
                new BigDecimal(100)
        );

        Product greatGatsby = new Product(
                "Great Gatsby",
                MediaFormat.BOOK,
                Paths.get("/home/media/greatGatsby.pdf"),
                new BigDecimal(150)
        );

        Product panteraVulgarDisplayOfPower = new Product(
                "Pantera - Vulgar Display of Power",
                MediaFormat.MUSIC,
                Paths.get("/home/media/panteraVulgarDisplayOfPower.zip"),
                new BigDecimal(200)
        );


        /**
         * Payments
         */
        Payment peterPayment = new Payment(
                Arrays.asList(killBill, pulpFiction),
                LocalDateTime.now().minusDays(1),
                peter
        );

        Payment johnPayment = new Payment(
                Arrays.asList(greatGatsby, pulpFiction),
                LocalDateTime.now(),
                john
        );

        Payment maryFirstPayment = new Payment(
                Arrays.asList(panteraVulgarDisplayOfPower, greatGatsby),
                LocalDateTime.now().plusDays(1),
                mary
        );

        Payment marySecondPayment = new Payment(
                Arrays.asList(pulpFiction, killBill),
                LocalDateTime.now().plusDays(1),
                mary
        );

        List<Payment> payments = Arrays.asList(peterPayment, johnPayment, maryFirstPayment, marySecondPayment);


        /**
         * From now on,
         * we'll just stream the payments and gather useful data around it
         */

        payments.stream()
                .sorted(Comparator.comparing(Payment::getDate))
                .forEach(System.out::println);


        System.out.format("Peter has bought " +
                peterPayment.getProducts().stream()
                        .map(Product::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );


        Stream<BigDecimal> streamOfPayments = payments.stream()
                .flatMap(payment -> payment.getProducts().stream().
                        map(Product::getPrice)
                );

        BigDecimal totalOfPayments = streamOfPayments.reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.format("Total of payments: %s\n", NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(totalOfPayments));


        Map<Product, Long> totalSoldByProduct = payments.stream()
                .flatMap(payment -> payment.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                        )
                );

        totalSoldByProduct.forEach((product, totalSold) -> System.out.format("%s (%d)\n", product, totalSold));


        // Finds out the top sold product
        totalSoldByProduct.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(System.out::println);


        Map<Product, BigDecimal> totalAmountSoldByProduct = payments.stream()
                .flatMap(payment -> payment.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.reducing(BigDecimal.ZERO, Product::getPrice, BigDecimal::add)
                        )
                );

        totalAmountSoldByProduct.forEach(((product, amount) -> System.out.println(product + " - " + NumberFormat.getCurrencyInstance().format(amount))));


        // Finds out the product that has been sold more, and its value
        totalAmountSoldByProduct.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(System.out::println);


        Map<Customer, List<List<Product>>> customersProducts = payments.stream()
                .collect(Collectors.groupingBy(
                        Payment::getCustomer,
                        Collectors.mapping(
                                Payment::getProducts,
                                Collectors.toList())
                        )
                );

        customersProducts.forEach((customer, products) -> System.out.println(customer + " - " + products));


        // Show how many times a media format has been sold
        payments.stream()
                .flatMap(payment -> payment.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Product::getFormat,
                        Collectors.counting()
                        )
                )
                .forEach((mediaFormat, total) -> System.out.format("%s have been sold %d times\n", mediaFormat, total));


        // Groups by media format than display the itens that have been sold under that category
        payments.stream()
                .flatMap(payment -> payment.getProducts().stream())
                .collect(Collectors.groupingBy(Product::getFormat))
                .forEach((mediaFormat, products) -> System.out.println(mediaFormat + " - " + products));


        payments.stream()
                .collect(Collectors.groupingBy(
                        Payment::getCustomer,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Payment::getTotalAmount,
                                BigDecimal::add)
                ))
                .forEach((customer, total) -> System.out.format("Customer \"%s\" has bought %f in total\n", customer.toString().toUpperCase(), total));


        /**
         * Subscriptions
         */

        Subscription peterSubscription = new Subscription(
                peter,
                new BigDecimal(100.00),
                LocalDateTime.now().minusYears(1)
        );

        Subscription johnrSubscription = new Subscription(
                john,
                new BigDecimal(100.00),
                LocalDateTime.now().minusMonths(5),
                LocalDateTime.now().minusMonths(1)
        );

        Subscription marySubscription = new Subscription(
                mary,
                new BigDecimal(100.00),
                LocalDateTime.now().minusMonths(5),
                LocalDateTime.now().minusMonths(2)
        );

        List<Subscription> subscriptions = Arrays.asList(peterSubscription, johnrSubscription, marySubscription);

        // Stats about the subscriptions
        subscriptions.stream()
                .forEach(s -> System.out.format("Customer \"%s\" has paid on subscription %f\n", s.getCustomer().getName().toUpperCase(), s.getTotalPaid()));
    }
}
