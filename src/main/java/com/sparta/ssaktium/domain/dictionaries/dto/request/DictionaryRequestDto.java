package com.sparta.ssaktium.domain.dictionaries.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DictionaryRequestDto {

    @Schema(description = "제목", example = "선인장")
    private String title;

    @Schema(description = "내용", example = "선인장은 사막에서 잘 자랍니다.")
    private String content;
}
