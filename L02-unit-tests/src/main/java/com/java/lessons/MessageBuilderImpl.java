package com.java.lessons;

public class MessageBuilderImpl implements MessageBuilder {

    private final MessageTemplateProvider templateProvider;

    public MessageBuilderImpl(MessageTemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    @Override
    public String buildMessage(String templateName, String text, String signature) {
        String messageTemplate = templateProvider.getMessageTemplate(templateName);
        if (messageTemplate == null || messageTemplate.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return String.format(messageTemplate, text, signature);
    }
}
