package org.fourstack.temporal.swiggyorders;

import io.temporal.worker.WorkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.fourstack.temporal.swiggyorders.config.WorkFlowAndActivitiesRegisterFactory.registerWorkFlowsAndActivities;

@SpringBootApplication
public class SwiggyOrdersApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SwiggyOrdersApplication.class, args);

        WorkerFactory workerFactory = context.getBean(WorkerFactory.class);
        registerWorkFlowsAndActivities(context, workerFactory);
        workerFactory.start();
    }

}
