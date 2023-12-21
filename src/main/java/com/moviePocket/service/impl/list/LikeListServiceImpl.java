package com.moviePocket.service.impl.list;

import com.moviePocket.entities.list.LikeList;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.LikeListRepository;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.inter.list.LikeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LikeListServiceImpl implements LikeListService {
    @Autowired
    private LikeListRepository likeListRepository;
    @Autowired
    private MovieListRepository movieListRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis) {
        ListMovie movieList = movieListRepository.getById(id);
        User user = userRepository.findByEmail(username);
        LikeList likeList = likeListRepository.getByUserAndMovieList(user, movieList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            if (likeList == null) {
                likeListRepository.save(new LikeList(movieList, user, likeOrDis));
            } else if (likeList.isLickOrDis() == likeOrDis) {
                likeListRepository.delete(likeList);
            } else {
                likeList.setLickOrDis(likeOrDis);
                likeListRepository.save(likeList);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Boolean> getLikeOrDis(String username, Long id) {
        ListMovie movieList = movieListRepository.getById(id);
        User user = userRepository.findByEmail(username);
        LikeList likeList = likeListRepository.getByUserAndMovieList(user, movieList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (likeList != null) {
            return ResponseEntity.ok(likeList.isLickOrDis());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Integer[]> getAllLikeAndDisByIdMovieList(Long idMovieList) {
        ListMovie movieList = movieListRepository.getById(idMovieList);
        if (movieList != null) {
            return ResponseEntity.ok(new Integer[]{
                    likeListRepository.countByMovieReviewAndLickOrDisIsTrue(movieList),
                    likeListRepository.countByMovieReviewAndLickOrDisIsFalse(movieList)
            });
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
