package com.sparta.ssaktium.domain.boards.repository;

import com.sparta.ssaktium.domain.boards.entity.BoardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardSearchRepository extends ElasticsearchRepository<BoardDocument,Long> {

}
