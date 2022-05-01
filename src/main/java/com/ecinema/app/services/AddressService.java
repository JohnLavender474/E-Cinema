package com.ecinema.app.services;

import com.ecinema.app.domain.EntityToDtoConverter;
import com.ecinema.app.domain.dtos.AddressDto;
import com.ecinema.app.domain.entities.Address;

/**
 * The interface Address service.
 */
public interface AddressService extends AbstractService<Address>, EntityToDtoConverter<Address, AddressDto> {}
