package LangForU_DevTeam.LangForU.finalexam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозитори интерфейс за управление на {@link FinalExam} ентитети.
 * Чрез наследяването на {@link JpaRepository}, този интерфейс автоматично получава
 * пълен набор от стандартни CRUD (Create, Read, Update, Delete) операции,
 * без да е необходимо да се пишат имплементации за тях.
 */
@Repository // Маркира интерфейса като Spring компонент за достъп до данни.
public interface FinalExamRepository extends JpaRepository<FinalExam, Long> {
    // Няма нужда от дефиниране на методи тук, тъй като се използват
    // вградените от JpaRepository, като save(), findById(), findAll(), deleteById() и др.
}