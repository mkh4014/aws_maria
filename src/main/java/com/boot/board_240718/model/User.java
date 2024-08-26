package com.boot.board_240718.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
//Entity가 다른 Entity와 연관관계가 있는 상태에서 둘 다 @Data어노테이션이 붙어있을 경우
//Lombok에서 생성하는 equals, hashCode,혹은 toString 메서드가 서로를 순환 참조하는 문제가 발생
//@Data
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private  long id;
    private String username;
    private String password;
//    private Boolean enabled;
    private Boolean enable;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    Set<Course> likedCourses;
//    private List<Role> roles;
    @JsonIgnore
    private List<Role> roles=new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Board> boards=new ArrayList<>();
}
