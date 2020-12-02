package tictactoe;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "game")
public class Game implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    @OneToOne(targetEntity = Account.class)
    private Account acc1;

    @OneToOne(targetEntity = Account.class)
    private Account acc2;

    private int result;
}
