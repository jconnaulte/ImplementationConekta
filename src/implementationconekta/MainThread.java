/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementationconekta;

import forms.CustomerInformationsForm;
import forms.OrderInformationForm;
import forms.PaymentInformationForm;
import informationprocessing.OrderProcessing;
import io.conekta.Order;
import java.io.IOException;

/**
 *
 * @author julien
 */
public class MainThread {

    private static MainThread instance;
    private String customerId;
    private Order order;
    private String chargeType;
    public static Boolean pause;

    private MainThread() {
        pause = false;
    }

    public void setChargeType(String type) {
        chargeType = type;
    }

    public void setOrder(Order newOrder) {
        order = newOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setCustomerId(String id) {
        customerId = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    // To be sure only one instance is running at a time
    public static MainThread getInstance() {
        if (instance == null) {
            instance = new MainThread();
        }
        return instance;
    }

    // Main function. Calls the classes from the forms package and 
    // makes pause to let the user fill in.
    public void execute() throws InterruptedException, IOException {

        // Calls the customerInformationForm
        pause = true;
        CustomerInformationsForm customerForm = new CustomerInformationsForm();
        customerForm.start();

        synchronized (this) {
            while (pause) {
                this.wait();
            }
        }
        
        // Calls the OrderInformationForm
        pause = true;
        OrderInformationForm orderForm = new OrderInformationForm();
        orderForm.start();
         
        synchronized (this) {
            while (pause) {
                this.wait();
            }
        }
        
        // Calls the PaymentInformationForm
        pause = true;
        PaymentInformationForm paymentForm = new PaymentInformationForm();
        paymentForm.start();

        synchronized (this) {
            while (pause) {
                this.wait();
            }
        }
         
        // Generate response to the transaction
        pause = true;
        OrderProcessing process = OrderProcessing.getInstance();
        process.generateAnswer(chargeType);

        synchronized (this) {
            while (pause) {
                this.wait();
            }
        }
    }
}
