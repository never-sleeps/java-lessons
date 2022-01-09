package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.numbers.NumberRequest;
import ru.demo.numbers.NumbersServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculateExecutor {

    private static final Logger log = LoggerFactory.getLogger(CalculateExecutor.class);

    private final NumbersServiceGrpc.NumbersServiceStub asyncClient;

    private long currentValue = 0;

    public CalculateExecutor(NumbersServiceGrpc.NumbersServiceStub asyncClient) {
        this.asyncClient = asyncClient;
    }

    public void execute() throws InterruptedException {
        var request = createRequest();
        var observer = new ResponseStreamObserver(new CountDownLatch(1));

        asyncClient.getNumber(request, observer);

        for (var i = 0; i < 50; i++) {
            updateCurrentValue(observer);
            log.info("current value: {}", this.currentValue);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * формула из условия: currentValue = [currentValue] + [ПОСЛЕДНЕЕ число от сервера] + 1
     */
    private void updateCurrentValue(ResponseStreamObserver observer) {
        currentValue = currentValue + observer.getLastValueFromServer() + 1;
    }

    private NumberRequest createRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(10)
                .build();
    }
}
