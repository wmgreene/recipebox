package recipebox.com;
import javax.persistence.*;
import lombok.*;

import java.util.Base64;
import java.util.Collection;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude="user")
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    @Column(name = "`desc`")
    private String desc;

    private String ing;
    private String ins;

    @Lob
    private byte[] img;
    @Transient
    public String getImgBase64() {
        if (img != null) {
            return Base64.getEncoder().encodeToString(img);
        }
        return null;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Recipe(String title, String desc, String ing, String ins, byte[] img) {
        this.title = title;
        this.desc = desc;
        this.ing = ing;
        this.ins = ins;
        this.img = img;
    }
}