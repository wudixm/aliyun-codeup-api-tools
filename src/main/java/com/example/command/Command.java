package com.example.command;

public interface Command {
    void execute(String[] args);

    String helpMessage();

    String option();
}
