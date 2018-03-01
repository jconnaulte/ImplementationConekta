/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementationconekta;

import io.conekta.*;
import java.io.IOException;


/**
 *
 * @author julien
 */
public class Main {

    /**
     * Set the Api Key and launch the main thread.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        Conekta.setApiKey("key_NxhGDtJqTCu7zesu6NeDXw");
        MainThread mainThread = MainThread.getInstance();
        mainThread.execute();
    }

}
