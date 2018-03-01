/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationprocessing;

import implementationconekta.MainThread;

/**
 * Get some of the fields wich are mandatory to create an Order.
 *
 * @author julien
 */
public class OrderInformation {

    private static OrderInformation instance;

    private String id;
    private String name;
    private String description;
    private int price;
    private int quantity;

    private OrderInformation() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public static OrderInformation getInstance() {
        if (instance == null) {
            instance = new OrderInformation();
        }
        return instance;
    }

    // Depending on what is ordered, fills the attributes with information. 
    // These attributes will be used to create a new Order in the 
    // PaymentInformation class.
    public void orderType(Boolean Tacos, int number) {

        if (Tacos) {
            id = "item_tacos_756";
            name = "Tacos";
            description = "Delicious tacos";
            price = 800;
        } else {
            id = "item_quesadillas_862";
            name = "Quesadillas";
            description = "Juicy quesadillas";
            price = 1200;
        }
        quantity = number;
        synchronized (MainThread.getInstance()) {
            MainThread.pause = false;
            MainThread.getInstance().notify();
        }
    }
}
