package com.abrenchev.testing.services;

import com.abrenchev.MessageBuilder;
import com.abrenchev.MessageBuilderImpl;
import com.abrenchev.MessageTemplateProvider;
import com.abrenchev.TemplateNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("MessageBuilderImpl")
public class MessageBuilderImplTest {
    @BeforeEach
    public void setUp() {
        System.out.println("setUp called");
    }

    @Test
    @DisplayName("should correctly compose message by provided template")
    public void checkMessage() {
        MessageTemplateProvider templateProvider = spy(MessageTemplateProvider.class);
        given(templateProvider.getMessageTemplate("testTemplate")).willReturn("Best regards, %s, %s");

        MessageBuilder messageBuilder = new MessageBuilderImpl(templateProvider);

        assertThat(messageBuilder.buildMessage("testTemplate", "Petr", "PB"))
                .isEqualTo("Best regards, Petr, PB");
    }

    @Test
    @DisplayName("should call templateProvider only once")
    public void checkTemplateProviderCall() {
        MessageTemplateProvider templateProvider = spy(MessageTemplateProvider.class);
        given(templateProvider.getMessageTemplate("testTemplate")).willReturn("Best regards, %s, %s");

        MessageBuilder messageBuilder = new MessageBuilderImpl(templateProvider);
        messageBuilder.buildMessage("testTemplate", "something", "any");

        verify(templateProvider, times(1)).getMessageTemplate("testTemplate");
    }

    @Test
    public void checkTemplateException() {
        MessageTemplateProvider templateProvider = spy(MessageTemplateProvider.class);
        given(templateProvider.getMessageTemplate("testTemplate")).willReturn(null);

        MessageBuilder messageBuilder = new MessageBuilderImpl(templateProvider);

        assertThatThrownBy(
                () -> messageBuilder.buildMessage("testTemplate", "something", "any")
        ).isInstanceOf(TemplateNotFoundException.class);
    }
}
