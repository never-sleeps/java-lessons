package generics;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    /**
     * Интерфейс java.util.Deque является подтипом интерфейса java.util.Queue.
     * Deque связан с двусторонней очередью, которая поддерживает добавление или удаление элементов
     * с любого конца структуры данных, его можно использовать как очередь (first-in-first-out / FIFO)
     * или как стек (last- in-first-out / LIFO). Это быстрее, чем Stack и LinkedList.
     */
    private final Deque<Customer> customerQueue = new ArrayDeque<>();

    /**
     * push (element): добавляет элемент [customer] в голову
     */
    public void add(Customer customer) {
        customerQueue.push(customer);
    }

    /**
     * pop (element): удаляет элемент из головы и возвращает его.
     */
    public Customer take() {
        return customerQueue.pop();
    }
}
