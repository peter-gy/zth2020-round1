package hu.zerotohero.verseny.crud.exception;

public class TooManyManagersException extends LogicalException {

    public TooManyManagersException() {
    }

    public TooManyManagersException(String message) {
        super(message);
    }
}
