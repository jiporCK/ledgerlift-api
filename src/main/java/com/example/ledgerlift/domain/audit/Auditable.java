package com.example.ledgerlift.domain.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base class for auditable entities.
 *
 * <p>This class is annotated with JPA and Spring Data JPA annotations to capture and store audit
 * information. It includes fields for tracking the creation and modification timestamps and the
 * user responsible for these actions. This class should be extended by entities that require
 * audit information.</p>
 *
 * <p>Annotated with Lombok annotations for getters, setters, and a no-argument constructor.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @Entity
 * public class SomeEntity extends Auditable {
 *     // other fields and methods
 * }
 * }
 * </pre>
 *
 * @see jakarta.persistence.MappedSuperclass
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 * @see org.springframework.data.annotation.CreatedDate
 * @see org.springframework.data.annotation.LastModifiedDate
 * @see org.springframework.data.annotation.CreatedBy
 * @see org.springframework.data.annotation.LastModifiedBy
 * @see lombok.Getter
 * @see lombok.Setter
 * @see lombok.NoArgsConstructor
 *
 * @version 1.0
 *
 * <p>Created by Sreng Chipor on 2024-06-04.</p>
 */
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    /**
     * The timestamp when the entity was created.
     *
     * <p>This field is automatically populated by Spring Data JPA.</p>
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the entity was last modified.
     *
     * <p>This field is automatically populated by Spring Data JPA.</p>
     */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastModifiedAt;

    /**
     * The user who created the entity.
     *
     * <p>This field is automatically populated by Spring Security or a similar mechanism.</p>
     */
    @CreatedBy
    private String createdBy;

    /**
     * The user who last modified the entity.
     *
     * <p>This field is automatically populated by Spring Security or a similar mechanism.</p>
     */
    @LastModifiedBy
    private String lastModifiedBy;
}

