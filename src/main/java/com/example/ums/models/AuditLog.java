package com.example.ums.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuditLog {
    @Id
    private String id;

    private String userId;
    private String action;
    private LocalDateTime timestamp;
}
