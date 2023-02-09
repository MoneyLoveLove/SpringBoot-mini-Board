package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @GetMapping("/board/write") //localhost:8090/board/write
    public String boardWriteForm(){
        return "boardwrite";
    }
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{

         boardService.write(board,file);

         //알림메세지
         model.addAttribute("message","글 작성이 완료되었습니다.");
         model.addAttribute("searchUrl","/board/list");


        return "message";
    }

    @GetMapping("/board/list")
    public String boardLIst(Model model, @PageableDefault(page=0,size=10,sort="id",direction= Sort.Direction.DESC) Pageable pageable){

        Page<Board> list = boardService.boardList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1; //0에서 부터 시작하므로 1을 더해줘야함
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage +5,list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "boardlist";
    }
    @GetMapping("/board/view") //localhost:8090/board/view?id=
    public String boardView(Model model, Integer id){


        model.addAttribute("board",boardService.boardView(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id,Model model){

        boardService.boardDelete(id);

        //알림메세지
        model.addAttribute("message","글 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");

        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,Model model){


        model.addAttribute("board",boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id , Board board,Model model, MultipartFile file)throws Exception{

        //수정할 내용을 가져옴
        Board boardTemp = boardService.boardView(id);

        //원래 내용에 수정한 내용으로 바꿈
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());


        //바꾼 내용 저장
        boardService.write(boardTemp,file);

        //알림메세지
        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");

        return "message";
    }




}
