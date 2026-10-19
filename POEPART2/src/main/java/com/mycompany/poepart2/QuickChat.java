/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Message workflow, totals and JSON storage, separated from console input/output.
 *
 * @author Student
 */
public class QuickChat {
    public enum SendChoice { SEND, DISCARD, STORE }

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final Path jsonFile;

    public QuickChat() {
        this(Path.of("storedMessages.json"));
    }

    public QuickChat(Path jsonFile) {
        this.jsonFile = jsonFile;
    }

    /** Generates a random ten-digit message ID.
     * @return  */
    public static String generateMessageId() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
    }

    public Message createMessage(int messageNumber, String recipient, String body) {
        return new Message(generateMessageId(), messageNumber, recipient, body);
    }

    /** Applies the user's choice (send, disregard or store) and returns the feedback message.
     * @param message
     * @param choice
     * @return  */
    public String sentMessage(Message message, SendChoice choice) {
        if (message == null) return "Message cannot be empty.";
        if (choice == SendChoice.DISCARD) return "Press 0 to delete the message.";
        if (!message.checkMessageID()) return "Message ID must be no more than ten characters.";
        if (!message.isRecipientValid()) return message.checkRecipientCell();
        if (message.getText().isBlank()) return "Please enter a message.";
        if (!message.isLengthValid()) return "Please enter a message of less than 250 characters.";
        if (choice == SendChoice.STORE) {
            storeMessage(message);
            return "Message successfully stored.";
        }
        sentMessages.add(message);
        return "Message successfully sent.";
    }

    /** Adds the message to the stored list and rewrites the JSON file with every stored message.
     * @param message */
    public void storeMessage(Message message) {
        storedMessages.add(message);
        StringJoiner json = new StringJoiner(",\n", "[\n", "\n]\n");
        for (Message stored : storedMessages) json.add(stored.toJson());
        try {
            Files.writeString(jsonFile, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Could not write " + jsonFile + ": " + ex.getMessage());
        }
    }

    /** Returns the details of every message sent while the program is running.
     * @return  */
    public String printMessages() {
        if (sentMessages.isEmpty()) return "No messages have been sent.";
        StringJoiner out = new StringJoiner("\n\n");
        for (Message sent : sentMessages) out.add(sent.getDetails());
        return out.toString();
    }

    public int returnTotalMessages() { return sentMessages.size(); }
    public List<Message> getSentMessages() { return Collections.unmodifiableList(sentMessages); }
    public List<Message> getStoredMessages() { return Collections.unmodifiableList(storedMessages); }
}
