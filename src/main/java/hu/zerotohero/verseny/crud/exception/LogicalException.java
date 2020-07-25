package hu.zerotohero.verseny.crud.exception;

public abstract class LogicalException extends RuntimeException {

    public LogicalException() {
    }

    public LogicalException(String message) {
        super(message);
    }
}
