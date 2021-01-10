package com.vk.maylanderclick;

import com.vk.maylanderclick.feignclients.GiphyClient;
import com.vk.maylanderclick.feignclients.OpenExchangeRatesClient;
import com.vk.maylanderclick.services.HtmlGifElementService;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HtmlGifElementServiceTests {

    @MockBean
    GiphyClient giphyClient;
    @MockBean
    OpenExchangeRatesClient openExchangeRatesClient;
    @Autowired
    Config config;

    @Before
    public void before(){
        Mockito.when(openExchangeRatesClient
                .getTodayRate(any(),any()))
                .thenReturn(new ResponseEntity<>(TestsData.OPEN_EXCHANGE_RATES_API_TODAY_BODY, HttpStatus.OK));

        Mockito.when(openExchangeRatesClient
                .getByDateRate(any(), anyString(), any())).
                thenReturn(new ResponseEntity<>(TestsData.OPEN_EXCHANGE_RATES_API_YESTERDAY_BODY, HttpStatus.OK));

        Mockito.when(openExchangeRatesClient.getAllCurrencies()).thenReturn(new ResponseEntity<>(TestsData.OPEN_EXCHANGE_RATES_API_ALL_CURRENCY_BODY,
                HttpStatus.OK));
    }
    @SneakyThrows
    @Test
    public void correctBrokeGif(){
        Mockito.when(giphyClient.getGif(any(), eq("broke"), anyInt(), eq(1)))
                .thenReturn(new ResponseEntity<>(TestsData.GIPHY_API_BODY, HttpStatus.OK));

        HtmlGifElementService htmlGifElementService = new HtmlGifElementService(giphyClient, openExchangeRatesClient, config);

        Assert.assertEquals(htmlGifElementService.getGifElement("AED"), TestsData.GIF_SERVICE_RESULT );
    }

    @SneakyThrows
    @Test
    public void correctRichGif(){
        Mockito.when(giphyClient.getGif(any(), eq("rich"), anyInt(), eq(1)))
                .thenReturn(new ResponseEntity<>(TestsData.GIPHY_API_BODY, HttpStatus.OK));

        HtmlGifElementService htmlGifElementService = new HtmlGifElementService(giphyClient, openExchangeRatesClient, config);

        Assert.assertEquals(htmlGifElementService.getGifElement("ZMW"), TestsData.GIF_SERVICE_RESULT);
    }

    @SneakyThrows
    @Test
    public void remoteError(){
        Mockito.when(giphyClient.getGif(any(), eq("rich"), anyInt(), eq(1)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR));

        HtmlGifElementService htmlGifElementService = new HtmlGifElementService(giphyClient, openExchangeRatesClient, config);

        Assert.assertThrows(IllegalStateException.class,
                () -> htmlGifElementService.getGifElement("ZMW"));
    }

    @SneakyThrows
    @Test
    public void invalidCurrency(){
        Mockito.when(giphyClient.getGif(any(), eq("rich"), anyInt(), eq(1)))
                .thenReturn(new ResponseEntity<>(TestsData.GIPHY_API_BODY, HttpStatus.OK));

        HtmlGifElementService htmlGifElementService = new HtmlGifElementService(giphyClient, openExchangeRatesClient, config);

        Assert.assertThrows(IllegalArgumentException.class,
                ()->htmlGifElementService.getGifElement("ZMWD"));
    }
}
