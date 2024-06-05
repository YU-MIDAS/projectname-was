package com.example.demo.bounded_context.solution.controller;

import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.solution.dto.ContributedCreationRequest;
import com.example.demo.bounded_context.solution.dto.KeywordRequest;
import com.example.demo.bounded_context.solution.dto.SolutionResponse;
import com.example.demo.bounded_context.solution.dto.WasteListResponse;
import com.example.demo.bounded_context.solution.service.SolutionService;
import com.example.demo.bounded_context.solution.service.SolutionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solution")
public class SolutionController {
    private final SolutionService solutionService;
    private final SolutionUseCase solutionUseCase;

    @GetMapping("/keyword")
    @Operation(summary = "키워드 검색", description = "키워드를 사용한 솔루션 검색")
    private ResponseEntity<SolutionResponse> searchByKeyword(@RequestBody KeywordRequest request){
        SolutionResponse response = solutionService.search(request.keyword());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리별 필터링", description = "카테고리별 솔루션 검색")
    private ResponseEntity<List<WasteListResponse>> searchByMaterial(@RequestBody KeywordRequest request){
        List<WasteListResponse> response = solutionService.category(request.keyword());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("")
    private ResponseEntity<?> create(@AuthorizationHeader Long accountId,
                                     @RequestBody ContributedCreationRequest request){
        Long id = solutionUseCase.create(accountId, request);
        return ResponseEntity.ok("생성 요청이 완료되었습니다. id = " + id);
    }
}
