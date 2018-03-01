/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationprocessing;

import implementationconekta.MainThread;
import io.conekta.Customer;
import io.conekta.Error;
import io.conekta.ErrorList;
import io.conekta.Order;
import org.json.JSONObject;

/**
 *Fills in the last field and create an Order
 * @author julien
 */
public class PaymentInformation {

    static private PaymentInformation instance;

    private PaymentInformation() {

    }

    public static PaymentInformation getInstance() {
        if (instance == null) {
            instance = new PaymentInformation();
        }
        return instance;
    }

    public void makeOrder(int paymentType, String cardToken) throws Error, ErrorList {

        String customerId = MainThread.getInstance().getCustomerId();
        
        Customer customer = Customer.find(customerId);
        OrderInformation order = OrderInformation.getInstance();

        String orderArguments = "{"
                + "'currency': 'mxn',"
                + "'metadata': {"
                + "    'test': true"
                + "},"
                + "'line_items': [{"
                + "    'name': " + order.getName() + ","
                + "    'description': " + order.getDescription() + ","
                + "    'unit_price': " + order.getPrice() + ","
                + "    'quantity': " + order.getQuantity() + ","
                + "    'tags': ['food', 'mexican food'],"
                + "    'type': 'physical'"
                + "}],"
                + "'customer_info': { "
                + "    'name': " + customer.name + ","
                + "    'phone': " + customer.phone + ","
                + "    'email': " + customer.email
                + "},";

        String type;
        switch (paymentType) {
            case 0:
                type = "card";
                orderArguments += "'charges': [{"
                        + "    'payment_method': {"
                        + "        'type': " + type + ","
                        + "        'token_id': " + cardToken
                        + "    }, "
                        + "    'amount': " + order.getPrice() * order.getQuantity()
                        + "}]"
                        + "}";
                break;
            case 1:
                type = "oxxo_cash";
                 orderArguments += "'charges': [{"
                + "    'payment_method': {"
                + "        'type': " + type + ","
                + "    }, "
                + "    'amount': " + order.getPrice() * order.getQuantity()
                + "}]"
                + "}";
                break;
            default:
                type = "spei";
                orderArguments += "'charges': [{"
                + "    'payment_method': {"
                + "        'type': " + type + ","
                + "    }, "
                + "    'amount': " + order.getPrice() * order.getQuantity()
                + "}]"
                + "}";
                break;
        }

        JSONObject completeOrderJSON = new JSONObject(orderArguments);
        Order completeOrder = Order.create(completeOrderJSON);
        
        MainThread.getInstance().setOrder(completeOrder);
        MainThread.getInstance().setChargeType(type);
        
        synchronized (MainThread.getInstance()) {
            MainThread.pause = false;
            MainThread.getInstance().notify();
        }
    }

}
