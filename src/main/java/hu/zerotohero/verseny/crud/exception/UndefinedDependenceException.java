package hu.zerotohero.verseny.crud.exception;

public class UndefinedDependenceException extends RuntimeException {

    public UndefinedDependenceException() {
    }

    public UndefinedDependenceException(String message) {
        super(message);
    }
}
