package com.vk.maylanderclick.feignclients;

import com.vk.maylanderclick.Config;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "open-exchange-rates", url = Config.API_OPEN_EXCHANGE_RATES_LINK)
public interface OpenExchangeRatesClient {

    @GetMapping("/latest.json")
    ResponseEntity<String> getTodayRate(@RequestParam(name = "app_id") String app_id,
                                        @PathVariable(name = "base") String base);

    @GetMapping("/historical/{date}.json")
    ResponseEntity<String>  getByDateRate(@RequestParam(name = "app_id") String app_id,
                            @PathVariable(name = "date") String date,
                            @PathVariable(name = "base") String base);

    @GetMapping("/currencies.json")
    ResponseEntity<String> getAllCurrencies();
}
