package com.dearnewyear.dny.album.domain;

import com.dearnewyear.dny.album.domain.constant.AlbumBackground;
import com.dearnewyear.dny.album.domain.constant.AlbumCover;
import com.dearnewyear.dny.album.domain.constant.AlbumPhrases;
import com.dearnewyear.dny.music.domain.Music;
import com.dearnewyear.dny.user.domain.User;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "ALBUM")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id @Column(name = "album_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;

    @Column(name = "album_cover", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlbumCover albumCover;

    @Column(name = "album_phrases", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlbumPhrases albumPhrases;

    @Column(name = "album_background", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlbumBackground albumBackground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Column(name = "from_name", nullable = false, length = 10)
    private String fromName;

    @Column(name = "to_name", nullable = false, length = 10)
    private String toName;

    @Column(name = "letter", nullable = false, length = 400)
    private String letter;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void updateTo(User toUser) {
        this.toUser = toUser;
    }
}
