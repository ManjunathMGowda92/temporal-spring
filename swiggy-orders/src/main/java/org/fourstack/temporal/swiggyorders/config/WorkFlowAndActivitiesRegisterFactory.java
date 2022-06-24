package org.fourstack.temporal.swiggyorders.config;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderActivity;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderWorkFlowImpl;
import org.springframework.context.ConfigurableApplicationContext;

import static org.fourstack.temporal.swiggyorders.config.QueueConfigs.CUSTOMER_ORDER_QUEUE;

public class WorkFlowAndActivitiesRegisterFactory {

    public static void registerWorkFlowsAndActivities(ConfigurableApplicationContext context, WorkerFactory factory) {
        // Register OrderWorkFlow and OrderActivity
        registerOrderWorkFlowAndActivity(context, factory);
    }

    private static void registerOrderWorkFlowAndActivity(ConfigurableApplicationContext context, WorkerFactory factory) {
        // Get OrderActivity Bean from the Application Context
        OrderActivity activity = context.getBean(OrderActivity.class);
        // Create Worker for Customer_Order
        Worker worker = factory.newWorker(CUSTOMER_ORDER_QUEUE);

        // register WorkFlow and Activity for Order
        worker.registerWorkflowImplementationTypes(OrderWorkFlowImpl.class);
        worker.registerActivitiesImplementations(activity);
    }
}
