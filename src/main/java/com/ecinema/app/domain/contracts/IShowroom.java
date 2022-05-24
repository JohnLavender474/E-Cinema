package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.Letter;

public interface IShowroom {
    Letter getShowroomLetter();
    void setShowroomLetter(Letter showroomLetter);
    Integer getNumberOfRows();
    void setNumberOfRows(Integer numberOfRows);
    Integer getNumberOfSeatsPerRow();
    void setNumberOfSeatsPerRow(Integer numberOfSeatsPerRow);
}
