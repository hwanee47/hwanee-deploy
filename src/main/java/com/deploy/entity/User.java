package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "USER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "USERNAME")
    private String username;


    //== 생성 메서드 ==//
    public static User createUser(String email, String password, String username) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.username = username;

        return user;
    }


    @Builder
    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    //== 비즈니스 메서드 ==//
    /**
     * 비밀번호 변경
     * @param newPassword (암호화된 새 비밀번호)
     */
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }


    public void changeInfo(String username) {
        this.username = username;
    }
}
