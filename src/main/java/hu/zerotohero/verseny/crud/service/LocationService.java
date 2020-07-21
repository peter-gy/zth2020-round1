package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location location) {
        Location toUpdate = locationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        toUpdate.setName(location.getName());
        toUpdate.setAddress(location.getAddress());
        return locationRepository.save(toUpdate);
    }

    public void deleteLocation(Long id) {
        Location toDelete = locationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        locationRepository.delete(toDelete);
    }
}
