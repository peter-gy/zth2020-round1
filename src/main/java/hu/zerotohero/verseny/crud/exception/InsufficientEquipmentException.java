package hu.zerotohero.verseny.crud.exception;

public class InsufficientEquipmentException extends BadLogicException {

    public InsufficientEquipmentException() {
    }

    public InsufficientEquipmentException(String message) {
        super(message);
    }
}
