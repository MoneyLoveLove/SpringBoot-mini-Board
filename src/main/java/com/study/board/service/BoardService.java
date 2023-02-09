package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository ;

    //글 작성                          throws Exception : 파일 저장 시 오류가 생길 경우 예외처리
    public void write(Board board, MultipartFile file) throws Exception{

        //파일 경로 저장                           프로젝트 경로 + 파일 위치 경로
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        //식별자        랜덤으로 이름을 만들어줌
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        //파일을 저장할 공간을 만들어줌      경로 , 파일 이름
        File saveFile = new File(projectPath,fileName);

        //파일 저장
        file.transferTo(saveFile);

        //DB에 File 데이터 수정
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        //DB에 데이터 저장
        boardRepository.save(board);
    }

    //게시물 리스트
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    //특정 게시판 불러오기(상세페이지)
    public  Board boardView(Integer id){

        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){

        boardRepository.deleteById(id);

    }




}
