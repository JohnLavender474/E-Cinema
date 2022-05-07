package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.Letter;

public interface IShowroom {
    Letter getShowroomLetter();
    Integer getNumberOfRows();
    Integer getNumberOfSeatsPerRow();
}
