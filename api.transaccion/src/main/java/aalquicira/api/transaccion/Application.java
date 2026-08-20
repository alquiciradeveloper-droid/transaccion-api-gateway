package aalquicira.api.transaccion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import aalquicira.api.transaccion.Entity.Usuario;
import aalquicira.api.transaccion.Repository.UsuarioRepository;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            
            if (usuarioRepository.findByUsuario("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsuario("admin");
                admin.setPassword(encoder.encode("admin123")); 
                usuarioRepository.save(admin);
                System.out.println(" Usuario 'admin' creado exitosamente con contraseña BCrypt.");
            }
        };
    }
}
