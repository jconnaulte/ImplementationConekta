/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationprocessing;

import forms.CardRecapForm;
import io.conekta.Charge;
import main.MainThread;
import io.conekta.LineItems;
import io.conekta.Order;
import io.conekta.OxxoPayment;
import io.conekta.SpeiPayment;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.apache.commons.io.FileUtils;

/**
 * Process the response to the order, depending on the payment type.
 *
 * @author julien
 */
public class OrderProcessing {

    private static OrderProcessing instance;

    private OrderProcessing() {

    }

    public static OrderProcessing getInstance() {
        if (instance == null) {
            instance = new OrderProcessing();
        }
        return instance;
    }

    public void generateAnswer(String type) throws IOException {
        if (type.equals("card")) {
            generateDefaultAnswer();
        } else if (type.equals("oxxo_cash")) {
            synchronized (MainThread.getInstance()) {
                MainThread.pause = false;
                MainThread.getInstance().notify();
            }
            createOxxoStub();
        } else {
            createSpeiStub();
            synchronized (MainThread.getInstance()) {
                MainThread.pause = false;
                MainThread.getInstance().notify();
            }
        }
    }

    // If paid by credit card
    private void generateDefaultAnswer() {
        Order order = MainThread.getInstance().getOrder();
        System.out.println(order.id);
        LineItems item = (LineItems) order.line_items.get(0);
        CardRecapForm recapForm = new CardRecapForm(order.id, order.amount / 100, item.name, item.quantity, item.unit_price / 100);
        //  recapForm.start();
    }

    // If paid by OXXO
    public void createOxxoStub() throws IOException {
        Order order = MainThread.getInstance().getOrder();
        String normAmount = Float.toString((float) order.amount / (float) 100);
        Charge charge = (Charge) order.charges.get(0);
        OxxoPayment oxxoPayment = (OxxoPayment) charge.payment_method;

        File file = new File("./src/forms/oxxostub/opps_en.html");
        org.jsoup.nodes.Document doc = Jsoup.parse(file, null);

        Element div = doc.getElementsByClass("opps-amount").first().select("h2").first();
        div.text("$ " + normAmount + " MXN ");

        div = doc.getElementsByClass("opps-reference").first().select("h1").first();
        div.text(oxxoPayment.reference);

        FileUtils.writeStringToFile(file, doc.outerHtml(), "UTF-8");

        Desktop.getDesktop().browse(file.toURI());
    }

    // if paid by SPEI
    public static void createSpeiStub() throws IOException {
        Order order = MainThread.getInstance().getOrder();
        String normAmount = Float.toString((float) order.amount / (float) 100);
        Charge charge = (Charge) order.charges.get(0);
        SpeiPayment speiPayment = (SpeiPayment) charge.payment_method;

        File file = new File("./src/forms/speistub/spes_en.html");
        org.jsoup.nodes.Document doc = Jsoup.parse(file, null);

        Element div = doc.getElementsByClass("ps-amount").first().select("h2").first();
        div.text("$ " + normAmount + " MXN ");

        div = doc.getElementsByClass("ps-reference").first().select("h1").first();
        div.text(speiPayment.clabe);

        FileUtils.writeStringToFile(file, doc.outerHtml(), "UTF-8");

        Desktop.getDesktop().browse(file.toURI());
    }

    // Resume the program and close it
    public void cardFormEnded() {
        synchronized (MainThread.getInstance()) {
            MainThread.pause = false;
            MainThread.getInstance().notify();
            System.exit(0);
        }
    }

}
