package com.person98.mineMonTCG.card.imlp;

import lombok.Getter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class Set {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    
    private final String id;
    private final String displayName;
    private final Date releaseDate;
    private final Date endDate;

    public Set(String id, String displayName, String releaseDate, String endDate) throws ParseException {
        this.id = id;
        this.displayName = displayName;
        this.releaseDate = DATE_FORMAT.parse(releaseDate);
        this.endDate = DATE_FORMAT.parse(endDate);
    }

    public boolean isActive() {
        Date now = new Date();
        return now.after(releaseDate) && now.before(endDate);
    }
} 