package com.vk.maylanderclick.controllers;

import com.vk.maylanderclick.services.HtmlGifElementService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public final class GifController {

    private final HtmlGifElementService htmlGifElementService;

    @Autowired
    public GifController(HtmlGifElementService htmlGifElementService) {
        this.htmlGifElementService = htmlGifElementService;
    }

    @GetMapping("/exchange-rates-info")
    public String getGif(@RequestParam(name = "currency_code") String currency_code) throws ParseException {
        return htmlGifElementService.getGifElement(currency_code);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> parseException(ParseException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> currencyError(IllegalArgumentException exception){
        return  new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> remoteException(IllegalStateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}