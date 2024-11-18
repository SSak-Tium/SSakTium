package com.sparta.ssaktium.domain.boards.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(indexName = "boards")
public class BoardDocument {

    @Id
    @Field(name = "boards_id", type = FieldType.Long)
    private Long id;

    @Field(name = "title", type = FieldType.Text,analyzer = "custom_nori_analyzer")
    private String title;

    @Field(name = "content", type = FieldType.Text,analyzer = "custom_nori_analyzer")
    private String content;

    @Field(name = "imageUrls", type = FieldType.Text)
    private String imageUrls;
}
