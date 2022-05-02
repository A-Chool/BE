package com.RoutineGongJakSo.BE.board;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController

public class PostsController {

    private final PostsService postsService;

    //게시판 리스트 조회
    @GetMapping("/api/user/board")
    public List<PostsResponseDto> findAll(){
        return postsService.findAll();
    }

    //게시글 조회
    @GetMapping("/api/user/{boardId}/posts")
    public PostsResponseDto findById(@PathVariable("boardId") Long id){
        return postsService.findById(id);
    }

    //게시글 작성
    @GetMapping("/api/user/{boardId}/posts")
    public Long save(@PathVariable("boardId") Long boardId,@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    //게시글 수정
    @GetMapping("/api/user/{boardId}/posts/{postId}")
    public Long update(@PathVariable("boardId") Long boardId, @PathVariable ("postId") Long postId, @RequestBody PostsUpdateRequestDto  requestDto){
        return postsService.update(boardid,requestDto);
    }

    //게시글 삭제
    @GetMapping("/api/user/{boardId}/posts/{postId}")
    public Long delete(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId){
        postsService.delete(id);
        return id;
    }
}













//    /**
//     * 게시판 리스트 조회
//     *
//     * @return Response<List<Posts>>
//     */
//    @GetMapping("/api/user/board")
//    private Response<List<Posts>> posts(){
//        return postsService.findAll();
//    }
//
//    /**
//     * 게시글 조회
//     *
//     * @return Response<Posts>
//     */
//    @GetMapping("/api/user/{boardId}/posts)")
//    private Response<Posts> posts(@PathVariable long postsId){
//        return postsService.findAllBypostsId(postsId);
//    }
//
//    /**
//     * 게시글 등록
//     *
//     * @param postsDto
//     * return Response<Posts>
//     */
//    @PostMapping("/api/user/{boardId}/{posts}")
//    private Response<Posts> writePosts(@RequestPart("posts") PostsDto postsDto) {
//        return postsService.savePosts(postsDto);
//    }
//
//
//}
