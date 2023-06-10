package com.akash.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akash.blog.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
