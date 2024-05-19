package com.rsgtechlabs.tradestore.model;

import java.time.LocalDate;
import java.util.Objects;

public record Trade(String tradeId, int version, String counterPartyId, String bookId, LocalDate maturityDate,
                    LocalDate createdDate, String expired) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return version == trade.version && Objects.equals(tradeId, trade.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, version);
    }
}
