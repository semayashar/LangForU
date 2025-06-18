package LangForU_DevTeam.LangForU.blog;

import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.coment.Blog_Comment_Service;
import LangForU_DevTeam.LangForU.like.Blog_Like_Service;
import LangForU_DevTeam.LangForU.subscriber.SubscriberService;
import LangForU_DevTeam.LangForU.security.config.WebSecurityConfig;
import LangForU_DevTeam.LangForU.appuser.AppUser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@Import(WebSecurityConfig.class) // Import your security configuration
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private Blog_Comment_Service blogCommentService;
    @MockBean
    private Blog_Like_Service blogLikeService;
    @MockBean
    private SubscriberService subscriberService;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder; // Also mock the password encoder

    @Test
    void viewBlog_ShouldReturnBlogViewWithBlogs() throws Exception {
        Page<Blog> blogPage = new PageImpl<>(Collections.singletonList(new Blog()));
        when(blogService.findBlogsPaginated(anyInt(), anyInt())).thenReturn(blogPage);

        mockMvc.perform(get("/blog"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"))
                .andExpect(model().attributeExists("blogs"));
    }

    @Test
    void blogDetails_ShouldReturnBlogDetailView() throws Exception {
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setAuthor(new AppUser());
        when(blogService.getBlogWithCommentsById(1L)).thenReturn(Optional.of(blog));

        mockMvc.perform(get("/blog/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/blogDetail"))
                .andExpect(model().attributeExists("blog"));
    }

    @Test
    @WithMockUser(username = "admin@user.com", roles = "ADMIN")
    void addBlog_ShouldSaveAndRedirect() throws Exception {
        AppUser admin = new AppUser();
        admin.setEmail("admin@user.com");
        when(appUserService.findByEmail("admin@user.com")).thenReturn(admin);

        mockMvc.perform(post("/blog/add")
                        .param("name", "New Blog")
                        .param("shortExplanation", "A short explanation")
                        .param("blogText", "Some text for the blog.")
                        .with(csrf())) // Add CSRF token for POST requests
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blogs"));
    }

    @Test
    @WithMockUser(username="test@user.com")
    void likeBlog_ShouldLikeAndRedirect() throws Exception {
        Blog blog = new Blog();
        AppUser user = new AppUser();
        when(blogService.findBlogById(1L)).thenReturn(blog);
        when(appUserService.findByEmail("test@user.com")).thenReturn(user);
        when(blogLikeService.findByBlogAndUser(any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/blog/1/like")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/detail/1"));
    }
}