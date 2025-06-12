package LangForU_DevTeam.LangForU.security.config;

import LangForU_DevTeam.LangForU.appuser.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Помощен клас (Facade), който предоставя опростен и удобен достъп
 * до информацията за автентикация от контекста на Spring Security.
 * Абстрахира директната работа със {@link SecurityContextHolder},
 * като я прави по-лесна за използване и тестване в други части на приложението.
 */
@Component // Маркира класа като Spring компонент, за да може да бъде инжектиран.
public class AuthenticationFacade {

    /**
     * Извлича пълния обект {@link Authentication} от текущия контекст на сигурността.
     * Този обект съдържа детайли за потребителя (principal), неговите права (authorities)
     * и статуса на автентикацията.
     *
     * @return Текущият обект Authentication, или null, ако няма автентикиран потребител.
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Удобен метод за извличане на текущо логнатия потребител директно като обект от тип {@link AppUser}.
     * Извършва проверка и безопасно преобразуване на типа.
     *
     * @return Обект {@link AppUser}, представляващ текущо логнатия потребител.
     * @throws IllegalStateException ако няма автентикиран потребител или ако потребителят
     * не е инстанция на {@link AppUser} (напр. анонимен потребител).
     */
    public AppUser getAuthenticatedUser() {
        Authentication authentication = getAuthentication();
        // Проверява дали има автентикация и дали principal (основният обект) е от нашия потребителски клас.
        if (authentication != null && authentication.getPrincipal() instanceof AppUser) {
            return (AppUser) authentication.getPrincipal();
        }
        // Ако проверката е неуспешна, хвърля изключение.
        throw new IllegalStateException("Потребителят не е автентикиран");
    }
}