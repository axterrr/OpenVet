package ua.edu.ukma.objectanalysis.openvet.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BaseException extends RuntimeException {

    private final int status;
    private final String name;
    private final List<String> messages;

    public BaseException(int status, String name, List<String> messages) {
        super(messages.toString());
        this.status = status;
        this.name = name;
        this.messages = messages;
    }

    public BaseException(int status, String name, String message) {
        super(message);
        this.status = status;
        this.name = name;
        this.messages = List.of(message);
    }
}
