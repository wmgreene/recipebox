package recipebox.com;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe save(Recipe recipe);
    Optional<Recipe> findById(long id);
    void delete(Recipe recipe);
    void deleteById(long id);
    long count();
    public List<Recipe> getRecipesByUserId(long userId);

}