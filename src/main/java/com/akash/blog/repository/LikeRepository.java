package com.akash.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akash.blog.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {

}
