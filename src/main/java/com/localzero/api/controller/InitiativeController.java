package com.localzero.api.controller;

import com.localzero.api.entity.*;
import com.localzero.api.enumeration.InitiativeCategory;
import com.localzero.api.enumeration.UserRole;
import com.localzero.api.repository.*;
import com.localzero.api.service.InitiativeService;
import com.localzero.api.template.InitiativeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/initiatives")
@RequiredArgsConstructor
public class InitiativeController {

    private final InitiativeCreator ic;
    private final InitiativeRepository initiativeRepository;
    private final PersonRepository personRepository;
    private final CommunityRepository communityRepository;
    private final InitiativeService initiativeService;
    private final PostRepository postRepository;
    private final InitiativeParticipantRepository initiativeParticipantRepository;

    @PostMapping("/create")
    public String createInitiative(@RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam String location,
                                   @RequestParam String startDate,
                                   @RequestParam String endDate,
                                   @RequestParam InitiativeCategory category,
                                   @RequestParam(required = false, defaultValue = "false") boolean isPublic,
                                   @RequestParam(required = false) List<Long> communityIds,
                                   @AuthenticationPrincipal UserDetails user) {

        Initiative initiative = new Initiative();
        initiative.setTitle(title);
        initiative.setDescription(description);
        initiative.setLocation(location);
        initiative.setStartDate(LocalDateTime.parse(startDate));
        initiative.setEndDate(LocalDateTime.parse(endDate));
        initiative.setCategory(category);
        initiative.setPublic(isPublic);

        Person creator = personRepository.findByEmail(user.getUsername()).orElseThrow();
        initiative.setCreator(creator);
        initiative.setCommunityMember(creator);

        Set<Community> selectedCommunities = (communityIds != null)
                ? new HashSet<>(communityRepository.findAllById(communityIds))
                : new HashSet<>();
        initiative.setCommunities(selectedCommunities);

        ic.create(user.getUsername(), initiative);

        // Skkaparen får Admin roll
        InitiativeParticipant admin = new InitiativeParticipant();
        admin.setInitiative(initiative);
        admin.setPerson(creator);
        admin.setRole(UserRole.ADMIN);
        admin.setJoinedAt(LocalDateTime.now());

        initiativeParticipantRepository.save(admin);

        return "redirect:/feed";
    }

    @GetMapping("/feed")
    public String showInitiatives(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Person person = personRepository.findByEmail(currentUser.getUsername()).orElseThrow();
        List<Initiative> initiatives = initiativeService.getVisibleForUser(person);
        initiatives = initiatives.stream().filter(Objects::nonNull).toList();
        List<Initiative> myInitiatives = initiativeService.getByParticipant(person.getEmail());

        Set<Long> joinedInitiativeIds = myInitiatives.stream().map(Initiative::getId).collect(Collectors.toSet());

        model.addAttribute("initiatives", initiatives);
        model.addAttribute("joinedInitiativeIds", joinedInitiativeIds);

        return "initiative-feed";
    }

    @GetMapping("/new")
    public String showCreateInitiativeForm(Model model) {
        model.addAttribute("categories", InitiativeCategory.values());
        model.addAttribute("allCommunities", communityRepository.findAll());
        return "create-initiative";
    }

    @GetMapping("/{id}")
    public String showEditInitiativeForm(@PathVariable Long id, Model model,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        Initiative initiative = initiativeService.getById(id);
        Person person = personRepository.findByEmail(currentUser.getUsername()).orElseThrow();

        boolean isParticipant = initiativeParticipantRepository
                .findByInitiativeIdAndPersonEmail(id, person.getEmail())
                .isPresent();

        model.addAttribute("initiative", initiative);
        model.addAttribute("isParticipant", isParticipant);

        model.addAttribute("currentUserEmail", currentUser.getUsername());

        List<InitiativeParticipant> participantsWithRoles =
                initiativeParticipantRepository.findByInitiativeId(initiative.getId());

        String myRole = participantsWithRoles.stream()
                .filter(p -> p.getPerson().getEmail().equals(person.getEmail()))
                .map(p -> p.getRole().name())
                .findFirst().orElse("UNKNOWN");

        model.addAttribute("participantsWithRoles", participantsWithRoles);
        model.addAttribute("myRole", myRole);
        model.addAttribute("isCreator", initiative.getCreator().getEmail().equals(person.getEmail()));

        List<Post> posts = postRepository.findByInitiativeIdOrderByCreationDatetimeDesc(initiative.getId());
        model.addAttribute("posts", posts);

        return "initiative-view";
    }

