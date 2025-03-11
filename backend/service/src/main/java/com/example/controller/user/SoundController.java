package com.example.controller.user;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.dto.SoundComDto;
import com.example.dto.SoundDto;
import com.example.dto.SoundPageQueryDto;
import com.example.properties.AliOssProperties;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.SoundService;
import com.example.vo.SoundVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static java.lang.System.exit;

@RestController
@RequestMapping("/sound")
@Slf4j
public class SoundController {
    /*
     * 获取声音样本列表
     * */
    @Autowired
    private SoundService soundService;
    @Autowired
    private AliOssProperties aliOssProperties;

    @GetMapping("/list")
    public Result<PageResult> getSoundList(SoundPageQueryDto soundPageQueryDto) {
        log.info("获取声音样本列表");
        PageResult pageResult = soundService.pageQuery(soundPageQueryDto);
        return Result.success(pageResult);
    }

    /*
     * 上传音频文件。*/
    @PostMapping("/upload")
    public Result upload(@RequestBody SoundDto soundDto) throws NoApiKeyException, InputRequiredException {
        log.info("上传音频文件");
        soundService.upload(soundDto);
        return Result.success();
    }

    /*
     * 删除指定声音样本。
     * */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除音频文件");
        soundService.deleteById(id);
        return Result.success();
    }

    /*
     * 修改指定声音样本。
     * */
    @PutMapping("/update")
    public Result update(@RequestBody SoundDto soundDto) {
        log.info("修改音频文件");
        soundService.update(soundDto);
        return Result.success();
    }

    /*
     * 根据id查询指定声音样本。
     * */
    @GetMapping("/{id}")
    public Result<SoundVo> getById(@PathVariable Integer id) {
        log.info("根据id查询指定声音样本");
        SoundVo soundvo = soundService.getById(id);
        return Result.success(soundvo);
    }

    @PostMapping("/syscom")
    public Result voiceSysCom(@RequestBody SoundComDto soundComDto) {
        log.info("合成系统声音");
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                .model(soundComDto.getModel_id())
                .text(soundComDto.getText())
                .sampleRate(48000)
                .format(SpeechSynthesisAudioFormat.WAV)
                .apiKey(aliOssProperties.getApikey())
                .build();

        File file = new File("output.wav");
        // 调用call方法，传入param参数，获取合成音频
        ByteBuffer audio = synthesizer.call(param);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
            System.out.println("synthesis done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

   /*  @PostMapping("com")
   public Result voiceCom(@RequestBody SoundComDto soundComDto) {
        log.info("合成自定义声音");
        // 使用复刻的声音来合成文本为语音
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                .apiKey(aliOssProperties.getApikey())
                .model("cosyvoice-clone-v1")
                .voice(soundComDto.getModel_id())
                .build();
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        ByteBuffer audio = synthesizer.call(param);
        // 保存合成的语音到文件
        System.out.println("TTS RequestId: " + synthesizer.getLastRequestId());
        File file = new File("output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  Result.success();
    }*/
}
