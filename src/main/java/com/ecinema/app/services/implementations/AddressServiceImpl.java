package com.ecinema.app.services.implementations;

import com.ecinema.app.dtos.AddressDto;
import com.ecinema.app.entities.Address;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.AddressRepository;
import com.ecinema.app.services.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Address service.
 */
@Service
@Transactional
public class AddressServiceImpl extends AbstractServiceImpl<Address, AddressRepository>
        implements AddressService {

    /**
     * Instantiates a new Address service.
     *
     * @param repository the repository
     */
    public AddressServiceImpl(AddressRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Address address) {}

    @Override
    public AddressDto convert(Long entityId)
            throws NoEntityFoundException {
        Address address = findById(entityId).orElseThrow(
                () -> new NoEntityFoundException("address", "id", entityId));
        AddressDto addressDTO = new AddressDto();
        addressDTO.setId(address.getId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setUsState(address.getUsState());
        addressDTO.setZipcode(address.getZipcode());
        return addressDTO;
    }

}
