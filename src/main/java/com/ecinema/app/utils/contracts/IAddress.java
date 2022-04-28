package com.ecinema.app.utils.contracts;

import com.ecinema.app.utils.constants.UsState;

public interface IAddress {
    void setStreet(String street);
    String getStreet();
    void setCity(String city);
    String getCity();
    void setUsState(UsState usState);
    UsState getUsState();
    void setZipcode(String zipcode);
    String getZipcode();
}
