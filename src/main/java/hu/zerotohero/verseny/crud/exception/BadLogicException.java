package hu.zerotohero.verseny.crud.exception;

public abstract class BadLogicException extends RuntimeException {

    public BadLogicException() {
    }

    public BadLogicException(String message) {
        super(message);
    }
}
