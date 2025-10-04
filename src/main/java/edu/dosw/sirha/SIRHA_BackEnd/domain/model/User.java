package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Authenticable;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;

/**
 * Entidad abstracta que representa a un usuario dentro del sistema SIRHA.
 *
 * Esta clase define los atributos y comportamientos básicos de todos los usuarios,
 * incluyendo credenciales de acceso y datos de identificación.
 *
 * Características principales:
 * - Identificación única en la base de datos (MongoDB)
 * - Username único para inicio de sesión
 * - Email de contacto asociado
 * - Contraseña almacenada en formato hash seguro (BCrypt)
 *
 * @see Authenticable
 * @see PasswordUtils
 */
@Document(collection = "users")
public abstract class User implements Authenticable {

    @Id
    private String id;

    @Field("username")
    private String username;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    /**
     * Constructor vacío requerido por frameworks de persistencia.
     */
    public User() {}

    /**
     * Constructor principal para inicializar un usuario.
     *
     * @param id identificador único del usuario
     * @param username nombre de usuario único
     * @param email correo electrónico del usuario
     * @param password contraseña en texto plano o hash (si comienza con $2a$ se asume hash BCrypt)
     */
    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password.startsWith("$2a$") ? password : PasswordUtils.hashPassword(password);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado.
     *
     * @param rawPassword contraseña en texto plano
     * @return true si la contraseña es válida, false en caso contrario
     */
    public boolean verifyPassword(String rawPassword) {
        return PasswordUtils.verifyPassword(rawPassword, this.password);
    }

    // ---------- Getters y Setters ----------

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el hash de la contraseña del usuario.
     * @return hash BCrypt de la contraseña
     */
    public String getPasswordHash() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * Se almacena en formato hash BCrypt para mayor seguridad.
     *
     * @param passwordHash contraseña en texto plano que será hasheada
     */
    public void setPasswordHash(String passwordHash) {
        this.password = PasswordUtils.hashPassword(passwordHash);
    }

    public String getEmail() {
        return email;
    }
}
