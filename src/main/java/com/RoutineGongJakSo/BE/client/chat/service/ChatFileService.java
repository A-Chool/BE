package com.RoutineGongJakSo.BE.client.chat.service;

import com.RoutineGongJakSo.BE.client.chat.model.ChatFile;
import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatFileRepository;
import com.RoutineGongJakSo.BE.client.myPage.S3Validator;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatFileService {
    private final ChatFileRepository chatFileRepository;
    private final S3Validator s3Validator;

    // 파일에 내용 저장하기
    // fileName = roomId
    public String[] fileWriter(List<ChatMessage> chatMessageList, String roomId) throws Exception {

        String path = "/home/ubuntu/test/";
        String fileName = roomId+".txt";
        ObjectMapper mapper = new ObjectMapper();

        String insertStr = mapper.writeValueAsString(chatMessageList);

        File file = new File(path);

        if (!file.exists()) {            // 경로가 없다면 생성합니다. (디렉토리)
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

        return new String[]{path, fileName};
    }

    // 파일 -> List<ChatMessage> 로 읽어오기
    public List<ChatMessage> getMessageFromFile(String roomId, Long prevId) {
        try {
            String result = fileReader(roomId, prevId);
            System.out.println("result = " + result);
            ObjectMapper mapper = new ObjectMapper();

            List<ChatMessage> test = Arrays.asList(mapper.readValue(result, ChatMessage[].class));
            return test;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fileReader(String roomId, Long prevId) throws Exception {
        log.info("roomId : {} ", roomId);
        log.info("prevId : {} ", prevId);
        String targetFileUrl;
        if (prevId == null) {
            List<ChatFile> foundList = chatFileRepository.findByRoomId(roomId);
            targetFileUrl = foundList.get(foundList.size() - 1).getFileUrl();
        } else {
            ChatFile _found = chatFileRepository.findById(prevId).orElseThrow(
                    () -> new IllegalArgumentException("파일없다")
            );
            targetFileUrl = _found.getFileUrl();
        }

        S3Object s3Object = s3Validator.getTxtFile(targetFileUrl);

        String temp = "";

        BufferedReader bf = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
        try {
            temp += bf.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("fileReader 에러 : " + e.toString());
        }
        return temp;
    }
}
