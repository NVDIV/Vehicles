package org.example.models;

import lombok.*;
import org.example.repositories.impl.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String login;
    private String password;
    private UserRole role;
    private String rentedVehicleID;
}
