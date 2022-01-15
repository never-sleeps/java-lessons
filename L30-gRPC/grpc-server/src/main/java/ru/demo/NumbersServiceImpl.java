package ru.demo;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.numbers.NumberRequest;
import ru.demo.numbers.NumberResponse;
import ru.demo.numbers.NumbersServiceGrpc;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NumbersServiceImpl.class);

    @Override
    public void getNumber(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        var executor = Executors.newScheduledThreadPool(1);
        log.info("Start streaming. Request: firstValue:{}, lastValue:{}", request.getFirstValue(), request.getLastValue());

        var currentValue = new AtomicLong(request.getFirstValue());

        Runnable task = () -> {
            var value = currentValue.incrementAndGet();
            var response = NumberResponse.newBuilder()
                    .setNumber(value)
                    .build();
            responseObserver.onNext(response);
            if (value == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("Streaming finished ...");
            }
        };
        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }
}
