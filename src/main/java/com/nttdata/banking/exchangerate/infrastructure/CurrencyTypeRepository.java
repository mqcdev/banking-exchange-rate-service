package com.nttdata.banking.exchangerate.infrastructure;

import com.nttdata.banking.exchangerate.config.WebClientConfig;
import com.nttdata.banking.exchangerate.model.CurrencyType;
import com.nttdata.banking.exchangerate.util.Constants;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class CurrencyTypeRepository {

    @Value("${local.property.host.ms-currency-type}")
    private String propertyHostMsCurrencyType;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = Constants.CURRENCYTYPE_CB, fallbackMethod = "getDefaultCurrencyTypeByType")
    public Mono<CurrencyType> findCurrencyTypeByType(String currencyType) {
        log.info("Inicio----findCurrencyTypeByType-------currencyType: " + currencyType);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsCurrencyType + ":8089")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/currencytype/type/" + currencyType).retrieve()
                        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                        .bodyToMono(CurrencyType.class)
                );
    }

    public Mono<CurrencyType> getDefaultCurrencyTypeByType(String currencyType, Exception e) {
        log.info("Inicio----getDefaultCurrencyTypeByType-------currencyType: " + currencyType);
        return Mono.empty();
    }
}