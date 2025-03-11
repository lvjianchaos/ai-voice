package com.example.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SoundVo {
    private Integer id;
    private String name;
    private String fileUrl;
    private String language;
    private  String gender;
    private String ageRange;
    private String isPreset;
    private LocalDateTime uploadedAt;
}
