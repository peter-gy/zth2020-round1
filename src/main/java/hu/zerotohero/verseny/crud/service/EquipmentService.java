package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Equipment;

public interface EquipmentService {

    Equipment createEquipment(Equipment equipment);
    Equipment updateEquipment(Long id, Equipment equipment);
    void deleteEquipment(Long id);

}
