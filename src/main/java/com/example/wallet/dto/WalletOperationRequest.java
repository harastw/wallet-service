package com.example.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletOperationRequest {
    private UUID valletId;
    private OperationType operationType;
    private BigDecimal amount;

    public UUID getValletId() { return valletId; }
    public void setValletId(UUID valletId) { this.valletId = valletId; }

    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public enum OperationType {
        DEPOSIT, WITHDRAW
    }

}

