/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart2;

/**
 *
 * @author Student
 */


import java.util.Locale;
import java.util.regex.Pattern;

/** Represents and validates a QuickChat message. */

public class Message {
    public static final int MAX_MESSAGE_LENGTH = 250;

    // Regex: a message ID holds between one and ten characters.
    private static final Pattern MESSAGE_ID_PATTERN = Pattern.compile("^.{1,10}$");
    // Regex: a recipient starts with the international "+" code followed by 1-12 digits.
    private static final Pattern RECIPIENT_PATTERN = Pattern.compile("^\\+\\d{1,12}$");

    private final String messageId;
    private final int messageNumber;   // loop counter of the message, starting at 0
    private final String recipient;
    private final String text;
    private final String hash;

    public Message(String messageId, int messageNumber, String recipient, String text) {
        this.messageId = messageId == null ? "" : messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient;
        this.text = text == null ? "" : text;
        this.hash = buildHash();
    }

    /** Ensures the message ID is not more than ten characters.
     * @return  */
    public boolean checkMessageID() {
        return MESSAGE_ID_PATTERN.matcher(messageId).matches();
    }

    public String describeMessageId() {
        return "Message ID generated: " + messageId;
    }

    /** True when the recipient starts with an international code and is correctly sized.
     * @return  */
    public boolean isRecipientValid() {
        return RECIPIENT_PATTERN.matcher(recipient).matches();
    }

    public String checkRecipientCell() {
        return isRecipientValid()
                ? "Cell phone number successfully captured."
                : "Cell phone number is incorrectly formatted or does not contain an international code. "
                  + "Please correct the number and try again.";
    }

    /** True when the message is not blank and does not exceed 250 characters.
     * @return  */
    public boolean isLengthValid() {
        return !text.isBlank() && text.length() <= MAX_MESSAGE_LENGTH;
    }

    public String checkMessageLength() {
        int excess = text.length() - MAX_MESSAGE_LENGTH;
        return excess > 0
                ? "Message exceeds 250 characters by " + excess + "; please reduce the size."
                : "Message ready to send.";
    }

    /** Returns the hash, e.g. 00:0:HITONIGHT.
     * @return  */
    public String createMessageHash() {
        return hash;
    }

    /** First two ID characters : message number : first and last word, in capitals. */
    private String buildHash() {
        String[] words = text.trim().split("\\s+");
        String first = stripPunctuation(words[0]);
        String last = stripPunctuation(words[words.length - 1]);
        String prefix = messageId.substring(0, Math.min(2, messageId.length()));
        return (prefix + ":" + messageNumber + ":" + first + last).toUpperCase(Locale.ROOT);
    }

    /** Removes punctuation so "tonight?" becomes "tonight". */
    private static String stripPunctuation(String word) {
        return word.replaceAll("[^\\p{L}\\p{N}]", "");
    }

    /** Full details in the required order: ID, hash, recipient, message.
     * @return  */
    public String getDetails() {
        return "Message ID: " + messageId + "\nMessage Hash: " + hash
                + "\nRecipient: " + recipient + "\nMessage: " + text;
    }

    /**
     * Converts the message to a JSON object. String escaping follows RFC 8259, section 7
     * (https://www.rfc-editor.org/rfc/rfc8259#section-7).
     * @return 
     */
    public String toJson() {
        return "  {\n"
                + "    \"messageId\": \"" + escapeJson(messageId) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(hash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(text) + "\"\n"
                + "  }";
    }

    private static String escapeJson(String value) {
        StringBuilder out = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) out.append(String.format("\\u%04x", (int) c));
                    else out.append(c);
                }
            }
        }
        return out.toString();
    }

    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public String getHash() { return hash; }
}
