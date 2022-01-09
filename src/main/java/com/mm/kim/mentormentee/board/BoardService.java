package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.common.util.file.FileInfo;
import com.mm.kim.mentormentee.common.util.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public void persistBoard(List<MultipartFile> fileList, Board board) {
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
        FileUtil fileUtil = new FileUtil();

        for (MultipartFile multipartFile : fileList) {
            if(multipartFile.isEmpty()){
                fileInfos.add(fileUtil.fileUpload(multipartFile));
            }
        }

        board.setFiles(fileInfos);
        boardRepository.save(board);
    }
}
