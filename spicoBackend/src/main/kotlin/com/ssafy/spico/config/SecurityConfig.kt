package com.ssafy.spico.config

import com.ssafy.spico.common.jwt.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import com.ssafy.spico.domain.user.repository.UserRepository
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userRepository: UserRepository,
    private val jwtFilter: JwtFilter
) {

    /* filter chain */
    @Bean
    fun filterChain(http: HttpSecurity
    ): SecurityFilterChain {
        http
            // default disable
            .csrf{ it.disable() }
            .formLogin{ it.disable() }
            .httpBasic { it.disable() }

            // stateless session
            .sessionManagement { session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // authorize rules
            .authorizeHttpRequests {
//                개발 끝나면 주석 해제
//                it.requestMatchers("/auth/login", "/actuator/prometheus").permitAll()
//                    .anyRequest().authenticated()
                it.anyRequest().permitAll()
            }

            // add jwt filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}