package com.kcs.guestbook.service;

import com.kcs.guestbook.domain.GuestBookDTO;
import com.kcs.guestbook.domain.PageRequestDTO;
import com.kcs.guestbook.domain.PageResponseDTO;
import com.kcs.guestbook.entity.GuestBook;

public interface GuestBookService {

    public PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO);
    //상세보기를 위한 메서드
    public GuestBookDTO read(Long gno);

    default GuestBook dtoToEntity(GuestBookDTO dto){
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }
    default GuestBookDTO entityToDTO(GuestBook entity){
        GuestBookDTO dto = GuestBookDTO.builder()
                        .gno(entity.getGno())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .writer(entity.getWriter())
                        .regDate(entity.getRegDate())
                        .lastdate(entity.getModDate())
                        .build();
        return dto;
    }
    public Long register(GuestBookDTO dto);
}
