package com.ecinema.app.services;

import com.ecinema.app.dtos.AddressDto;
import com.ecinema.app.entities.Address;
import com.ecinema.app.utils.Converter;

/**
 * The interface Address service.
 */
public interface AddressService extends AbstractService<Address>, Converter<AddressDto, Long> {}
