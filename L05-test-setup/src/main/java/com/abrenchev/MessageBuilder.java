package com.abrenchev;

public interface MessageBuilder {
    String buildMessage(String templateName, String text, String signature);
}
