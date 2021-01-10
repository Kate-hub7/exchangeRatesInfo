package com.vk.maylanderclick.services;

import com.vk.maylanderclick.Config;
import com.vk.maylanderclick.ParserUtils;
import com.vk.maylanderclick.feignclients.GiphyClient;
import com.vk.maylanderclick.feignclients.OpenExchangeRatesClient;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
public final class HtmlGifElementService {

    private final GiphyClient giphyClient;
    private final OpenExchangeRatesClient openExchangeRatesClient;
    private final Config config;

    @Autowired
    public HtmlGifElementService(GiphyClient giphyClient, OpenExchangeRatesClient openExchangeRatesClient, Config config) {
        this.giphyClient = giphyClient;
        this.openExchangeRatesClient = openExchangeRatesClient;
        this.config = config;
    }

    public String getGifElement(String currency_code) throws ParseException {
        return "<img src=\"" + ParserUtils.getGifURL(getGifJSON(currency_code)) + "\"/>";
    }

    private String getGifJSON(String currency_code) throws ParseException {
        ResponseEntity<String> response = giphyClient.getGif(config.getApiGiphyKey(),
                isRich(currency_code)? "rich" : "broke",
                (int) Math.floor(Math.random()*100), 1);
        if(!response.getStatusCode().equals(HttpStatus.OK))
            throw new IllegalStateException("The remote server error");

        return response.getBody();
    }

    private boolean isRich(String currency_code) throws ParseException {
        ResponseEntity<String> responseToday = openExchangeRatesClient
                .getTodayRate(config.getApiOpenExchangeRatesKey(),config.getApiBaseCurrency());

        ResponseEntity<String> responseYesterday = openExchangeRatesClient
                .getByDateRate(config.getApiOpenExchangeRatesKey(),
                        LocalDate.now(ZoneOffset.UTC).minusDays(1).toString(), config.getApiBaseCurrency());

        ResponseEntity<String> responseAllCurrency = openExchangeRatesClient.getAllCurrencies();

        if(!responseToday.getStatusCode().equals(HttpStatus.OK)
                || !responseYesterday.getStatusCode().equals(HttpStatus.OK)
                || !responseAllCurrency.getStatusCode().equals(HttpStatus.OK))
            throw new IllegalStateException("The remote server error");

        if(!currency_code.matches("[A-Z]{3}") || !responseAllCurrency.getBody().contains(currency_code))
            throw new IllegalArgumentException("Error currency id");

        Double todayCurrency =  ParserUtils.getRate(responseToday.getBody(), currency_code);
        Double yesterdayCurrency =  ParserUtils.getRate(responseYesterday.getBody(), currency_code);
        return  todayCurrency > yesterdayCurrency;
    }
}
