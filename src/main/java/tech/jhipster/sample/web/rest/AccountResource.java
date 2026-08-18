package tech.jhipster.sample.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.security.Principal;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.jhipster.sample.repository.UserRepository;
import tech.jhipster.sample.security.SecurityUtils;
import tech.jhipster.sample.service.MailService;
import tech.jhipster.sample.service.UserService;
import tech.jhipster.sample.service.dto.AdminUserDTO;
import tech.jhipster.sample.service.dto.PasswordChangeDTO;
import tech.jhipster.sample.web.rest.errors.*;
import tech.jhipster.sample.web.rest.vm.KeyAndPasswordVM;
import tech.jhipster.sample.web.rest.vm.ManagedUserVM;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Validated
public class AccountResource {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account resource request invalid")
    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    public AccountResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        LOG.debug("REST request to register account");
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        return userService.registerUser(managedUserVM, managedUserVM.getPassword()).doOnSuccess(mailService::sendActivationEmail).then();
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public Mono<Void> activateAccount(@RequestParam(value = "key") String key) {
        LOG.debug("REST request to activate account");
        return userService
            .activateRegistration(key)
            .switchIfEmpty(Mono.error(new AccountResourceException("No user was found for this activation key")))
            .then();
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)},
     * or with status {@code 401 (Unauthorized)} if not authenticated.
     */
    @GetMapping("/authenticate")
    public ResponseEntity<Void> isAuthenticated(Principal principal) {
        LOG.debug("REST request to check if the current user is authenticated");
        return ResponseEntity.status(principal == null ? HttpStatus.UNAUTHORIZED : HttpStatus.NO_CONTENT).build();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public Mono<AdminUserDTO> getAccount() {
        LOG.debug("REST request to get account");
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public Mono<Void> saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        LOG.debug("REST request to save account");
        return SecurityUtils.getCurrentUserLogin()
            .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
            .flatMap(userLogin ->
                userRepository
                    .findOneByEmailIgnoreCase(userDTO.getEmail())
                    .filter(existingUser -> !existingUser.getLogin().equalsIgnoreCase(userLogin))
                    .hasElement()
                    .flatMap(emailExists -> {
                        if (emailExists) {
                            throw new EmailAlreadyUsedException();
                        }
                        return userRepository.findOneByLogin(userLogin);
                    })
            )
            .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")))
            .flatMap(user ->
                userService.updateUser(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    userDTO.getLangKey(),
                    userDTO.getImageUrl()
                )
            );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public Mono<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        LOG.debug("REST request to change password");
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        return userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public Mono<Void> requestPasswordReset(@RequestBody @Email @Size(min = 5, max = 254) String mail) {
        LOG.debug("REST request to request password reset");
        return userService
            .requestPasswordReset(mail)
            .doOnSuccess(user -> {
                if (Objects.nonNull(user)) {
                    mailService.sendPasswordResetMail(user);
                } else {
                    // Pretend the request has been successful to prevent checking which emails really exist
                    // but log that an invalid attempt has been made
                    LOG.warn("Password reset requested for non existing mail");
                }
            })
            .then();
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public Mono<Void> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        return userService
            .completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
            .switchIfEmpty(
                Mono.defer(() ->
                    Mono.fromRunnable(() -> {
                        // Dummy hash to prevent reset-key enumeration via response-time timing attack:
                        // mirrors the bcrypt cost of a successful path so both branches take equal time.
                        passwordEncoder.encode(keyAndPassword.getNewPassword());
                    })
                        .subscribeOn(Schedulers.boundedElastic())
                        .then(Mono.error(new AccountResourceException("No user was found for this reset key")))
                )
            )
            .then();
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
