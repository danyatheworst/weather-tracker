package com.danyatheworst.location;

import com.danyatheworst.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lat", "lon", "user_Id"})
)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 9, scale = 7)
    private BigDecimal lat;

    @Column(nullable = false, precision = 9, scale = 7)
    private BigDecimal lon;

    @Column(nullable = false)
    private String country;

    @Column
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    public Location(String name, BigDecimal lat, BigDecimal lon, User user, String country, String state) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.user = user;
        this.country = country;
        this.state = state;
    }
}
