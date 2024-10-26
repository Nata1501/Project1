package by.project.service.entertainment.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(BigDecimal balance, BigDecimal requiredFunds) {
        super("Insufficient funds. Current balance is: " + balance + ", required funds: " + requiredFunds);
    }

}
