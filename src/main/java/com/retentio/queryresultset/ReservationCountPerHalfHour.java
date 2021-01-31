package com.retentio.queryresultset;

import java.util.Date;

public interface ReservationCountPerHalfHour {
    Date getDate();

    int getCount();

    int hasUserAlreadyReserved();
}
