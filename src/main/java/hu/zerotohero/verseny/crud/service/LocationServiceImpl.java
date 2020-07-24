package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import hu.zerotohero.verseny.crud.util.PropertyCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        Location toUpdate = locationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        PropertyCopier.copyNonNullProperties(location, toUpdate);
        return locationRepository.save(toUpdate);
    }

    @Override
    public void deleteLocation(Long id) {
        Location toDelete = locationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        locationRepository.delete(toDelete);
    }
}
