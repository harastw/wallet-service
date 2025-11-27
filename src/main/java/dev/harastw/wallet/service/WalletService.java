package dev.harastw.wallet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import dev.harastw.wallet.dto.WalletOperationRequest;
import dev.harastw.wallet.exception.InsufficientFundsException;
import dev.harastw.wallet.exception.WalletNotFoundException;
import dev.harastw.wallet.model.Wallet;
import dev.harastw.wallet.repository.WalletRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void processOperation(WalletOperationRequest request) {
        UUID walletId = request.getValletId();
        
        Wallet wallet = walletRepository.findByIdWithLock(walletId).orElseGet(() -> {
            Wallet newWallet = new Wallet(walletId);
            newWallet.setBalance(BigDecimal.ZERO);
            return walletRepository.save(newWallet);
        });

        BigDecimal amount = request.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (request.getOperationType() == WalletOperationRequest.OperationType.WITHDRAW) {
            if (wallet.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(walletId);
            }
            wallet.setBalance(wallet.getBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        } else if (request.getOperationType() == WalletOperationRequest.OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(amount).setScale(2, RoundingMode.HALF_UP));
        }

        walletRepository.save(wallet);
    }

    public BigDecimal getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }
}