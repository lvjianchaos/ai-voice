package com.example.service.impl;

import com.alibaba.dashscope.audio.ttsv2.enrollment.Voice;
import com.alibaba.dashscope.audio.ttsv2.enrollment.VoiceEnrollmentService;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.dto.SoundDto;
import com.example.dto.SoundPageQueryDto;
import com.example.entity.Sound;
import com.example.mapper.SoundMapper;
import com.example.properties.AliOssProperties;
import com.example.result.PageResult;
import com.example.service.SoundService;
import com.example.vo.SoundVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SoundServiceImpl implements SoundService {
    @Autowired
    private SoundMapper soundMapper;
    private static String prefix = "prefix";
    private static String targetModel = "cosyvoice-clone-v1";
     @Autowired
     private AliOssProperties aliOssProperties;

    @Override
    public PageResult pageQuery(SoundPageQueryDto soundPageQueryDto) {
        PageHelper.startPage(soundPageQueryDto.getPage(),soundPageQueryDto.getPageSize());
        Page<Sound> page = soundMapper.pageQuery(soundPageQueryDto);
        long total = page.getTotal();
        List<Sound> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void upload(SoundDto soundDto) throws NoApiKeyException, InputRequiredException {
        VoiceEnrollmentService service = new VoiceEnrollmentService(aliOssProperties.getApikey());
        Voice myVoice = service.createVoice(targetModel, prefix, soundDto.getFileUrl());
        System.out.println("RequestId: " + service.getLastRequestId());
        System.out.println("your voice id is " + myVoice.getVoiceId());
        Sound sound=new Sound();
       sound.setModelId(myVoice.getVoiceId());
        BeanUtils.copyProperties(soundDto,sound);
        sound.setUploadedAt(LocalDateTime.now());
        soundMapper.upload(sound);
    }

    @Override
    public void deleteById(Integer id) {
        soundMapper.deleteById(id);

    }

    @Override
    public void update(SoundDto soundDto) {
        Sound sound=new Sound();
        BeanUtils.copyProperties(soundDto,sound);
        soundMapper.update(sound);
    }

    @Override
    public SoundVo getById(Integer id) {

        Sound sound = soundMapper.getById(id);
        SoundVo soundVo = new SoundVo();
        BeanUtils.copyProperties(sound,soundVo);
        return soundVo;
    }
}
