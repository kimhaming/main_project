package com.main.bbangbbang.member.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    // 회원 가입 및 로그인은 Spring Security를 이용하여 oauth에서 구현할 예정입니다.
    @GetMapping("/{member-id}")
    public String getMember(@PathVariable("member-id") Long memberId) {
        return "member";
    }

    @PatchMapping("/{member-id}")
    public String patchMember(@PathVariable("member-id") Long memberId) {
        return "patched member";
    }

    @DeleteMapping("/{member-id}")
    public String deleteMember(@PathVariable("member-id") Long memberId) {
        return "deleted member";
    }
}
