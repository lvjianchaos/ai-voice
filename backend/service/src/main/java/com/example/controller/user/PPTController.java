package com.example.controller.user;

import com.example.result.Result;
import com.example.service.PPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ppt")
@Slf4j
public class PPTController {

 @Autowired
 private PPTService pptService;
    @GetMapping("/test2")
    public Result pptToVideo() throws Exception {

       log.info("开始下载ppt");
      String url =  pptService.pptToVideo();
        return Result.success(url);

}
}