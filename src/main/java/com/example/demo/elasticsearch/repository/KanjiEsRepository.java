package com.example.demo.elasticsearch.repository;


import com.example.demo.elasticsearch.entity.KanjiEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanjiEsRepository extends ElasticsearchRepository<KanjiEs, String> {

}



