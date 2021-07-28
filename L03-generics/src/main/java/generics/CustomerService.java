package generics;

import java.util.*;

public class CustomerService {

    private final TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = map.firstEntry();
        Customer customer = firstEntry.getKey();

        return new AbstractMap.SimpleEntry<>(
                new Customer(customer.getId(), customer.getName(), customer.getScores()),
                firstEntry.getValue()
        );
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = map.higherEntry(customer);

        return Optional.ofNullable(higherEntry)
                .map(entry -> {
                    Customer higherCustomer = entry.getKey();
                    return new AbstractMap.SimpleEntry<> (
                            new Customer(higherCustomer.getId(), higherCustomer.getName(), higherCustomer.getScores()),
                            entry.getValue()
                    );
                }).orElse(null);
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
