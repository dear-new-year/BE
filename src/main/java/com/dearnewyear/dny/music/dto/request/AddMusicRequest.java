package com.dearnewyear.dny.music.dto.request;

import static com.dearnewyear.dny.music.domain.constant.MusicPatterns.MUSIC_CATEGORY;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.regex.Pattern;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(value = "음악 추가 요청 모델")
public class AddMusicRequest {

    @NotEmpty
    @ApiModelProperty(value = "음악 이름", required = true)
    private final String musicName;

    @NotEmpty
    @ApiModelProperty(value = "아티스트", required = true)
    private final String musicArtist;

    @NotEmpty
    @ApiModelProperty(value = "youtube url id", required = true)
    private final String youtubeUrlId;

    @NotNull
    @ApiModelProperty(value = "카테고리", required = true)
    private final String category;

    @AssertTrue(message = "카테고리가 유효하지 않습니다.")
    public boolean isValidCategory() {
        return Pattern.matches(MUSIC_CATEGORY, category);
    }
}
