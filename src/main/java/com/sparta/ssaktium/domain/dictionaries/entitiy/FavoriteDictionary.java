package com.sparta.ssaktium.domain.dictionaries.entitiy;

import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "favorite_dictionaries")
public class FavoriteDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", nullable = false)
    private Dictionary dictionary;

    private FavoriteDictionary(User user, Dictionary dictionary) {
        this.user = user;
        this.dictionary = dictionary;
    }

    public static FavoriteDictionary addFavoriteDictionary(User user, Dictionary dictionary) {
        return new FavoriteDictionary(user, dictionary);
    }
}
