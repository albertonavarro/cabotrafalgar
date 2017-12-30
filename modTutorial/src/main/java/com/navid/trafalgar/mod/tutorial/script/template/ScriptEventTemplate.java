package com.navid.trafalgar.mod.tutorial.script.template;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ScriptEventTemplate {

    List<String> messages = newArrayList();

    public List<String> getMessages() {
        return messages;
    }

    public ScriptEventTemplate setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }
}
