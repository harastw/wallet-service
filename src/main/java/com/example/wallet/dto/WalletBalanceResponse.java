package com.example.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.UUID;

public record WalletBalanceResponse(
    UUID walletId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal balance
) {}