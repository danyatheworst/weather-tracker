package com.danyatheworst.location;

import com.danyatheworst.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;

    @Column(nullable = false)
    private String country;

    @Column
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Location(String name, double lat, double lon, User user, String country, String state) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.user = user;
        this.country = country;
        this.state = state;
    }
}
