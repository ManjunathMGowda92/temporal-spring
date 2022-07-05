package org.fourstack.temporal.swiggyorders.config;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderActivity;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderWorkFlowImpl;
import org.springframework.context.ConfigurableApplicationContext;

import static org.fourstack.temporal.swiggyorders.config.QueueConfigs.CUSTOMER_ORDER_QUEUE;

/**
 * Class responsible to register the Workflow Impl and Activity Impl related to the Workflows.
 * Also creates the worker responsible for Workflows. Each Workflow should have a separate Worker.
 *
 * @author manjunath
 */
public class WorkFlowAndActivitiesRegisterFactory {

    public static void registerWorkFlowsAndActivities(ConfigurableApplicationContext context, WorkerFactory factory) {
        // Register OrderWorkFlow and OrderActivity
        registerOrderWorkFlowAndActivity(context, factory);
    }

    /**
     * Method responsible to register the Workflow and Activity related to Order Workflow.
     *
     * @param context SpringApplicationContext object.
     * @param factory WorkerFactory object from Temporal framework.
     */
    private static void registerOrderWorkFlowAndActivity(ConfigurableApplicationContext context, WorkerFactory factory) {
        // Get OrderActivity Bean from the Application Context
        OrderActivity activity = context.getBean(OrderActivity.class);

        // Create Worker for Customer_Order
        Worker customerOrderWorker = factory.newWorker(CUSTOMER_ORDER_QUEUE);

        // register WorkFlow and Activity for Order
        customerOrderWorker.registerWorkflowImplementationTypes(OrderWorkFlowImpl.class);
        customerOrderWorker.registerActivitiesImplementations(activity);
    }
}
