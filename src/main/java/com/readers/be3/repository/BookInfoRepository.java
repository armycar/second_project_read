package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.BookInfoEntity;

public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long> {
    BookInfoEntity findByBiSeq(Long biSeq);
    public BookInfoEntity findByBiIsbnEquals(String biIsbn);
    public List<BookInfoEntity> findByBiNameContainsOrBiAuthorContainsOrBiPublisherContains(String name, String author, String publisher, Sort sort);
}
