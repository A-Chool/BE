package com.RoutineGongJakSo.BE.client.chat.service;

import com.RoutineGongJakSo.BE.client.chat.dto.EnterRoomDto;
import com.RoutineGongJakSo.BE.client.chat.model.ChatFile;
import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatFileRepository;
import com.RoutineGongJakSo.BE.client.myPage.S3Validator;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.exception.ErrorCode;
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
    public EnterRoomDto getMessageFromFile(String roomId, Long prevId) {
        try {
            String[] result = fileReader(roomId, prevId);

            ObjectMapper mapper = new ObjectMapper();

            List<ChatMessage> chatMessageList = Arrays.asList(mapper.readValue(result[0], ChatMessage[].class));
            EnterRoomDto enterRoomDto = new EnterRoomDto();
            enterRoomDto.setPrevId(Long.parseLong(result[1]));
            enterRoomDto.setChatMessageList(chatMessageList);
            return enterRoomDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] fileReader(String roomId, Long id) throws IOException{
        log.info("roomId : {} ", roomId);
        log.info("prevId : {} ", id);
        String targetFileUrl;
        String prevId;

        if (id == null) {
            List<ChatFile> foundList = chatFileRepository.findByRoomId(roomId);
            targetFileUrl = foundList.get(foundList.size() - 1).getFileUrl();
            prevId = foundList.get(foundList.size() - 1).getPrevId().toString();
        } else if (id == 0L) {
            throw new CustomException(ErrorCode.NOT_EXIST_CHAT_FILE);
        } else {
            ChatFile _found = chatFileRepository.findByFileIdAndRoomId(id, roomId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_EXIST_CHAT_FILE)
            );
            targetFileUrl = _found.getFileUrl();
            prevId = _found.getPrevId().toString();
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
        String[] rtStArr = new String[2];
        rtStArr[0] = temp;
        rtStArr[1] = prevId;
        return rtStArr;
    }
}
