package LangForU_DevTeam.LangForU.Data_Layer_Tests;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserRole;
import LangForU_DevTeam.LangForU.blog.Blog;
import LangForU_DevTeam.LangForU.blog.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BlogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlogRepository blogRepository;

    private Blog blog1, blog2;

    @BeforeEach
    public void setUp() {
        AppUser author = new AppUser("author@test.com", "password123", "Author",
                LocalDate.now().minusYears(30), "Male", "Home", AppUserRole.USER, true);
        entityManager.persist(author);

        blog1 = new Blog();
        blog1.setName("Learning Tips"); // КОРЕКЦИЯ: setName -> setTitle
        blog1.setShortExplanation("A short explanation for learning tips.");
        blog1.setBlogText("Some text about learning.");
        blog1.setDate(LocalDate.now());
        blog1.setAuthor(author);
        blog1.setCategories(List.of("Education"));
        blog1.setTags(List.of("tips", "learning"));
        entityManager.persist(blog1);

        blog2 = new Blog();
        blog2.setName("Travel Guide"); // КОРЕКЦИЯ: setName -> setTitle
        blog2.setShortExplanation("A short explanation for travel guide.");
        blog2.setBlogText("Some text about traveling.");
        blog2.setDate(LocalDate.now().minusDays(1));
        blog2.setAuthor(author);
        blog2.setCategories(List.of("Travel"));
        blog2.setTags(List.of("guide", "travel"));
        entityManager.persist(blog2);

        entityManager.flush();
    }

    @Test
    public void whenFindByTitleContainingIgnoreCase_thenReturnPageOfBlogs() { // КОРЕКЦИЯ: findByName -> findByTitle
        Pageable pageable = PageRequest.of(0, 5);
        Page<Blog> page = blogRepository.findByNameContainingIgnoreCase("learning", pageable); // КОРЕКЦИЯ: Method name
        assertThat(page.getContent()).hasSize(1).contains(blog1);
    }

    @Test
    public void whenFindTop5ByOrderByDateDesc_thenReturnRecentBlogs() {
        List<Blog> recent = blogRepository.findTop5ByOrderByDateDesc();
        assertThat(recent).hasSize(2);
        assertThat(recent.get(0)).isEqualTo(blog1);
    }

    @Test
    public void whenFindFirstByIdLessThanOrderByIdDesc_thenReturnPreviousBlog() {
        Optional<Blog> previous = blogRepository.findFirstByIdLessThanOrderByIdDesc(blog2.getId()); // КОРЕКЦИЯ: blog1 -> blog2
        assertThat(previous.isPresent()).isTrue();
        assertThat(previous.get()).isEqualTo(blog1); // КОРЕКЦИЯ: blog2 -> blog1
    }

    @Test
    public void whenFindFirstByIdGreaterThanOrderByIdAsc_thenReturnNextBlog() {
        Optional<Blog> next = blogRepository.findFirstByIdGreaterThanOrderByIdAsc(blog1.getId()); // КОРЕКЦИЯ: blog2 -> blog1
        assertThat(next.isPresent()).isTrue();
        assertThat(next.get()).isEqualTo(blog2); // КОРЕКЦИЯ: blog1 -> blog2
    }

    @Test
    public void whenFindTop5Categories_thenReturnAggregatedCategories() {
        List<Object[]> categories = blogRepository.findTop5Categories();
        assertThat(categories).hasSize(2);
        assertThat(categories.stream().map(o -> o[0].toString())).contains("Education", "Travel");
    }
}