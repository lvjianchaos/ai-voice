package com.example.service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.dto.SoundDto;
import com.example.dto.SoundPageQueryDto;
import com.example.result.PageResult;
import com.example.vo.SoundVo;

public interface SoundService {

    PageResult pageQuery(SoundPageQueryDto soundPageQueryDto);

    void upload(SoundDto soundDto) throws NoApiKeyException, InputRequiredException;

    void deleteById(Integer id);

    void update(SoundDto soundDto);

    SoundVo getById(Integer id);
}
