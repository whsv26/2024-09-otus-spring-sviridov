package me.whsv26.search.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "#{@indexName}")
public class Novel {

    @Id
    private String id;

    @Field(type = FieldType.Float)
    private float rating;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String synopsis;

    @Field(type = FieldType.Keyword)
    private String authorId;

    @Field(type = FieldType.Text)
    private String authorName;

    @Field(type = FieldType.Keyword)
    private List<String> genres;

    @Field(type = FieldType.Keyword)
    private List<String> tags;
}
