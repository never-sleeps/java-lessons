package ru.java.ex04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
yum install rsyslog (Fedora)

vi /etc/rsyslog.conf
ModLoad imudp
UDPServerRun 514

systemctl enable rsyslog
systemctl start rsyslog

sudo tail -f /var/log/messages (Fedora)
tail -f /var/log/syslog (Ubuntu)
*/

public class LoggerSyslogDemo {
    private static final Logger logger = LoggerFactory.getLogger(LoggerSyslogDemo.class);
    private long counter = 0;

    public static void main(String[] args) throws InterruptedException {
        new LoggerSyslogDemo().loop();
    }

    private void loop() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            logger.error("msg for Syslog:{}", counter);
            counter++;
            Thread.sleep(3_000);
        }
    }
}
