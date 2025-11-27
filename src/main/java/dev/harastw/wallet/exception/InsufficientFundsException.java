package com.example.wallet.exception;

import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    private final UUID walletId;
    public InsufficientFundsException(UUID walletId) {
        super("Insufficient funds in wallet: " + walletId);
        this.walletId = walletId;
    }
    public UUID getWalletId() { return walletId; }
}