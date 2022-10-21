package com.kcs.guestbook.controller;

import com.kcs.guestbook.domain.GuestBookDTO;
import com.kcs.guestbook.domain.PageRequestDTO;
import com.kcs.guestbook.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GuestBookController {
    private final GuestBookService guestBookService;

    @GetMapping("/")
    public String main(){
        log.info("list.......");
        return "redirect:/guestbook/list";

    }

    @GetMapping("/guestbook/list")
    public void list(Model model, PageRequestDTO dto){
        log.info("list.......");
        //서비스 메서드 호출
        //result와 dtolist에 DTO의 List가 있고
        //result의 pageList에 페이지 번호의 list가 존재
        model.addAttribute("result",guestBookService.getList(dto));
    }

    @GetMapping("/guestbook/register")
    public void qwe(){

    }

    @PostMapping("/guestbook/register")
    public String register(GuestBookDTO dto, RedirectAttributes redirectAttributes){
        log.info("register set..");
        Long gno = guestBookService.register(dto);
        redirectAttributes.addFlashAttribute("msg",gno+" 등록");
        return "redirect:/guestbook/list";
    }

    @GetMapping("/guestbook/read")
    //ModelAttribute 는 매개변수를 결과 페이지에 넘겨줄 때 사용
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        GuestBookDTO dto = guestBookService.read(gno);
        model.addAttribute("dto",dto);
    }
}
