package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sound implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String fileUrl;
    private String language;
    private  String gender;
    private String ageRange;
    private String isPreset;
    private LocalDateTime uploadedAt;
    private String modelId;
}
