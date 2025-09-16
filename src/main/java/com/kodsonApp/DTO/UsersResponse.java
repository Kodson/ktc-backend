package com.kodsonApp.DTO;

import com.kodsonApp.domain.Kodson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private boolean success;
    private List<Kodson> data;
    private int total;
    private String message;
}