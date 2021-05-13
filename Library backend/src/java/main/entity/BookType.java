package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "bookTypes")
public class BookType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "fine")
    private Long fine;

    @Column(name = "dayCount")
    private Long dayCount;

    @JsonIgnore
    @OneToMany(targetEntity = Book.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "typeId", referencedColumnName = "id")
    private Set<Book> books = new HashSet<>();
}
