package com.vk.maylanderclick.feignclients;

import com.vk.maylanderclick.Config;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphy", url= Config.API_GIPHY_LINK)
public interface GiphyClient {

    @GetMapping("/search")
    ResponseEntity<String> getGif(@RequestParam(name = "api_key") String api_key,
                          @RequestParam(name = "q") String q,
                          @RequestParam(name = "offset") Integer offset,
                          @RequestParam(name = "limit") Integer limit);
}
