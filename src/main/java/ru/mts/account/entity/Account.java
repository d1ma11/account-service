package ru.mts.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_accounts", schema = "public")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id_bank_account")
    private Integer id;

    @Column(name = "num_bank_account")
    private BigDecimal accountNumber;

    @Column(name = "amount")
    private BigDecimal amount;
}
