package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.repository.EquipmentRepository;
import hu.zerotohero.verseny.crud.util.PropertyCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipment(Long id, Equipment equipment) {
        Equipment toUpdate = equipmentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        PropertyCopier.copyNonNullProperties(equipment, toUpdate);
        return equipmentRepository.save(toUpdate);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment toDelete = equipmentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        equipmentRepository.delete(toDelete);
    }
}
