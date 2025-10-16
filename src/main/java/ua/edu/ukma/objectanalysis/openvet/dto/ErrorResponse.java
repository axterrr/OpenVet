package ua.edu.ukma.objectanalysis.openvet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String name;

    private List<String> messages;

    public ErrorResponse(int status, String name, String message) {
        this.status = status;
        this.name = name;
        this.messages = List.of(message);
    }
}
