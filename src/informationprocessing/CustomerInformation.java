/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationprocessing;

import io.conekta.*;
import org.json.JSONObject;
import implementationconekta.MainThread;

/**
 *
 * @author julien
 */
public class CustomerInformation {

    public CustomerInformation() {
        
    }

    // Get the customer information from the form and create e new Customer
    public synchronized void createNewCustomer(String name, String phone, String email) throws io.conekta.Error, ErrorList {
        JSONObject customerJSON = new JSONObject("{"
                + "'name': '" + name + "',"
                + "'email': '" + email + "',"
                + "'phone': '" + phone
                + "'}");
        Customer customer = Customer.create(customerJSON);
        MainThread.getInstance().setCustomerId(customer.id);
        synchronized (MainThread.getInstance()) {
            MainThread.pause = false;
            MainThread.getInstance().notify();
        }
    }
}