    @PostMapping("/{id}/join")
    public String joinInitiative(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Person person = personRepository.findByEmail(currentUser.getUsername()).orElseThrow();

        // Kontrollera om personen redan är deltagare
        Optional<InitiativeParticipant> existingParticipant =
                initiativeParticipantRepository.findByInitiativeIdAndPersonEmail(id, person.getEmail());

        if (existingParticipant.isEmpty()) {
            InitiativeParticipant newParticipant = new InitiativeParticipant();
            newParticipant.setInitiative(initiative);
            newParticipant.setPerson(person);
            newParticipant.setRole(UserRole.MEMBER);
            newParticipant.setJoinedAt(LocalDateTime.now());

            initiativeParticipantRepository.save(newParticipant);

            try {
                initiativeService.addParticipant(id, currentUser.getUsername());
            } catch (Exception e) {
                System.out.println("Warning: InitiativeService.addParticipant failed, but participant was added manually");
            }
        }

        return "redirect:/initiatives/" + id;
    }

    @PostMapping("/{id}/leave")
    public String leaveInitiative(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails currentUser,
                                  Model model) {
        String email = currentUser.getUsername();

        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        InitiativeParticipant me = initiativeParticipantRepository
                .findByInitiativeIdAndPersonEmail(id, email)
                .orElse(null);

        // Admin kan inte lämna
        if (me == null || initiative.getCreator().getEmail().equals(email)) {
            String errorMessage = (me == null)
                    ? "Du är inte med i detta initiativ."
                    : "Skaparen kan inte lämna sitt eget initiativ.";

            Person person = personRepository.findByEmail(email).orElseThrow();
            boolean isParticipant = (me != null);

            List<InitiativeParticipant> participantsWithRoles =
                    initiativeParticipantRepository.findByInitiativeId(initiative.getId());

            String myRole = participantsWithRoles.stream()
                    .filter(p -> p.getPerson().getEmail().equals(person.getEmail()))
                    .map(p -> p.getRole().name())
                    .findFirst()
                    .orElse("UNKNOWN");

            List<Post> posts = postRepository.findByInitiativeIdOrderByCreationDatetimeDesc(initiative.getId());

            model.addAttribute("initiative", initiative);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("isParticipant", isParticipant);
            model.addAttribute("participantsWithRoles", participantsWithRoles);
            model.addAttribute("myRole", myRole);
            model.addAttribute("posts", posts);
            model.addAttribute("currentUserEmail", email);
            model.addAttribute("isCreator", initiative.getCreator().getEmail().equals(email));

            return "initiative-view";
        }

        initiativeParticipantRepository.delete(me);

        // Ta även bort från Initiative.participants
        initiative.getParticipants().removeIf(p -> p.getEmail().equals(email));
        initiativeRepository.save(initiative);

        return "redirect:/feed";
    }

    @PostMapping("/{id}/roles")
    public String updateRole(@PathVariable Long id,
                             @RequestParam String targetEmail,
                             @RequestParam String newRole,
                             @AuthenticationPrincipal UserDetails currentUser) {
        Person current = personRepository.findByEmail(currentUser.getUsername()).orElseThrow();

        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Endast skaparen (som är ADMIN) kan ändra roller
        if (!initiative.getCreator().getEmail().equals(current.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Endast skaparen kan ändra roller.");
        }

        // Skaparen kan inte ändra sin egen roll
        if (initiative.getCreator().getEmail().equals(targetEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Skaparen kan inte ändra sin egen roll.");
        }

        // Ingen kan få ADMIN rollen förutom skaparen
        if ("ADMIN".equals(newRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Endast skaparen kan ha ADMIN-rollen.");
        }

        InitiativeParticipant target = initiativeParticipantRepository
                .findByInitiativeIdAndPersonEmail(id, targetEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deltagaren hittades inte."));

        try {
            UserRole parsedRole = UserRole.valueOf(newRole);
            target.setRole(parsedRole);
            initiativeParticipantRepository.save(target);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ogiltig roll");
        }

        return "redirect:/initiatives/" + id;
    }
}