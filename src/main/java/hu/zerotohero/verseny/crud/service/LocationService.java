package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Location;

public interface LocationService {

    Location createLocation(Location location);
    Location updateLocation(Long id, Location location);
    void deleteLocation(Long id);

}
