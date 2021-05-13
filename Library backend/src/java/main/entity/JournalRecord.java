package main.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;


@Data
@Entity
@Table(name = "journal")
public class JournalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bookId")
    private Long bookId;

    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "dateBeg")
    private Date dateBeg;

    @Column(name = "dateEnd")
    private Date dateEnd;

    @Column(name = "dateRet")
    private Date dateRet;
}
