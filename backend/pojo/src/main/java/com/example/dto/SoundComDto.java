package com.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SoundComDto implements Serializable {
    private  String model_id;//模型名称
    private  String text;//待合成文本，采用UTF-8编码
    private  String format;//返回音频格式
    private String sampleRate;//返回音频采样率
    private int  volume;//返回音频音量 0-100
    private double rate;//返回音频语速 0.5-2.0
    private double pitch;// 返回音频语调 0.5-2.0
    private  boolean worldTimestampEnabled ;// 是否开启字级别时间戳
    private boolean phonemeTimestampEnabled ;//是否在开启字级别时间戳的基础上，显示音素时间戳

}
