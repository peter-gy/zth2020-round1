package hu.zerotohero.verseny.crud.exception;

public class InsufficientEquipmentException extends LogicalException {

    public InsufficientEquipmentException() {
    }

    public InsufficientEquipmentException(String message) {
        super(message);
    }
}
