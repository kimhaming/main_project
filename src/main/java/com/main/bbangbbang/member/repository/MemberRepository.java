package com.main.bbangbbang.member.repository;

import com.main.bbangbbang.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
