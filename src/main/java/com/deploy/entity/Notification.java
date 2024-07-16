package com.deploy.entity;

import com.deploy.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "NOTIFICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @Comment("알림 유형")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private NotificationType type;

    @Comment("URL")
    @Column(name = "URL")
    private String url;

    @Comment("APIKEY 또는 TOKEN")
    @Column(name = "API_KEY")
    private String apiKey;

    @Setter
    @OneToOne(mappedBy = "notification", fetch = LAZY)
    private Job job;


    //== 생성 메서드 ==//
    public static Notification createNotification(NotificationType type, String url, String apiKey) {
        Notification notification = new Notification();
        notification.type = type;
        notification.url = url;
        notification.apiKey = apiKey;

        return notification;
    }


    //== 비즈니스 메서드 ==//
    public void changeInfo(NotificationType type, String url, String apiKey) {
        this.type = type;
        this.url = url;
        this.apiKey = apiKey;
    }

}
