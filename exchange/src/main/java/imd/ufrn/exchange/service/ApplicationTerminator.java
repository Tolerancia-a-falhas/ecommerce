package imd.ufrn.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationTerminator {

    @Autowired
    private ApplicationContext context;

    public void stopApplication() {
        // SpringApplication.exit(context, () -> 0);
        System.exit(0);
    }
}
