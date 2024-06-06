package com.example.demo.bounded_context.questionBoard.service;


import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionBoard.dto.CreateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.PageQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.ReadQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.UpdateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.entity.QuestionBoard;
import com.example.demo.bounded_context.questionBoard.repository.QuestionBoardRepository;
import com.example.demo.bounded_context.questionComment.entity.QuestionComment;
import com.example.demo.bounded_context.questionComment.repository.QuestionCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionCommentRepository questionCommentRepository;

    //c
    @Transactional
    public void create(CreateQuestionBoardDto createQuestionBoardDto, Account writer){
        QuestionBoard questionBoard = QuestionBoard.builder()
                .title(createQuestionBoardDto.getTitle())
                .content(createQuestionBoardDto.getContent())
                .imageUrl(createQuestionBoardDto.getImageUrl())
                .recommend(0)
                .adopted(false)
                .writer(writer)
                .view(0)
                .build();

        questionBoardRepository.save(questionBoard);
    }

    //r
    @Transactional
    public ReadQuestionBoardDto read(Long questionBoardId){
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardId)
                .orElseThrow(() -> new IllegalArgumentException("QUESTIONBOARD_NOT_FOUND"));

        questionBoard.view();
        return new ReadQuestionBoardDto(questionBoard);
    }

    @Transactional
    public List<QuestionBoard> readAll(){
        return questionBoardRepository.findAll();
    }

    //u
    @Transactional
    public void update(Long questionBoardId, UpdateQuestionBoardDto updateQuestionBoardDto){
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardId)
                .orElseThrow(() -> new IllegalArgumentException("QUESTIONBOARD_NOT_FOUND"));

        questionBoard.update(updateQuestionBoardDto);
    }

    //d
    @Transactional
    public void delete(Long questionBoardId){
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardId)
                .orElseThrow(() -> new IllegalArgumentException("QUESTIONBOARD_NOT_FOUND"));

        questionBoardRepository.delete(questionBoard);
    }

    @Transactional //댓글 채택
    public void adopting(Long questionBoardId, Long commentId){
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardId)
                .orElseThrow(() -> new IllegalArgumentException("QUESTIONBOARD_NOT_FOUND"));

        QuestionComment comment = questionCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

        questionBoard.adopting(comment);
    }

    public Page<PageQuestionBoardDto> paging(Pageable pageable, Integer option) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; // 한페이지에 보여줄 글 개수
        Page<QuestionBoard> questionBoardPages;
        if(option==1) {
            questionBoardPages = questionBoardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else if(option==2){
            questionBoardPages = questionBoardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "recommend","createdDate")));
        }
        else{
            questionBoardPages = questionBoardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "view","createdDate")));
        }

        return questionBoardPages.map(
                PageQuestionBoardDto::new);
    }



}
