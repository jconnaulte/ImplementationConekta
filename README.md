# ImplementationConekta

This project is an implementation example in Java of some of the main functionalities  contained in the Conekta REST API.

It uses a basic interface to let a client fill in his informations and then take an order. It was tested with a private sandbox key.

The program will : 
1. Read the user answers and create a "Customer" instance.
2. Read and save the user order.
3. Read the user payment details and create an "Order" instance, depending on the payment type (card, oxxo or spei).
4. Process the order by either showing a recap (card payment) or showing a stub (oxxo and spei).
                          
Due to lack of time this program doesn't : 
- tokenize cards
- process webhooks
- handle well errors and exceptions
