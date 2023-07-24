package recipebox.com;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = "recipes")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userName;

    private String email;

    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recipe> recipes = new ArrayList<>();


    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setUser(this);
    }
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.setUser(null);
    }

}