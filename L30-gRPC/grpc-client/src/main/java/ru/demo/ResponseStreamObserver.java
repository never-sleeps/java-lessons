package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.numbers.NumberResponse;

import java.util.concurrent.CountDownLatch;

public class ResponseStreamObserver implements io.grpc.stub.StreamObserver<NumberResponse> {
    private static final Logger log = LoggerFactory.getLogger(ResponseStreamObserver.class);
    private final CountDownLatch latch;

    private long valueFromServer = 0;

    public ResponseStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(NumberResponse numberResponse) {
        var number = numberResponse.getNumber();
        log.info("new value from server:{}", number);
        setValueFromServer(number);
    }

    @Override
    public void onError(Throwable e) {
        log.error("got error", e);
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.info("request completed");
        latch.countDown();
    }

    /**
     * Поскольку число, полученное от сервера должно учитываться только один раз,
     * сбрасываем его после использования в расчёте
     */
    public long getLastValueFromServer() {
        if(this.valueFromServer != 0) {
            var valueForCalculate = this.valueFromServer;
            setValueFromServer(0);
            log.debug("reset valueFromServer");
            return valueForCalculate;
        } else return 0;
    }

    private synchronized void setValueFromServer(long value) {
        this.valueFromServer = value;
    }
}
