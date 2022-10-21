package com.kcs.guestbook.domain;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//목록보기 응답을 위한 클래스
@Data
public class PageResponseDTO<DTO, EN> {
    private List<DTO> dtoList;

    private int totalPage; //전체 페이지 개수
    private int page;//현재 페이지 번호
    private int size; // 현페이지에 출력되는 데이터 개수
    private int start,end; //페이지 시작번호, 끝번호
    private boolean prev, next; //이전과 다음 존재 여부
    private List<Integer> pageList; //페이지 번호 목록

    //페이지 번호 목록을 만들어주는 메서드
    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1; //현재 페이지 번호와 페이지당 데이터 개수 가져오기
        this.size = pageable.getPageSize(); //

        //임시 종료 페이지 번호
        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        start = tempEnd - 9;
        prev = start>1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start,end)
                .boxed().collect(Collectors.toList());

    }

    //page객체와 변환 함수를 넘겨받아 dtoList를 만들어주는 메서드
    public PageResponseDTO(Page<EN> result, Function<EN,DTO> fn){
        //Page 객체를 순회하면서 fn 함수로 변환한 후 List로 만들기
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        //페이지 번호 목록 만들기
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }
}
