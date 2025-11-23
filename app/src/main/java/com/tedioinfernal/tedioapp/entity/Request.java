package com.tedioinfernal.tedioapp.entity;

import com.tedioinfernal.tedioapp.enums.ContentType;
import com.tedioinfernal.tedioapp.enums.HttpMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method", nullable = false)
    private HttpMethod httpMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id", nullable = false)
    private Path path;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "body_fields", columnDefinition = "jsonb")
    private Map<String, String> bodyFields;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "header_fields", columnDefinition = "jsonb")
    private Map<String, String> headerFields;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "param_fields", columnDefinition = "jsonb")
    private Map<String, String> paramFields;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_example", columnDefinition = "jsonb")
    private Map<String, Object> requestExample;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_fields", columnDefinition = "jsonb")
    private Map<String, String> responseFields;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_example", columnDefinition = "jsonb")
    private Map<String, Object> responseExample;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
