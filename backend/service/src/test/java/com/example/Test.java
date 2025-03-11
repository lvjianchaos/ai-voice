package com.example;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.JsonUtils;
import com.example.constant.PPTConstant;
import com.example.utils.PPTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xslf.usermodel.*;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

import com.alibaba.dashscope.aigc.generation.Generation;
import org.stringtemplate.v4.ST;

import javax.imageio.ImageIO;

@SpringBootTest
@Slf4j
public class Test {


 @org.junit.Test
 public  void test2() throws Exception {
     Generation gen = new Generation();
     Message systemMsg = Message.builder()
             .role(Role.SYSTEM.getValue())
             .content("You are a helpful assistant.")
             .build();
     Message userMsg = Message.builder()
             .role(Role.USER.getValue())
             .content("你是谁？")
             .build();
     GenerationParam param = GenerationParam.builder()
             // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
             .apiKey(System.getenv("DASHSCOPE_API_KEY"))
             // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
             .model("qwen-plus")
             .messages(Arrays.asList(systemMsg, userMsg))
             .resultFormat(GenerationParam.ResultFormat.MESSAGE)
             .build();

    }
    @org.junit.Test
 public void test3() throws Exception {
        int nums = 0;
        String imgUrl = "https://sky-itcast-frankie.oss-cn-beijing.aliyuncs.com/10d492d3-6032-460c-91c2-55c09018bbf4.jpg";
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add(imgUrl);
        imgUrls.add("https://sky-itcast-frankie.oss-cn-beijing.aliyuncs.com/af3a5615-0bfd-4d75-b7de-f69da8da1d00.jpg");
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

        String text = null;
        for (int i = 0; i < 2; i++) {
            MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                    .content(Arrays.asList(
                            Collections.singletonMap("image", imgUrls.get(i)),
                            Collections.singletonMap("text", "仅生成口播文案，输出的内容是直接可以用于本页PPT演讲的文字，内容连贯通顺口语化；不要输出其他内容，比如标题页等,"))).build();
            messages.add(userMessage);
            param.setMessages((List) messages);
            MultiModalConversationResult result = conv.call(param);
            messages.add(result.getOutput().getChoices().get(0).getMessage());
            text = (String) result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text");
            System.out.println(text);

        }


        /* MultiModalConversation conv = new MultiModalConversation();
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
                 .message( userMessage)
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
             .content("text")
             .build();
        Message userMsg2 = Message.builder()
                 .role(Role.USER.getValue())
                 .content("返回的内容是直接用作本页PPT解说词朗读，不要返回其他信息；字数尽量和原文字数保持一致，并且尽可能控制在80字以内;不要说'今天'、'现在")
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
        // System.out.println(JsonUtils.toJson(result2));
         String text2 = (String) result2.getOutput().getChoices().get(0).getMessage().getContent();
         System.out.println(text2);
         SpeechSynthesizer synthesizer = new SpeechSynthesizer();
         SpeechSynthesisParam param3 = SpeechSynthesisParam.builder()
                 .model("sambert-zhichu-v1")
                 .text(text)
                 .sampleRate(48000)
                 .format(SpeechSynthesisAudioFormat.WAV)
                 .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                 .build();

         File file = new File("..\\audios\\output" + num + ".wav");
         // 调用call方法，传入param参数，获取合成音频
         ByteBuffer audio = synthesizer.call(param3);
         try (FileOutputStream fos = new FileOutputStream(file)) {
             fos.write(audio.array());
             System.out.println("synthesis done!");
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
*/
    }
    @org.junit.Test
  public void test4() throws Exception {
        long start = System.currentTimeMillis();
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        SpeechSynthesisParam param3 = SpeechSynthesisParam.builder()
                .model("sambert-zhichu-v1")
                .text("")
                .sampleRate(48000)
                .format(SpeechSynthesisAudioFormat.WAV)
                .apiKey("sk-d681ee3d565048babee7fd6f9dae3e1e")
                .build();

        File file = new File("..\\audios\\output" +2 + ".wav");
        // 调用call方法，传入param参数，获取合成音频
        ByteBuffer audio = synthesizer.call(param3);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
            System.out.println("synthesis done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + ((end - start)/1000));

    }

}