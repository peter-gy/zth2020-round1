package hu.zerotohero.verseny.crud.exception;

public class TooManyManagersException extends BadLogicException {

    public TooManyManagersException() {
    }

    public TooManyManagersException(String message) {
        super(message);
    }
}
