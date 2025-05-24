package com.localzero.api.controller;

import com.localzero.api.entity.*;
import com.localzero.api.service.*;
import com.localzero.api.enumeration.InitiativeCategory;
import com.localzero.api.enumeration.UserRole;
import com.localzero.api.template.InitiativeCreator;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class InitiativeController {

    private InitiativeCreator initiativeCreator;
    private InitiativeService initiativeService;
    private PersonService personService;
    private CommunityService communityService;
    private PostService postService;
    private InitiativeParticipantService InitiativeParticipantService;
    private NotificationService notificationService;

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

        Person creator = personService.findByEmail(user.getUsername());
        initiative.setCreator(creator);
        initiative.setCommunityMember(creator);

        Set<Community> selectedCommunities = (communityIds != null)
                ? new HashSet<>(communityService.findAllById(communityIds))
                : new HashSet<>();
        initiative.setCommunities(selectedCommunities);

        initiativeCreator.create(user.getUsername(), initiative);

        // Skaparen får Admin roll
        InitiativeParticipant admin = new InitiativeParticipant();
        admin.setInitiative(initiative);
        admin.setPerson(creator);
        admin.setRole(UserRole.ADMIN);
        admin.setJoinedAt(LocalDateTime.now());

        InitiativeParticipantService.save(admin);

        for (Community membership : selectedCommunities) {
            String memberEmail = membership.getMemberEmail();

            if (!memberEmail.equals(creator.getEmail())) {
                personService.findOptionalByEmail(memberEmail).ifPresent(member ->
                        notificationService.notify(member,
                                "A new initiative \"" + title + "\" was created in your community."
                        )
                );
            }
        }

        return "redirect:/feed";
    }

    @GetMapping("/feed")
    public String showInitiatives(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Person person = personService.findByEmail(currentUser.getUsername());
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
        model.addAttribute("allCommunities", communityService.findAll());
        return "create-initiative";
    }

    @GetMapping("/{id}")
    public String showEditInitiativeForm(@PathVariable Long id, Model model,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        Initiative initiative = initiativeService.getById(id);
        Person person = personService.findByEmail(currentUser.getUsername());

        boolean isParticipant = InitiativeParticipantService.isParticipantInInitiative(id, person.getEmail());

        model.addAttribute("initiative", initiative);
        model.addAttribute("isParticipant", isParticipant);

        model.addAttribute("currentUserEmail", currentUser.getUsername());

        List<InitiativeParticipant> participantsWithRoles =
                InitiativeParticipantService.findByInitiativeId(initiative.getId());

        String myRole = participantsWithRoles.stream()
                .filter(p -> p.getPerson().getEmail().equals(person.getEmail()))
                .map(p -> p.getRole().name())
                .findFirst().orElse("UNKNOWN");

        model.addAttribute("participantsWithRoles", participantsWithRoles);
        model.addAttribute("myRole", myRole);
        model.addAttribute("isCreator", initiative.getCreator().getEmail().equals(person.getEmail()));

        List<Post> posts = postService.getPostsByInitiativeId(initiative.getId());
        model.addAttribute("posts", posts);

        return "initiative-view";
    }

    @PostMapping("/{id}/join")
    public String joinInitiative(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        Initiative initiative = initiativeService.findById(id);

        Person person = personService.findByEmail(currentUser.getUsername());

        // Kontrollera om personen redan är deltagare
        Optional<InitiativeParticipant> existingParticipant =
                InitiativeParticipantService.optionalFindByInitiativeIdAndPersonEmail(id, person.getEmail());

        if (existingParticipant.isEmpty()) {
            InitiativeParticipant newParticipant = new InitiativeParticipant();
            newParticipant.setInitiative(initiative);
            newParticipant.setPerson(person);
            newParticipant.setRole(UserRole.MEMBER);
            newParticipant.setJoinedAt(LocalDateTime.now());

            InitiativeParticipantService.save(newParticipant);

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

        Initiative initiative = initiativeService.findById(id);

        InitiativeParticipant initiativeParticipant = InitiativeParticipantService.findByInitiativeIdAndPersonEmail(id, email);

        // Admin kan inte lämna
        if (initiativeParticipant == null || initiative.getCreator().getEmail().equals(email)) {
            String errorMessage = (initiativeParticipant == null)
                    ? "You are not a member of this initiative"
                    : "The creator cannot leave their own initiative.";

            Person person = personService.findByEmail(email);
            boolean isParticipant = (initiativeParticipant != null);

            List<InitiativeParticipant> participantsWithRoles =
                    InitiativeParticipantService.findByInitiativeId(initiative.getId());

            String myRole = participantsWithRoles.stream()
                    .filter(p -> p.getPerson().getEmail().equals(person.getEmail()))
                    .map(p -> p.getRole().name())
                    .findFirst()
                    .orElse("UNKNOWN");

            List<Post> posts = postService.getPostsByInitiativeId(initiative.getId());

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

        InitiativeParticipantService.delete(initiativeParticipant);

        // Ta även bort från Initiative.participants
        initiative.getParticipants().removeIf(p -> p.getEmail().equals(email));
        initiativeService.save(initiative);

        return "redirect:/feed";
    }

    @PostMapping("/{id}/roles")
    public String updateRole(@PathVariable Long id,
                             @RequestParam String targetEmail,
                             @RequestParam String newRole,
                             @AuthenticationPrincipal UserDetails currentUser) {
        Person current = personService.findByEmail(currentUser.getUsername());

        Initiative initiative = initiativeService.findById(id);

        // Endast admin kan ändra roller
        if (!initiative.getCreator().getEmail().equals(current.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can update roles.");
        }

        // creator kan inte ändra sin egen roll
        if (initiative.getCreator().getEmail().equals(targetEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "creator cannot change his/her role.");
        }

        // Ingen kan få ADMIN rollen förutom creator
        if ("ADMIN".equals(newRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can have admin role.");
        }

        InitiativeParticipant target = InitiativeParticipantService.findByInitiativeIdAndPersonEmail(id, targetEmail);

        try {
            UserRole parsedRole = UserRole.valueOf(newRole);
            target.setRole(parsedRole);
            InitiativeParticipantService.save(target);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal role!");
        }

        return "redirect:/initiatives/" + id;
    }
}