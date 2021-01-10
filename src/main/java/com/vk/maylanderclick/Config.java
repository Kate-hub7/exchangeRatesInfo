package com.vk.maylanderclick;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class Config {
    public static final String API_GIPHY_LINK = "https://api.giphy.com/v1/gifs";
    public static final String API_OPEN_EXCHANGE_RATES_LINK = "https://openexchangerates.org/api";

    @Value("${api.giphy.key}")
    private String apiGiphyKey;

    @Value("${api.openexchangerates.key}")
    private String apiOpenExchangeRatesKey;

    @Value("${api.base.currency}")
    private String apiBaseCurrency;
}
