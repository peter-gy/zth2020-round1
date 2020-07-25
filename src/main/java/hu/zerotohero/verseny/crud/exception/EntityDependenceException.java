package hu.zerotohero.verseny.crud.exception;

public class EntityDependenceException extends BadLogicException {

    public EntityDependenceException() {
    }

    public EntityDependenceException(String message) {
        super(message);
    }
}
