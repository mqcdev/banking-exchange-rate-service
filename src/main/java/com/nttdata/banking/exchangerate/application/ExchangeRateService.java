package com.nttdata.banking.exchangerate.application;

import com.nttdata.banking.exchangerate.dto.ExchangeRatetDto;
import com.nttdata.banking.exchangerate.model.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class ExchangeRateService.
 * ExchangeRate microservice class ExchangeRateService.
 */
public interface ExchangeRateService {

    public Flux<ExchangeRate> findAll();

    public Mono<ExchangeRate> findById(String idExchangeRate);

    public Mono<ExchangeRate> save(ExchangeRatetDto exchangeRateDto);

    public Mono<ExchangeRate> update(ExchangeRatetDto exchangeRateDto, String idExchangeRate);

    public Mono<Void> delete(String idExchangeRate);

    public Mono<ExchangeRate> findByCurrencyType(String currencyType);
}