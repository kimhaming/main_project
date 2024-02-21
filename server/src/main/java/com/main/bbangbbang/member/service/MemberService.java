package com.main.bbangbbang.member.service;

import com.main.bbangbbang.exception.BusinessLogicException;
import com.main.bbangbbang.exception.ExceptionCode;
import com.main.bbangbbang.member.entity.Member;
import com.main.bbangbbang.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberService {
    private MemberRepository memberRepository;

    @Transactional
    public Member createMember(String email, String nickname, String img) {
        // 중복 여부 확인
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        Member member = new Member();
        member.setEmail(email);
        member.setNickname(nickname);
        member.setImg(img);

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_MEMBER));
    }

    @Transactional(readOnly = true)
    public Member findMemberById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Member updateMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        Member findMember = optionalMember.orElseThrow(() -> new NoSuchElementException("Member not found"));

        return memberRepository.save(findMember);
    }

    @Transactional
    public Member deleteMember(long MemberId) {
        Optional<Member> optionalMember = memberRepository.findById(MemberId);
        Member member = optionalMember.orElseThrow(() -> new NoSuchElementException("Member not found"));

        memberRepository.delete(member);

        return member;
    }
}
