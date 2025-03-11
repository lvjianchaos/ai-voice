package com.example.utils;


import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.example.constant.PPTConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xslf.usermodel.*;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

@Slf4j
public class PPTUtil {
    public static Integer toImage2007() throws Exception {
        FileInputStream is = new FileInputStream("test.pptx");
        XMLSlideShow ppt = new XMLSlideShow(is);
        is.close();
        Dimension pgsize = ppt.getPageSize();
        System.out.println(pgsize.width + "--" + pgsize.height);

        for (int i = 0; i < ppt.getSlides().size(); i++) {
            try {
                //防止中文乱码
                for (XSLFShape shape : ppt.getSlides().get(i).getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape tsh = (XSLFTextShape) shape;
                        for (XSLFTextParagraph p : tsh) {
                            for (XSLFTextRun r : p) {
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                // render
                ppt.getSlides().get(i).draw(graphics);

                // save the output
                String filename = "img\\" + (i + 1) + ".jpg";

                FileOutputStream out = new FileOutputStream(filename);
                javax.imageio.ImageIO.write(img, "png", out);
                out.close();
            } catch (Exception e) {
                log.info("第" + i + "张ppt转换出错");
            }
        }
        log.info("success");
        //删除下载的test.pptx文件
        FileUtils.deleteQuietly(new File("test.pptx"));

        return ppt.getSlides().size();
    }

    public static void download(String pptUrl) throws IOException {
        URL url = new URL(pptUrl);
        InputStream inputStream = url.openStream();
        FileUtils.copyInputStreamToFile(inputStream, new File("test.pptx"));
    }

    public static void cleanFile(int nums) throws IOException {
        //清理资源，删除img,audios下所有文件
        for (int i = 1; i <= nums; i++) {
            FileUtils.deleteQuietly(new File("img\\" + i + ".jpg"));
            FileUtils.deleteQuietly(new File("audios\\output" + i + ".wav"));
            FileUtils.deleteQuietly(new File("img\\output" + i + ".mp4"));
            FileUtils.deleteQuietly(new File("res22.mp4"));
            FileUtils.deleteQuietly(new File("res.mp4"));
            FileUtils.deleteQuietly(new File("res.wav"));
        }
    }

    public static void createMp4(String mp4SavePath, File img, File audio, int width, int height) throws Exception {
        int second = getSecond(audio);
        System.out.println("视频时长：" + second);
        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(mp4SavePath, width, height);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
// 设置视频为30帧每秒（提高帧率）
        recorder.setFrameRate(30);
// 设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setFormat("mp4");
// 设置比特率为2000k（提高比特率）
        recorder.setVideoBitrate(4000000);
        try {
            recorder.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            //录制一个second秒的视频

            for (int i = 0; i < second; i++) {
                BufferedImage read = ImageIO.read(img);
                //一秒是25帧 所以要记录25次
                for (int j = 0; j < 30; j++) {
                    recorder.record(converter.getFrame(read));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后一定要结束并释放资源
            recorder.stop();
            recorder.release();
        }
        log.info("视频合成成功");


    }

    public static int getSecond(File wavFile) throws Exception {

        // 打开WAV文件
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);

        // 读取fmt块
        int sampleRate = (int) audioInputStream.getFormat().getSampleRate();

        // 读取data块
        int dataSize = audioInputStream.available();

        // 计算时长
        double duration = (double) dataSize / (sampleRate * audioInputStream.getFormat().getFrameSize());

        System.out.println("WAV文件时长：" + duration + "秒");

        // 关闭文件
        audioInputStream.close();
        //四舍五入返回秒
        return (int) Math.round(duration);

    }

    public static void audioGenete(int nums, List<String> urls) throws Exception {
         /*   int i=0;
            String imgUrl = urls.get(i);
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage systemMessage = MultiModalMessage.builder().role(Role.SYSTEM.getValue())
                .content(Arrays.asList(Collections.singletonMap("text", PPTConstant.PPT_AUDIO_GENTE))).build();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("image", imgUrl),

                        Collections.singletonMap("text", "仅生成口播文案，输出的内容是直接可以用于本页PPT演讲的文字，内容连贯通顺口语化；不要输出其他内容，比如标题页、正文页、目录页等"))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                // 此处以qwen-vl-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-vl-plus")
                .message(userMessage)
                .build();
        MultiModalConversationResult result = conv.call(param);
        String text = (String) result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text");
        Generation gen = new Generation();

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(PPTConstant.PPT_TEXT_UPDATE)
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(text)
                .build();
        Message userMsg2 = Message.builder()
                .role(Role.USER.getValue())
                .content("返回的内容是直接用作PPT解说词朗读，不要返回其他信息；字数尽量和原文字数保持一致，并且尽可能控制在80字以内;不要说'今天'、'现在")
                .build();
        GenerationParam param2 = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg, userMsg2))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult result2 = gen.call(param2);
        System.out.println(JsonUtils.toJson(result2));
        text = (String) result2.getOutput().getChoices().get(0).getMessage().getContent();
        System.out.println(text);
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        SpeechSynthesisParam param3 = SpeechSynthesisParam.builder()
                .model("sambert-zhichu-v1")
                .text(text)
                .sampleRate(48000)
                .format(SpeechSynthesisAudioFormat.WAV)
                .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                .build();

        File file = new File("audios\\output" + i + ".wav");
        // 调用call方法，传入param参数，获取合成音频
        ByteBuffer audio = synthesizer.call(param3);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
            System.out.println("synthesis done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/
        List<MultiModalMessage> messages = new ArrayList<>();
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage systemMessage = MultiModalMessage.builder().role(Role.SYSTEM.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("text", PPTConstant.PPT_AUDIO_GENTE))).build();
        messages.add(systemMessage);
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                .model("qwen-vl-max-latest")
                .messages(messages)
                .build();


        for (int i = 1; i <=nums; i++) {
            MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                    .content(Arrays.asList(
                            Collections.singletonMap("image", urls.get(i-1)),
                            Collections.singletonMap("text", "仅生成口播文案，输出的内容是直接可以用于本页PPT演讲的文字，内容连贯通顺口语化；不要输出其他内容，比如标题页等,"))).build();
            messages.add(userMessage);
            param.setMessages((List) messages);
            MultiModalConversationResult result = conv.call(param);
            messages.add(result.getOutput().getChoices().get(0).getMessage());
           String  text = (String) result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text");
            System.out.println(text);
            SpeechSynthesizer synthesizer = new SpeechSynthesizer();
            SpeechSynthesisParam param3 = SpeechSynthesisParam.builder()
                    .model("sambert-zhichu-v1")
                    .text(text)
                    .sampleRate(48000)
                    .format(SpeechSynthesisAudioFormat.WAV)
                    .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                    .build();

            File file = new File("audios\\output" +i + ".wav");
            // 调用call方法，传入param参数，获取合成音频
            ByteBuffer audio = synthesizer.call(param3);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(audio.array());
                System.out.println("synthesis done!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void videoMerge(List<String> videoList, String output)
            throws Exception {


        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoList.get(0));
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, grabber.getImageWidth(),
                grabber.getImageHeight(), 0);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//
        recorder.setFormat("mp4");
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // yuv420p
        int bitrate = grabber.getVideoBitrate();
        if (bitrate == 0) {
            bitrate = grabber.getAudioBitrate();
        }
        recorder.setVideoBitrate(bitrate);

        recorder.start();
        Frame frame = null;
        for (; (frame = grabber.grabImage()) != null; ) {
            // 封装/复用
            recorder.record(frame);
        }

        for (int i = 1; i < videoList.size(); i++) {
            FFmpegFrameGrabber grabberTemp = new FFmpegFrameGrabber(videoList.get(i));
            grabberTemp.start();
            for (; (frame = grabberTemp.grabImage()) != null; ) {
                // 封装/复用
                recorder.record(frame);
            }
            grabberTemp.close();
        }


        recorder.close();
        grabber.close();
    }

    public static boolean mergeAudioAndVideo(String videoPath, String audioPath, String outPut) throws Exception {
        boolean isCreated = true;
        File file = new File(videoPath);
        if (!file.exists()) {
            return false;
        }
        FrameRecorder recorder = null;
        FrameGrabber grabber1 = null;
        FrameGrabber grabber2 = null;
        try {
            //抓取视频帧
            grabber1 = new FFmpegFrameGrabber(videoPath);
            //抓取音频帧
            grabber2 = new FFmpegFrameGrabber(audioPath);
            grabber1.start();
            grabber2.start();
            //创建录制
            recorder = new FFmpegFrameRecorder(outPut,
                    grabber1.getImageWidth(), grabber1.getImageHeight(),
                    grabber2.getAudioChannels());

            recorder.setFormat("mp4");
            recorder.setFrameRate(grabber1.getFrameRate());
            recorder.setSampleRate(grabber2.getSampleRate());
            recorder.start();

            Frame frame1;
            Frame frame2;
            //先录入视频
            while ((frame1 = grabber1.grabFrame()) != null) {
                recorder.record(frame1);
            }
            //然后录入音频
            while ((frame2 = grabber2.grabFrame()) != null) {
                recorder.record(frame2);
            }
            grabber1.stop();
            grabber2.stop();
            recorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (recorder != null) {
                    recorder.release();
                }
                if (grabber1 != null) {
                    grabber1.release();
                }
                if (grabber2 != null) {
                    grabber2.release();
                }
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
        return isCreated;

    }
    public static void audioMerge(List<String> audioFiles,String output) throws UnsupportedAudioFileException, IOException {

        List<AudioInputStream> streams = new ArrayList<>();
        for (int i = 0; i < audioFiles.size(); i++) {
            File file = new File(audioFiles.get(i));
            AudioInputStream stream = readAudioFile(file);
            streams.add(stream);
        }
        AudioInputStream mergedStream = mergeAudioStreams(streams);
        writeAudioFile(mergedStream, new File(output));
        System.out.println("音频文件合并成功");
    }

    public static AudioInputStream readAudioFile(File file) throws UnsupportedAudioFileException, IOException {
        return AudioSystem.getAudioInputStream(file);
    }

    public static boolean checkAudioFormat(AudioFormat format1, AudioFormat format2) {
        return format1.getSampleRate() == format2.getSampleRate() &&
                format1.getSampleSizeInBits() == format2.getSampleSizeInBits() &&
                format1.getChannels() == format2.getChannels();
    }

    public static AudioInputStream mergeAudioStreams(List<AudioInputStream> streams) {
        Enumeration<AudioInputStream> enumStreams = Collections.enumeration(streams);
        return new AudioInputStream(new SequenceInputStream(enumStreams), streams.get(0).getFormat(), AudioSystem.NOT_SPECIFIED);
    }

    public static void writeAudioFile(AudioInputStream mergedStream, File outputFile) throws IOException {
        AudioSystem.write(mergedStream, AudioFileFormat.Type.WAVE, outputFile);
    }


}