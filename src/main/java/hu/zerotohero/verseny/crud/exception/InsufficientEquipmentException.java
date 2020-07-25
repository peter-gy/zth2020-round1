package hu.zerotohero.verseny.crud.exception;

public class InsufficientEquipmentException extends RuntimeException {

    public InsufficientEquipmentException() {
    }

    public InsufficientEquipmentException(String message) {
        super(message);
    }
}
