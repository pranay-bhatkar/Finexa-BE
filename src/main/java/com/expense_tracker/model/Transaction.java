package com.expense_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // link to user

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Double amount;

    private String notes;

    private String receiptPath;

    private boolean recurring = false;

    private boolean archived = false; // soft delete

    private LocalDate date; // date of transaction

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updateAt;


    @PrePersist
    public void onCreate() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }
}