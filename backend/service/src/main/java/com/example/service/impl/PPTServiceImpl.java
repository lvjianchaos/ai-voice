package com.example.service.impl;

import com.example.service.PPTService;
import com.example.utils.AliOssUtil;
import com.example.utils.PPTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PPTServiceImpl implements PPTService {
    @Autowired
    private AliOssUtil aliOssUtil;

    @Override
    public String pptToVideo() throws Exception {
        PPTUtil.download("https://sky-itcast-frankie.oss-cn-beijing.aliyuncs.com/test1.pptx");
        Integer nums = PPTUtil.toImage2007();
        log.info("开始上传图片");
        List<String> urls = new ArrayList<>();
        //遍历img下所有图片,通过aliOssUtil上传
        for (int i = 1; i <= nums; i++) {
            File file = new File("img\\" + i + ".jpg");
            try {
                String url = aliOssUtil.upload(FileUtils.readFileToByteArray(file), UUID.randomUUID() + ".jpg");
                urls.add(url);
                log.info("上传成功");
                log.info("url:{}", url);
            } catch (Exception e) {
                log.error("上传失败");
                log.error("error:{}", e);
            }
        }

        log.info("开始生成文案{}",nums);

            PPTUtil.audioGenete(nums,urls);
        List<String> videoList = new ArrayList<>();
        List<String> audioList = new ArrayList<>();
        for (int i = 1; i <= nums; i++) {
            String img = "img\\" + i + ".jpg";
            String audio = "audios\\output" + i + ".wav";
            String mp4 = "img\\output" + i + ".mp4";
            videoList.add(mp4);
            audioList.add(audio);
            try {
                PPTUtil.createMp4(mp4,new File(img), new File(audio), 1600, 900);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        PPTUtil.videoMerge(videoList,"res.mp4");
        PPTUtil.audioMerge(audioList,"res.wav");
        PPTUtil.mergeAudioAndVideo("res.mp4","res.wav","res22.mp4");

        //将res22.mp4通过aliOssUtil上传
        String url = aliOssUtil.upload(FileUtils.readFileToByteArray(new File("res22.mp4")), UUID.randomUUID() + ".mp4");
        log.info("url:{}", url);
        PPTUtil.cleanFile(nums);
        return url;
    }
}
