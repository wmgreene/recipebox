package recipebox.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Optional<Recipe> findById(long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public void delete(Recipe recipe) {

    }

    @Override
    public void deleteById(long id) {
        recipeRepository.deleteById(id);
    }



    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<Recipe> getRecipesByUserId(long userId) {
        return recipeRepository.findByUserId(userId);
    }

}