package LangForU_DevTeam.LangForU.blog;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import LangForU_DevTeam.LangForU.appuser.AppUserService;
import LangForU_DevTeam.LangForU.coment.Blog_Comment;
import LangForU_DevTeam.LangForU.coment.Blog_Comment_Service;
import LangForU_DevTeam.LangForU.like.Blog_Like;
import LangForU_DevTeam.LangForU.like.Blog_Like_Service;
import LangForU_DevTeam.LangForU.subscriber.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * Контролер, който управлява всички HTTP заявки свързани с блог функционалността.
 * Включва показване на публикации, детайли, коментари, харесвания, както и административни действия.
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

    private static final int PAGE_SIZE = 5; // Константа за брой публикации на страница.

    //<editor-fold desc="Dependencies">
    private final AppUserService userService; // Забележка: Двойно инжектиране на AppUserService. Едното е излишно.
    private final AppUserService appUserService;
    private final BlogService blogService;
    private final Blog_Comment_Service blogCommentService;
    private final Blog_Like_Service blogLikeService;
    private final SubscriberService subscriberService;
    //</editor-fold>

    /**
     * Конструктор за инжектиране на зависимости (Dependency Injection).
     */
    @Autowired
    public BlogController(AppUserService userService, AppUserService appUserService, BlogService blogService,
                          Blog_Comment_Service blogCommentService, Blog_Like_Service blogLikeService, SubscriberService subscriberService) {
        this.userService = userService;
        this.appUserService = appUserService;
        this.blogService = blogService;
        this.blogCommentService = blogCommentService;
        this.blogLikeService = blogLikeService;
        this.subscriberService = subscriberService;
    }

    /**
     * Метод, който добавя общи атрибути към модела за всяка заявка в този контролер.
     * Използва се за показване на топ тагове, скорошни постове и категории в страничната лента на блога.
     * @param model Моделът, към който се добавят атрибутите.
     */
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("tags", blogService.getTop5Tags());
        model.addAttribute("recentPosts", blogService.getRecentPosts(5));
        model.addAttribute("categories", blogService.getTop5Categories());
    }

    /**
     * Показва главната страница на блога с пагиниран списък на публикациите.
     * @param currentPage Текущият номер на страницата (по подразбиране 0).
     * @param model Модел за подаване на данни към изгледа.
     * @return Името на шаблона 'blog'.
     */
    @GetMapping("")
    public String getAllBlogs(@RequestParam(value = "page", defaultValue = "0") int currentPage, Model model) {
        Page<Blog> blogPage = blogService.findBlogsPaginated(currentPage, PAGE_SIZE);

        // Ако няма блогове, показва съобщение.
        if (blogPage.isEmpty()) {
            model.addAttribute("message", "Очаквайте нови блогове скоро");
            model.addAttribute("blogs", Collections.emptyList());
            model.addAttribute("totalPages", 0);
            model.addAttribute("currentPage", 0);
            addCommonAttributes(model); // Добавя общите атрибути дори при празна страница.
            return "blog";
        }

        // Валидация на номера на страницата.
        if (currentPage < 0) {
            return "redirect:/blog?page=0";
        } else if (currentPage >= blogPage.getTotalPages()) {
            return "redirect:/blog?page=" + (blogPage.getTotalPages() - 1);
        }

        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);

        return "blog";
    }

    /**
     * Показва детайлната страница на една блог публикация.
     * @param id ID на публикацията.
     * @param principal Обект, представляващ текущо логнатия потребител.
     * @param model Модел за подаване на данни към изгледа.
     * @return Името на шаблона 'blog/blogDetail' или 'error/404' ако публикацията не е намерена.
     */
    @GetMapping("/detail/{id}")
    public String getBlogDetail(@PathVariable("id") Long id, Principal principal, Model model) {
        Optional<Blog> blogOptional = blogService.getBlogWithCommentsById(id);

        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            boolean isLikedByCurrentUser = false;

            // Проверява дали потребителят е харесал публикацията.
            if (principal != null) {
                AppUser currentUser = userService.findByEmail(principal.getName());
                isLikedByCurrentUser = blogLikeService.findByBlogAndUser(blog, currentUser).isPresent();
            }

            model.addAttribute("blog", blog);
            model.addAttribute("likesCount", blogLikeService.countLikesByBlogId(id));
            model.addAttribute("comments", blog.getComments());
            model.addAttribute("previousBlog", blogService.getPreviousBlogById(id));
            model.addAttribute("nextBlog", blogService.getNextBlogById(id));
            model.addAttribute("isLikedByCurrentUser", isLikedByCurrentUser);
            model.addAttribute("userName", principal != null ? userService.getUserNameByEmail(principal.getName()) : null);

            return "blog/blogDetail";
        } else {
            return "notifications/error";
        }
    }

    /**
     * Обработва публикуването на нов коментар към публикация.
     * @param blogId ID на публикацията, която се коментира.
     * @param commentText Текстът на коментара.
     * @param principal Обект на текущия потребител.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Пренасочване към детайлната страница на публикацията.
     */
    @PostMapping("/{id}/comment")
    public String postComment(@PathVariable("id") Long blogId, @RequestParam("commentText") String commentText,
                              Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Трябва да сте влезли, за да публикувате коментар.");
            return "redirect:/login";
        }

        Blog blog = blogService.findBlogById(blogId);
        if (blog == null) {
            redirectAttributes.addFlashAttribute("error", "Блогът не е намерен.");
            return "redirect:/blog";
        }

        Blog_Comment comment = new Blog_Comment();
        comment.setBlog(blog);
        comment.setUser(userService.findByEmail(principal.getName()));
        comment.setCommentText(commentText);
        comment.setCommentedAt(LocalDateTime.now());
        blogCommentService.save(comment);

        redirectAttributes.addFlashAttribute("success", "Коментарът е публикуван успешно.");
        return "redirect:/blog/detail/" + blogId;
    }

    /**
     * Обработва харесването или премахването на харесване от публикация (toggle).
     * @param blogId ID на публикацията.
     * @param principal Обект на текущия потребител.
     * @param redirectAttributes Атрибути за съобщения след пренасочване.
     * @return Пренасочване към детайлната страница на публикацията.
     */
    @PostMapping("/{id}/like")
    public String likeBlog(@PathVariable("id") Long blogId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Трябва да сте влезли, за да харесате публикация.");
            return "redirect:/login";
        }

        AppUser currentUser = userService.findByEmail(principal.getName());
        Blog blog = blogService.findBlogById(blogId);

        if (blog == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Блогът не е намерен.");
            return "redirect:/blog";
        }

        Optional<Blog_Like> existingLike = blogLikeService.findByBlogAndUser(blog, currentUser);
        if (existingLike.isPresent()) {
            blogLikeService.delete(existingLike.get()); // Премахване на харесване
            redirectAttributes.addFlashAttribute("message", "Отхаресахте тази публикация.");
        } else {
            Blog_Like newLike = new Blog_Like(null, blog, currentUser, LocalDateTime.now());
            blogLikeService.save(newLike); // Добавяне на харесване
            redirectAttributes.addFlashAttribute("message", "Харесахте тази публикация.");
        }
        return "redirect:/blog/detail/" + blogId;
    }

    /**
     * Търси в блога по ключова дума и показва резултатите пагинирано.
     * @param query Ключовата дума за търсене.
     * @param currentPage Номер на текущата страница.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'blog'.
     */
    @GetMapping("/search")
    public String searchBlogs(@RequestParam("query") String query, @RequestParam(value = "page", defaultValue = "0") int currentPage, Model model) {
        Page<Blog> blogPage = blogService.searchBlogs(query, currentPage, PAGE_SIZE);
        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("query", query);
        return "blog";
    }

    /**
     * Показва пагиниран списък с публикации от определена категория.
     * @param categoryName Името на категорията.
     * @param currentPage Номер на текущата страница.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'blog'.
     */
    @GetMapping("/category/{categoryName}")
    public String getBlogsByCategory(@PathVariable("categoryName") String categoryName, @RequestParam(value = "page", defaultValue = "0") int currentPage, Model model) {
        Page<Blog> blogPage = blogService.findBlogsByCategoryPaginated(categoryName, currentPage, PAGE_SIZE);
        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("categoryName", categoryName);
        return "blog";
    }

    /**
     * Показва пагиниран списък с публикации, маркирани с определен таг.
     * @param tagName Името на тага.
     * @param currentPage Номер на текущата страница.
     * @param model Модел за подаване на данни.
     * @return Името на шаблона 'blog'.
     */
    @GetMapping("/tag/{tagName}")
    public String getBlogsByTag(@PathVariable("tagName") String tagName, @RequestParam(value = "page", defaultValue = "0") int currentPage, Model model) {
        Page<Blog> blogPage = blogService.findBlogsByTagPaginated(tagName, currentPage, PAGE_SIZE);
        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("tagName", tagName);
        return "blog";
    }

    /**
     * Обработва абониране на потребител за блога.
     * @param email Имейлът на потребителя за абонамент.
     * @return Пренасочване към главната страница на блога.
     */
    @PostMapping("/subscribe")
    @ResponseBody
    public ResponseEntity<String> subscribeToBlog(@RequestParam("email") String email) {
        boolean subscribed = subscriberService.subscribe(email);
        if (subscribed) {
            return ResponseEntity.ok("Успешен абонамент!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Вече сте абонирани!");
        }
    }


    /**
     * Обработва създаването на нова блог публикация (от администратор).
     * @param blog Обектът Blog, създаден от формата.
     * @return Пренасочване към списъка с блогове в административния панел.
     */
    @PostMapping("/add")
    public String addBlog(@ModelAttribute("blog") Blog blog) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login"; // Изисква логнат потребител.
        }
        AppUser appUser = appUserService.findByEmail(authentication.getName());
        blog.setAuthor(appUser);
        blog.setDate(LocalDate.now());
        blogService.saveAndNotifySubscribers(blog);
        return "redirect:/admin/blogs";
    }

    /**
     * Показва формата за добавяне на нова блог публикация.
     * @param model Модел, към който се добавя празен Blog обект.
     * @return Името на шаблона 'blog/blogForm'.
     */
    @GetMapping("/add")
    public String showBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "blog/blogForm";
    }

    /**
     * Показва формата за редактиране на съществуваща публикация.
     * @param id ID на публикацията за редактиране.
     * @param model Модел, към който се добавя намерената публикация.
     * @return Името на шаблона 'blog/editBlog' или страница за грешка.
     */
    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable("id") Long id, Model model) {
        Blog blog = blogService.findBlogById(id);
        if (blog != null) {
            model.addAttribute("blog", blog);
            return "blog/editBlog";
        }
        return "error/404";
    }

    /**
     * Обработва промените в съществуваща публикация.
     * @param id ID на публикацията.
     * @param updatedBlog Обект с обновените данни от формата.
     * @param redirectAttributes Атрибути за съобщения.
     * @return Пренасочване към главната страница на блога.
     */
    @PostMapping("/edit/{id}")
    public String editBlog(@PathVariable("id") Long id, @ModelAttribute("blog") Blog updatedBlog, RedirectAttributes redirectAttributes) {
        Blog existingBlog = blogService.findBlogById(id);
        if (existingBlog != null) {
            existingBlog.setName(updatedBlog.getName());
            existingBlog.setShortExplanation(updatedBlog.getShortExplanation());
            existingBlog.setBlogText(updatedBlog.getBlogText());
            existingBlog.setDate(LocalDate.now());
            existingBlog.setImage(updatedBlog.getImage());
            existingBlog.setCategories(updatedBlog.getCategories());
            existingBlog.setTags(updatedBlog.getTags());
            blogService.save(existingBlog);
            redirectAttributes.addFlashAttribute("success", "Блогът е обновен успешно.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Блогът не е намерен.");
        }
        return "redirect:/blog";
    }

    /**
     * Обработва изтриването на блог публикация (от администратор).
     * @param id ID на публикацията за изтриване.
     * @param redirectAttributes Атрибути за съобщения.
     * @return Пренасочване към списъка с блогове в административния панел.
     */
    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Blog blog = blogService.findBlogById(id);
        if (blog != null) {
            blogService.delete(blog);
            redirectAttributes.addFlashAttribute("success", "Блогът е изтрит успешно.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Блогът не е намерен.");
        }
        return "redirect:/admin/blogs";
    }
}