package com.rsgtechlabs.tradestore.mapper;

import com.rsgtechlabs.tradestore.dto.TradeDTO;
import com.rsgtechlabs.tradestore.model.Trade;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

//DTO design pattern
@Component
public class TradeMapper {
    public static DateTimeFormatter DATE_FORMAT_DD_MM_YYYY = DateTimeFormatter.ofPattern("d/MM/uuuu");
    public Trade toModel(final TradeDTO tradeDTO) {
        return new Trade(tradeDTO.tradeId(), tradeDTO.version(), tradeDTO.counterPartyId(), tradeDTO.bookId(),
                LocalDate.parse(tradeDTO.maturityDate(), DATE_FORMAT_DD_MM_YYYY),
                LocalDate.parse(tradeDTO.createdDate(), DATE_FORMAT_DD_MM_YYYY),
                tradeDTO.expired());
    }
    public TradeDTO toDto(final Trade from) {
        return new TradeDTO(from.tradeId(), from.version(), from.counterPartyId(), from.bookId(),
                from.maturityDate().format(DATE_FORMAT_DD_MM_YYYY),
                from.createdDate().format(DATE_FORMAT_DD_MM_YYYY),
                from.expired());
    }

    public List<TradeDTO> toDto(final List<Trade> from) {
        return from.stream().map(trade -> toDto(trade)).collect(Collectors.toList());
    }
}
