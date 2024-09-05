package LangForU_Development.LangForU.lections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectionService {

    @Autowired
    private LectionRepository lectionRepository;

    public List<Lection> findAll() {
        return lectionRepository.findAll();
    }

    public Lection findById(Long id) {
        return lectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Lection not found"));
    }

    public void saveLection(Lection lection) {
        lectionRepository.save(lection);
    }

    public void deleteById(Long id) {
        lectionRepository.deleteById(id);
    }
}