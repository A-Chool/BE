package com.RoutineGongJakSo.BE.client.chat.service;

import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ChatMessageSaveService {
    private final RedisTemplate<String, Object> redisTemplate;
    // 파일에 내용 저장하기
    public void fileWriter(List<ChatMessage> chatMessageList) throws Exception {
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));

        String path = "/home/ubuntu/test/";
        String fileName = "testName.txt";
//        System.out.println("redisTemplate.getHashValueSerializer() = " + redisTemplate.getHashValueSerializer());
//        String insertStr = (String) redisTemplate.getHashValueSerializer().deserialize(chatMessageList.toString().getBytes(StandardCharsets.UTF_8));

        String insertStr = chatMessageList.toString();

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(chatMessageList);
        System.out.println(jsonInString);

        insertStr = jsonInString;

        File file = new File(path);

        if (!file.exists()) {
            // 경로가 없다면 생성합니다. (디렉토리)
            try {
                file.mkdirs();
            } catch (Exception e) {
                System.out.println("path mkdirs Error : " + e.toString());
            }
        }

        FileWriter writer = null;
        try {
            // 기존 파일의 내용에 이어서 쓰려면 true를
            // 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
            writer = new FileWriter(file + "/" + fileName, false);
            writer.write(insertStr);

            writer.flush();
            System.out.println("file write 완료 ... ");
            System.out.println("file write 내용 : " + insertStr);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fileWriter 에러 : " + e.toString());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String fileReader() throws Exception
    {
        String path = "/home/ubuntu/test/testName.txt";

        File file = new File(path);
        String temp = "";

        try
        {
            Scanner scan = new Scanner(file);
            temp += scan.nextLine();
//            while(scan.hasNextLine())
//            {
//                System.out.println(scan.nextLine());
//                temp += scan.nextLine();
//            }
        }
        catch(FileNotFoundException  e)
        {
            e.printStackTrace();
            System.out.println("fileReader 에러 : " + e.toString());
        }

        return temp;
    }
}
