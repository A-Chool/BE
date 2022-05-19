package com.RoutineGongJakSo.BE.client.chat.service;

import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ChatFileService {
    // 파일에 내용 저장하기
    public String[] fileWriter(List<ChatMessage> chatMessageList, String roomId, int cnt) throws Exception {

        String path = "/home/ubuntu/test/";
        String fileName = roomId + "_" + cnt + ".txt";
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

    public String fileReader() throws Exception {
        String path = "/home/ubuntu/test/testName.txt";

        File file = new File(path);
        String temp = "";

        try {
            Scanner scan = new Scanner(file);
            temp += scan.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("fileReader 에러 : " + e.toString());
        }

        return temp;
    }
}
