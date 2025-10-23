package com.example.kafkabatch.rw;

import com.example.kafkabatch.entity.WebLog;
import net.datafaker.Faker;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WebLogReader implements ItemReader<WebLog> {

    private final Faker faker = new Faker();
    private int genCount = 100;
    private int currentCount = 0;

    /**
     * 실제 weblog 존재하지 않으므로 무작위로 생성 후 return 함.
     * @return WebLog
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public WebLog read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentCount >= genCount) {
            currentCount++;
            return genNewWebLog();
        } else {
            return null;
        }
    }

    public WebLog genNewWebLog() {
        WebLog webLog = new WebLog();
        webLog.setIpAddress(faker.internet().ipV4Address());
        webLog.setUrl(faker.internet().url());
        webLog.setUserId(faker.idNumber().singaporeanFinBefore2000());
        webLog.setSessionId(UUID.randomUUID().toString());
        webLog.setTimestamp(faker.date().past(7, TimeUnit.DAYS).toString());

        return webLog;
    }
}
