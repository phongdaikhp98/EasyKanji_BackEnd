package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(	name = "verified_token")
@Getter
@Setter
public class VerifiedToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="verified_token")
    private String verifiedToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @OneToOne()
    @JoinColumn(nullable = false, name = "user_id")
    @JsonIgnore
    private User user;

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerifiedToken() {
    }

    public VerifiedToken(User user) {
        this.user = user;
        expiryDate = new Date();
        verifiedToken = UUID.randomUUID().toString();
    }
}
