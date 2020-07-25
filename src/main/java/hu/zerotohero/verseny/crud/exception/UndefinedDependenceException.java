package hu.zerotohero.verseny.crud.exception;

public class UndefinedDependenceException extends BadLogicException {

    public UndefinedDependenceException() {
    }

    public UndefinedDependenceException(String message) {
        super(message);
    }
}
