package ua.edu.ukma.objectanalysis.openvet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Admin;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.Veterinarian;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.repository.PendingOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.PetRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.UserRepository;
import ua.edu.ukma.objectanalysis.openvet.security.JwtProperties;

import java.util.HashSet;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class OpenVetApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PendingOwnerRepository pendingOwnerRepository;

    public OpenVetApplication(UserRepository userRepository, PetRepository petRepository, PendingOwnerRepository pendingOwnerRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.pendingOwnerRepository = pendingOwnerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(OpenVetApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Admin admin = new Admin();
        admin.setFirstName("Admin");
        admin.setLastName("");
        admin.setEmail("sdffdsfd");
        admin.setPassword("");
        admin.setPhoneNumber("sdfdsfsdf");

        userRepository.save(admin);

        Veterinarian vet = new Veterinarian();
        vet.setFirstName("Veterinarian");
        vet.setLastName("");
        vet.setEmail("34634643");
        vet.setPassword("");
        vet.setPhoneNumber("3546");

        userRepository.save(vet);

        PetEntity pet = new PetEntity();
        pet.setName("Motya");

        petRepository.save(pet);

        HashSet<PetEntity> pets = new HashSet<>();
        pets.add(pet);

        PendingOwnerEntity pendingOwner = new PendingOwnerEntity();
        pendingOwner.setPhoneNumber("380662426520");
        pendingOwner.setUnattachedPets(
                pets
        );

        pendingOwnerRepository.save(pendingOwner);
    }
}
