package ru.mts.account.model;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Integer accountId;
    @Positive(message = "Нельзя оперировать отрицательной суммой денег")
    private BigDecimal money;
}
