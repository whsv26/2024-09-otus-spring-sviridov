package me.whsv26.user.domain;

import com.google.common.collect.Sets;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.whsv26.user.infrastructure.GrantedAuthorityConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString()
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String password;

    @Convert(converter = GrantedAuthorityConverter.class)
    private Set<GrantedAuthority> authorities;

    public User(String username, String password) {
        this.authorities = Sets.newHashSet(new SimpleGrantedAuthority("ROLE_USER"));
        this.username = username;
        this.password = password;
    }
}
