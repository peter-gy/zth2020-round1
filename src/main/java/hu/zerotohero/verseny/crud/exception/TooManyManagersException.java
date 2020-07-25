package hu.zerotohero.verseny.crud.exception;

public class TooManyManagersException extends RuntimeException {

    public TooManyManagersException() {
    }

    public TooManyManagersException(String message) {
        super(message);
    }
}
