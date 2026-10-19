/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart2;

import java.util.Scanner;
/**
 * Console application: registration, login and the QuickChat menu.
 *
 * @author Student
 */
public class POEPART2 {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Registration and Login ===");
        Login login = register();
        if (login == null || !authenticate(login)) {
            System.out.println("Application closed.");
            return;
        }
        System.out.println("Welcome to QuickChat.");
        int limit = askMessageLimit();
        runMenu(new QuickChat(), limit);
    }

    private static Login register() {
        System.out.print("Enter first name: "); String first = SCANNER.nextLine().trim();
        System.out.print("Enter last name: "); String last = SCANNER.nextLine().trim();
        System.out.print("Enter username "
                    + "(must contain an underscore and be no more than 5 characters): ");
        System.out.print("Enter username: "); String user = SCANNER.nextLine();
         System.out.print("Enter password "
                    + "(at least 8 characters, uppercase letter, number, and special character): ");
        System.out.print("Enter password: "); String pass = SCANNER.nextLine();
        System.out.print("Enter South African cell phone number (+27XXXXXXXXX): "); String phone = SCANNER.nextLine();
        Login login = new Login(first, last, user, pass, phone);
        String result = login.registerUser();
        System.out.println(result);
        return "User successfully registered.".equals(result) ? login : null;
    }

    private static boolean authenticate(Login login) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter username: "); String username = SCANNER.nextLine();
            System.out.print("Enter password: "); String password = SCANNER.nextLine();
            if (login.loginUser(username, password)) {
                System.out.println(login.returnLoginStatus());
                return true;
            }
            if (attempt < 3) System.out.println("Username or password incorrect, please try again.");
        }
        System.out.println("Blocked: maximum login attempts reached.");
        return false;
    }

    /** Asks, once at start-up, how many messages the user wants to enter. */
    private static int askMessageLimit() {
        while (true) {
            System.out.print("How many messages do you wish to enter? ");
            try {
                int limit = Integer.parseInt(SCANNER.nextLine().trim());
                if (limit >= 1) return limit;
                System.out.println("Enter at least one message.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    /** Menu loop: runs until the user selects option 3 (Quit). */
    private static void runMenu(QuickChat chat, int limit) {
        boolean running = true;
        boolean messagesEntered = false;
        while (running) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit");
            System.out.print("Choose an option: ");
            String option = SCANNER.nextLine().trim();
            switch (option) {
                case "1" -> {
                    if (messagesEntered) {
                        System.out.println("You have already entered all " + limit + " messages.");
                    } else {
                        sendMessages(chat, limit);
                        messagesEntered = true;
                    }
                }
                case "2" -> System.out.println("Coming Soon.");
                case "3" -> { running = false; System.out.println("Goodbye."); }
                default -> System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }
    }

    /** Captures exactly the number of messages the user chose; the loop counter is the message number. */
    private static void sendMessages(QuickChat chat, int limit) {
        for (int i = 0; i < limit; i++) {
            System.out.println("\nMessage " + (i + 1) + " of " + limit);
            System.out.print("Recipient number (+ followed by up to 12 digits): "); String recipient = SCANNER.nextLine().trim();
            System.out.print("Message (maximum 250 characters): "); String body = SCANNER.nextLine();

            Message message = chat.createMessage(i, recipient, body);
            System.out.println(message.checkRecipientCell());
            System.out.println(message.checkMessageLength());
            System.out.println(message.describeMessageId());
            System.out.println("Message Hash: " + message.createMessageHash());

            String result = chat.sentMessage(message, readChoice());
            System.out.println(result);
            if (result.equals("Message successfully sent.")) System.out.println(message.getDetails());
        }
        System.out.println("\nTotal messages sent: " + chat.returnTotalMessages());
    }

    /** Keeps asking until the user picks send, disregard or store. */
    private static QuickChat.SendChoice readChoice() {
        while (true) {
            System.out.println("1) Send Message\n2) Disregard Message\n3) Store Message to send later");
            System.out.print("Choose an option: ");
            switch (SCANNER.nextLine().trim()) {
                case "1" -> {
                    return QuickChat.SendChoice.SEND;
                }
                case "2" -> {
                    return QuickChat.SendChoice.DISCARD;
                }
                case "3" -> {
                    return QuickChat.SendChoice.STORE;
                }
                default -> System.out.println("Invalid message option. Please choose 1, 2 or 3.");
            }
        }
    }
}
