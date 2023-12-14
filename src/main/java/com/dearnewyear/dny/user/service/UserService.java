package com.dearnewyear.dny.user.service;

import com.dearnewyear.dny.album.domain.Album;
import com.dearnewyear.dny.album.repository.AlbumRepository;
import com.dearnewyear.dny.common.error.ErrorCode;
import com.dearnewyear.dny.common.error.exception.CustomException;
import com.dearnewyear.dny.common.jwt.JwtTokenProvider;
import com.dearnewyear.dny.user.domain.User;
import com.dearnewyear.dny.user.dto.UserInfo;
import com.dearnewyear.dny.user.dto.request.LoginRequest;
import com.dearnewyear.dny.user.dto.request.SignupRequest;
import com.dearnewyear.dny.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    public UserInfo signupAndLoginUser(SignupRequest request, HttpServletResponse response) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);

        User user = new User(request);
        userRepository.save(user);

        return getUserInfo(response, user);
    }

    public UserInfo loginUser(LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return getUserInfo(response, user);
    }

    public void renewToken(String refreshToken, HttpServletResponse response) {
        String newAccessToken = jwtTokenProvider.renewAccessToken(refreshToken);
        response.setHeader("Authorization", "Bearer " + newAccessToken);
    }

    private UserInfo getUserInfo(HttpServletResponse response, User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("DNY-Refresh", refreshToken);
        return UserInfo.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .mainBackground(String.valueOf(user.getMainBackground()))
                .mainLp(String.valueOf(user.getMainLp()))
                .playlist(getPlaylist(user))
                .build();
    }

    private List<String> getPlaylist(User user) {
        int PLAYLIST_COUNT = 7;

        List<Album> albums = albumRepository.findByToUser(user);
        Collections.shuffle(albums);

        int count = Math.min(albums.size(), PLAYLIST_COUNT);
        List<Album> selectedAlbums = albums.subList(0, count);

        return selectedAlbums.stream()
                .map(Album::getFromName)
                .collect(Collectors.toList());
    }
}
