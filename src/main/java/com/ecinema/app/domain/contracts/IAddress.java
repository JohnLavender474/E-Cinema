package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.UsState;

public interface IAddress {

    String getStreet();
    void setStreet(String street);
    String getCity();
    void setCity(String city);
    UsState getUsState();
    void setUsState(UsState usState);
    String getZipcode();
    void setZipcode(String zipcode);

    default void set(IAddress address) {
        setStreet(address.getStreet());
        setCity(address.getCity());
        setUsState(address.getUsState());
        setZipcode(address.getZipcode());
    }

}
