package ru.java;

import ru.java.handler.ComplexProcessor;
import ru.java.listener.HistoryListener;
import ru.java.listener.ListenerPrinterConsole;
import ru.java.model.Message;
import ru.java.processor.LoggerProcessor;
import ru.java.processor.ProcessorEvenSecondThrow;
import ru.java.processor.ProcessorSwapFields11And12;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(
                new ProcessorSwapFields11And12(),
                new LoggerProcessor(new ProcessorEvenSecondThrow(LocalDateTime.now()))
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> {});

        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();

        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
