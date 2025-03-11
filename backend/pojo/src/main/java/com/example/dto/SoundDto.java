package com.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SoundDto implements Serializable {
    private Integer id;
    private String name;
    private String fileUrl;
    private String language;
    private  String gender;
    private String ageRange;
    private String isPreset;
    private String model_id;
}
